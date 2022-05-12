
package com.searchengine.springboot.segmentation;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.common.SegResult;
import com.searchengine.dao.RecordDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dao.TDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.entity.T;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.service.TService;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * 扫描data表把所有内容分词并加入分词库
 */
@SpringBootTest
public class addAllSeg {

    @Autowired
    private RecordService recordService;
    @Autowired
    private SegmentationService segmentationService;
    @Autowired
    private SegmentationDao segmentationDao;
    @Autowired
    private RecordSegDao recordSegDao;
    @Autowired
    private RecordDao recordDao;
    @Autowired
    private TDao tDao;

    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();


    @Test
    public void addAllSeg(){

        List<Record> records = recordService.queryAllRecord();
        List<Segmentation> segmentations = segmentationService.queryAllSeg();

        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000);

        for (Segmentation seg : segmentations) {
            bf.put(seg.getWord());
        }

        List<String> segs = new ArrayList<>(10000);
        List<T> relations = new ArrayList<>(10000);

        int segMaxId = tDao.getMaxId();  // 获取seg表中最大的id

        for (int i = 0; i < 10000; i++) {  // 10000 15s
            Record record = records.get(i);
            String caption = record.getCaption();
            List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
            List<Keyword> keywords = tfidfAnalyzer.analyze(caption,5);
            for (SegToken segToken : segTokens) {
                String word = segToken.word;

                int segId = 0;
                boolean exist = false;
                if (!bf.mightContain(word)) {  // 不存在是一定不存在
                    bf.put(word);
                    segs.add(word);
                    segId = ++segMaxId;
                    segmentations.add(new Segmentation(segMaxId, word));
                } else {  // 但是存在不一定是真的存在，但是这种误报的可能性很小，所以这时全部遍历的时间开销是完全可以接受的。
                          // https://www.geeksforgeeks.org/bloom-filter-in-java-with-examples/ 误报概率参考，1千万分之一
                    // 需要检查一下是不是真的存在
                    for (Segmentation seg : segmentations) {
                        if (word.equals(seg.getWord())) {
                            segId = seg.getId();
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) {  // 和上面的操作相同
                        bf.put(word);
                        segs.add(word);
                        segId = ++segMaxId;
                        segmentations.add(new Segmentation(segMaxId, word));
                    }
                }

                int dataId = record.getId();
                double tf = 0;
                for (Keyword v : keywords) {
                    if (v.getName().equals(word)) {
                        tf = v.getTfidfvalue();
                        break;
                    }
                }
                T relation = new T(dataId, segId, tf, 1);
                relations.add(relation);
            }
        }

        tDao.insert1(segs);
        tDao.insert2(relations);

    }

}
