
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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













        // for (int i = 0;i<1000;i++) {  // 1000 2m 3s
        //     /*
        //     对于每个文本信息
        //     1.进行分词 得到分词表segtokens 和分词+tidif表keywords ——> List<SegResult> 传给addSeg方法
        //      */
        //     int dataId = i+1;
        //     List<SegResult> segResults = new ArrayList<>();
        //     Set<String> words = new HashSet<>();
        //     Record record = records.get(i);
        //     String sentence = record.getCaption();
        //     List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
        //     List<Keyword> keywords=tfidfAnalyzer.analyze(record.getCaption(),5);
        //     for (SegToken segToken : segTokens) {
        //         String word = segToken.word;
        //         if (!words.contains(word)){
        //             words.add(word);
        //             SegResult segResult = new SegResult();
        //             segResult.setWord(word);
        //             segResult.setRecordId(dataId);
        //             segResult.setCount(1);
        //             segResults.add(segResult);
        //             for (Keyword keyword : keywords) {
        //                 if (keyword.getName().equals(word)){
        //                     segResult.setTidifValue(keyword.getTfidfvalue());
        //                 }
        //             }
        //         }else {
        //             for (SegResult segResult : segResults) {
        //                 if (segResult.getWord().equals(word)){
        //                     int count = segResult.getCount();
        //                     segResult.setCount(++count);
        //                 }
        //             }
        //         }
        //     }
        //
        //
        //     //把列表存入seg表
        //     segmentationService.addSeg(segResults);
        // }

    }

//    /**
//     * 试试用hashset判断分词会不会快一点
//     * 分词存到2w2左右会报错
//     */
//    @Test
//    public void addAllSeg(){
//
//        HashSet<String> hs = new HashSet<>();
//        for (Record record : recordService.queryAllRecord()) {
//            String sentence = record.getCaption();
//            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
//
//            Long recordId = record.getId();
//            for (SegToken segToken : segTokens) {
//                String word = segToken.word;
//                if (!hs.contains(word)){
//                    //加入分词表
//                    segmentationDao.insertSeg(word);
//                    hs.add(segToken.word);
//                }
//                Long segId = segmentationDao.selectOneSeg(word).getId();
//                //加入关系表
//                RecordSeg recordSeg = new RecordSeg();
//                recordSeg.setSegId(segId);
//                recordSeg.setDataId(recordId);
//                if (recordSegDao.selectOneRecordSeg(recordId,segId)==null) {
//                    recordSeg.setCount(1);
//                    recordSegDao.insertRecordSeg(recordSeg);
//                }
//                else {
//                    //文中出现次数>1
//                    recordSeg.setCount(2);
//                    recordSegDao.updateRecordSeg(recordSeg);
//                }
//
//            }
//
//        }
//
//    }

//    @Test
//    public void addTIDIF(){
//
//        //查询每个dataId对应分词的tidif值
//        for (int i = 39357; i <= 50258; i++) {
//            //分表查询
//            Record record = recordDao.selectById(i);
//            List<Keyword> list=tfidfAnalyzer.analyze(record.getCaption(),5);
//            for(Keyword word:list){
//                //对于每个seg和对应的值 存入recordSeg表
//                Segmentation seg = segmentationDao.selectOneSeg(word.getName());
//
//                if (seg !=null ) {
//                    Integer segId = seg.getId();
//                    RecordSeg recordSeg = new RecordSeg();
//                    recordSeg.setDataId(i);
//                    recordSeg.setSegId(segId);
//                    recordSeg.setTidifValue(word.getTfidfvalue());
//                    recordSegDao.updateRecordSeg(recordSeg);
//                }
//            }
//        }
//
//    }
}
