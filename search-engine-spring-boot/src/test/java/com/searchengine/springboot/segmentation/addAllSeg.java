package com.searchengine.springboot.segmentation;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.entity.Record;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter jiebaSegmenter = new JiebaSegmenter();

    @Test
    public void addAllSeg(){
        for (Record record : recordService.queryAllRecord()) {
            String sentence = record.getCaption();
            List<SegToken> segTokens = jiebaSegmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);

            Long recordId = record.getId();
            for (SegToken segToken : segTokens) {

                segmentationService.addSeg(segToken.word,recordId);
            }

        }

    }
}
