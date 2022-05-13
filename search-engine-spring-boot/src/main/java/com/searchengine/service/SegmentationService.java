package com.searchengine.service;

import com.searchengine.common.SegResult;
import com.searchengine.entity.Segmentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SegmentationService {

    List<Segmentation> queryAllSeg();

    Boolean addSeg(String word,Integer recordId,Double tidifValue);

    Boolean addSeg(String word,Integer recordId);

    /*
    之前的addSeg方法每个分词都要查询一遍分词表
    新方法传入一个SegResult对象列表，遍历一次分词库表得到所有分词信息(没做出来。。。)
     */
    Boolean addSeg(List<SegResult> segResults);

    int getMaxId();
    boolean insertBatchSeg(List<String> segs);

}
