package com.searchengine.springboot.segmentation;

import com.baomidou.mybatisplus.extension.api.R;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.RecordDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.HashSet;
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


    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    @Test
    public void addAllSeg(){
//        for (Record record : recordService.queryAllRecord()) {
//            String sentence = record.getCaption();
//            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
//
//            Long recordId = record.getId();
//            for (SegToken segToken : segTokens) {
//
//                segmentationService.addSeg(segToken.word,recordId);
//            }
//
//        }
        List<Record> records = recordService.queryAllRecord();

        for (int i = 40998;i<=50258;i++) {
            Record record = records.get(i);
            String sentence = record.getCaption();
            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);

            Long recordId = record.getId();
            for (SegToken segToken : segTokens) {

                segmentationService.addSeg(segToken.word,recordId,null);
        }
        }
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

    @Test
    public void addTIDIF(){

        //查询每个dataId对应分词的tidif值
        for (int i = 0; i < 50258; i++) {
            Long l = new Long(i);
            Record record = recordDao.selectById(l);
            List<Keyword> list=tfidfAnalyzer.analyze(record.getCaption(),5);
            for(Keyword word:list){
                //对于每个seg和对应的值 存入recordSeg表
                Segmentation seg = segmentationDao.selectOneSeg(word.getName());

                if (seg !=null ) {
                    Long segId = seg.getId();
                    RecordSeg recordSeg = new RecordSeg();
                    recordSeg.setDataId(l);
                    recordSeg.setSegId(segId);
                    recordSeg.setTidifValue(word.getTfidfvalue());
                    recordSegDao.updateRecordSeg(recordSeg);
                }
            }
        }

    }
}
