package com.searchengine.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import com.searchengine.entity.User;
import com.searchengine.service.RecordSegService;
import com.searchengine.service.RecordService;
import com.searchengine.service.SegmentationService;
import com.searchengine.service.TService;
import com.searchengine.utils.RedisUtil_db0;
import com.searchengine.utils.jieba.keyword.Keyword;
import com.searchengine.utils.jieba.keyword.TFIDFAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@Slf4j
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private SegmentationService segmentationService;

    @Autowired
    private RecordSegService recordSegService;


    @Autowired
    private RedisUtil_db0 redisUtil;

    private final int pageSize = 15;

    @Autowired
    private TService tService;


    TFIDFAnalyzer tfidfAnalyzer = new TFIDFAnalyzer();
    JiebaSegmenter segmenter = new JiebaSegmenter();

    /**
     * 查询所有文本
     *
     * @return
     */
    @GetMapping("/getAll")
    public List<Record> getAllRecord() {
        long start = System.currentTimeMillis();
        List<Record> records = recordService.queryAllRecord();
        long end = System.currentTimeMillis();
        System.out.println((end - start) + "ms");
        return records;
    }

    /**
     * redis 先查询redis,找不到再重定向到/search查询
     *
     * @param searchInfo
     * @return
     */
    @GetMapping("/search_redis")
    public List<RecordDto> search_redis(@RequestParam("word") String searchInfo, @RequestParam("pageNum") int pageNum, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<RecordDto> recordDtoList = null;
        if (redisUtil.hasKey(searchInfo)) {
            recordDtoList  = JSONObject.parseObject((String) redisUtil.get(searchInfo), new TypeReference<List<RecordDto>>(){});
        } else {
            //不能用重发请求，太慢了;重定向的word参数总是传不过去
            //request.getRequestDispatcher("/search?word=" + searchInfo + "&pageNum=" + pageNum).forward(request,response);
            log.info(searchInfo);
            recordDtoList = recordService.search(searchInfo);
            /*自定义排序器根据weight排序*/
            recordDtoList.sort(new Comparator<RecordDto>() {
                @Override
                public int compare(RecordDto o1, RecordDto o2) {
                    if ((o2.getWeight() > o1.getWeight())) return 1;
                    else if ((o2.getWeight() < o1.getWeight())) return -1;
                    return 0;
                }
            });
            List<RecordDto> finalRecordDtoList = recordDtoList;
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    String sList = JSONObject.toJSONString(finalRecordDtoList);
                    redisUtil.set(searchInfo, sList, 1800);
                }
            });
            thread.start();
        }
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        return recordDtoList.subList(start, end);
    }

    /**
     * 分词查询  根据tidif值从大到小排序
     *
     * @param searchInfo
     * @return
     */
    @GetMapping("/search")
    public List<RecordDto> search(@RequestParam("word") String searchInfo, @RequestParam("pageNum") int pageNum) {
        log.info(searchInfo);
        List<RecordDto> recordDtoList = recordService.search(searchInfo);


        /*自定义排序器根据weight排序*/
        recordDtoList.sort(new Comparator<RecordDto>() {
            @Override
            public int compare(RecordDto o1, RecordDto o2) {
                if ((o2.getWeight() > o1.getWeight())) return 1;
                else if ((o2.getWeight() < o1.getWeight())) return -1;
                return 0;
            }
        });
        /*截取结果集recordDtoList*/
        int start = (pageNum - 1) * pageSize;
        int end = pageNum * pageSize;
        return recordDtoList.subList(start, end);
    }

    @GetMapping("/search_test")
    public Map<String, Object> search_test(@RequestParam("word") String searchInfo, @RequestParam("pageNum") int pageNum) {
        return tService.getRcord(searchInfo, pageSize, pageNum);
    }

    @GetMapping("/related_word")
    public List<String> relatedWord(@RequestParam("word") String searchInfo){
        return segmentationService.getAllByWords(searchInfo);
    }

    @RequestMapping(value = "/imageUpload", method = RequestMethod.POST)
    @ResponseBody
    public String register(@RequestParam("file") MultipartFile file) {
        //文件上传
        if (!file.isEmpty()) {
            try {
                //获取图片名称
                String newCompanyImagepath = "D:\\"+file.getOriginalFilename();
                File newFile = new File(newCompanyImagepath);
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(newFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "图片上传失败！";
            } catch (IOException e) {
                e.printStackTrace();
                return "图片上传失败！";
            }
        }
        return "图片上传失败！";
    }

    /**
     * 新增文本
     *
     * @param record
     * @return
     */
    @PostMapping("/add")
    public boolean add(Record record) {
        return recordService.addRecord(record);
    }

    /**
     * 模糊查询
     *
     * @param word
     * @return
     */
    @GetMapping("/s_word")
    public List<Record> getRecordByWord(@RequestParam("word") String word) {
        System.out.println(word);
        log.info(word);
        return recordService.queryRecordByWord(word);
    }

}
