
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;


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

    static HashSet<String> stopWordsSet;


    @Test
    public void addAllSeg(){

        //-----------------初始化-------------
        List<Record> records = recordService.queryAllRecord();
        List<Segmentation> segmentations = segmentationService.queryAllSeg();

        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000);

        if(stopWordsSet==null) {
            stopWordsSet=new HashSet<>();
            loadStopWords(stopWordsSet, this.getClass().getResourceAsStream("/jieba/stop_words.txt"));
        }

        for (Segmentation seg : segmentations) {
            bf.put(seg.getWord());
        }
        //----------------初始化结束---------------


        //----------------开始加词-----------------
        for (int loop=0;loop<100;loop++) {

            List<String> segs = new ArrayList<>(10000);
            List<RecordSeg> relations = new ArrayList<>(10000);

            int segMaxId = segmentationDao.getMaxId();  // 获取seg表中最大的id

            for (int i = loop*10000; i < (loop+1)*10000; i++) {  // 10000 15s
                Record record = records.get(i);
                String caption = record.getCaption();
                List<SegToken> segTokens = jiebaSegmenter.process(caption, JiebaSegmenter.SegMode.INDEX);
                List<Keyword> keywords = tfidfAnalyzer.analyze(caption,5);
                Map<String,RecordSeg> countMap = new HashMap<>();
                for (SegToken segToken : segTokens) {
                    String word = segToken.word;
                    if (stopWordsSet.contains(word)) continue;//判断是否是停用词
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
                    //--------------计数--------------
                    if (!countMap.containsKey(word)){
                        int count = 1;
                        countMap.put(word,new RecordSeg(dataId, segId, tf, count));
                    }else {
                        RecordSeg t = countMap.get(word);
                        int count = t.getCount();
                        t.setCount(++count);
                        countMap.put(word,t);
                    }
                    //--------------------------------
                }
                for (RecordSeg t : countMap.values()) {
                    relations.add(t);
                }
            }

            segmentationDao.insertBatchSeg(segs);
            recordSegDao.insertBatch(relations);
        }

    }

    private void loadStopWords(Set<String> set, InputStream in){
        BufferedReader bufr;
        try
        {
            bufr = new BufferedReader(new InputStreamReader(in));
            String line=null;
            while((line=bufr.readLine())!=null) {
                set.add(line.trim());
            }
            try
            {
                bufr.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
