package com.searchengine.dao;

import com.searchengine.entity.Segmentation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentationDao {
    //查看所有分词
    List<Segmentation> selectAllSeg();
    //加入新分词
    int insertSeg(String word);
    //查询单个分词
    Segmentation selectOneSeg(String word);
}
