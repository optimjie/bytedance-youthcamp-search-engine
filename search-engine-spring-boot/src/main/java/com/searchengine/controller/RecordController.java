package com.searchengine.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.RecordDao;
import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dto.RecordDto;
import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordSegService;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.regex.Pattern;

@RestController
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;



    @Autowired
    private SegmentationService segmentationService;

    @Autowired
    private RecordSegService recordSegService;



    TFIDFAnalyzer tfidfAnalyzer=new TFIDFAnalyzer();
    JiebaSegmenter segmenter = new JiebaSegmenter();

    /**
     * 查询所有文本
     * @return
     */
    @GetMapping("/getAll")
    public List<Record> getAllRecord(){
        long start = System.currentTimeMillis();
        List<Record> records = recordService.queryAllRecord();
        long end=System.currentTimeMillis();
        System.out.println((end-start)+"ms");
        return records;
    }

//    /**
//     * 分词查询
//     * @param searchInfo
//     * @return
//     */
//    @GetMapping("/search")
//    public List<Record> search(@RequestParam("word") String searchInfo){
//        //调用jieba分词进行分词
//        log.info(searchInfo);
//        List<Record> recordList = new ArrayList<>();
//        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.INDEX);
//        for (SegToken token : segTokens) {
//            //查出每个分词对应的caption
//            log.info("分词为{}",token.word);
//            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
//            if (oneSeg!=null) {
//                List<Long> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);
//                for (Long dataId : RecordsIdList) {
//                    recordList.add(recordDao.selectById(dataId));
//                }
//            }
//        }
//
//
//        return recordList;
//    }

    /**
     * 分词查询  根据tidif值从大到小排序
     * @param searchInfo
     * @return
     */
    @GetMapping("/search")
    public List<RecordDto> search(@RequestParam("word") String searchInfo){
        //调用jieba分词进行分词
        log.info(searchInfo);
<<<<<<< HEAD

        // ======检查是否需要过滤======start
        Set<Long> set = new HashSet<>();
        List<SegToken> segTokensFilter = new ArrayList<>();
        String[] searchWords = searchInfo.split("\\s+");
        for (int i = searchWords.length - 1; i >= 1; i--) {
            if (Pattern.matches("^-.*?$", searchWords[i])) {
                List<SegToken> l = segmenter.process(searchWords[i].substring(1, searchWords[i].length()), JiebaSegmenter.SegMode.INDEX);
                for (SegToken v : l) {
                    segTokensFilter.add(v);
                }
            } else {
                break;
            }
        }
        for (SegToken token : segTokensFilter) {
            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
            if (oneSeg != null) {
                List<Long> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);  // 包含过滤词分词的所有recordID
                for (Long v : RecordsIdList) {
                    set.add(v);
                }
            }
        }
        String temp = "";
        String[] strs = searchInfo.split(" ");
        for (String v : strs) {
            if (!Pattern.matches("^-.*?$", v)) temp = temp + v;
        }
        searchInfo = temp;
        // ======检查是否需要过滤======end

        List<Record> recordList = new ArrayList<>();
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.INDEX);
        List<RecordDto> recordDtoList = new ArrayList<>();
        for (SegToken token : segTokens) {

            //查出每个分词对应的caption
            log.info("分词为{}",token.word);
            Segmentation oneSeg = segmentationDao.selectOneSeg(token.word);
            Double tidif = new Double(0);
            if (oneSeg!=null) {
                List<Long> RecordsIdList = recordSegService.queryRecordBySeg(oneSeg);//包含该分词的所有recordID
                for (Long dataId : RecordsIdList) {
                    if (set.contains(dataId)) continue;  // 若包含需要过滤的词 continue
                    //对于每个record对象 查询该分词对应的tidif加入recordDto
                    RecordDto recordDto = new RecordDto();
                    BeanUtils.copyProperties(recordDao.selectById(dataId),recordDto);
                    if (recordDto.getRecordSegs()==null){
                        List<RecordSeg> recordSegList= new ArrayList<>();
                        RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                        tidif =recordSeg.getTidifValue();
                        recordSegList.add(recordSeg);
                        recordDto.setRecordSegs(recordSegList);
                    }else {
                        List<RecordSeg> recordSegs = recordDto.getRecordSegs();
                        RecordSeg recordSeg = recordSegDao.selectOneRecordSeg(dataId, oneSeg.getId());
                        tidif =recordSeg.getTidifValue();
                        recordSegs.add(recordSeg);
                        recordDto.setRecordSegs(recordSegs);
                    }
                    Double weight = recordDto.getWeight() + tidif;
                    recordDto.setWeight(weight);
                    recordDtoList.add(recordDto);
                }
            }
        }
=======
        List<RecordDto> recordDtoList = recordService.search(searchInfo);

>>>>>>> d25f4753dc6ea31faadb14ca9d5c71b674e4faaa

        //选择排序  忘了springboot里咋排序了 先凑合用
        for (int i = 0; i < recordDtoList.size()-1; i++) {
            int index = i;
            for (int j = i + 1; j < recordDtoList.size(); j++) {
                if(recordDtoList.get(index).getWeight() > recordDtoList.get(j).getWeight()){
                    index = j;
                }
            }
            RecordDto tmp = recordDtoList.get(i);
            recordDtoList.set(i,recordDtoList.get(index));
            recordDtoList.set(index,tmp);
        }
        Collections.reverse(recordDtoList);



        return recordDtoList;
    }
    /**
     * 新增文本
     * @param record
     * @return
     */
    @PostMapping("/add")
    public boolean add(Record record){
        return recordService.addRecord(record);
    }

    /**
     * 模糊查询
     * @param word
     * @return
     */
    @GetMapping("/s_word")
    public List<Record> getRecordByWord(@RequestParam("word") String word){
        System.out.println(word);
        log.info(word);
        return recordService.queryRecordByWord(word);
    }


}
