package com.searchengine.controller;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.RecordDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordSegService;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private RecordDao recordDao;

    @Autowired
    private SegmentationService segmentationService;

    @Autowired
    private RecordSegService recordSegService;

    @Autowired
    private SegmentationDao segmentationDao;


    JiebaSegmenter segmenter = new JiebaSegmenter();

    @GetMapping("/getAll")
    public List<Record> getAllRecord(){
        long start = System.currentTimeMillis();
        List<Record> records = recordService.queryAllRecord();
        long end=System.currentTimeMillis();
        System.out.println((end-start)+"ms");
        return records;
    }

    @GetMapping("/search")
    public List<Record> search(@RequestBody String searchInfo){
        //调用jieba分词进行分词
        log.info(searchInfo);
        List<Record> recordList = new ArrayList<>();
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.INDEX);
        for (SegToken token : segTokens) {
            //查出每个分词对应的caption
            log.info("分词为{}",token.word);
            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
            List<Long> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);
            for (Long Recordid : RecordsIdList) {
                recordList.add(recordDao.selectById(Recordid));
            }
        }

        return recordList;
    }
}
