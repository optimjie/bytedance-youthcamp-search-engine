package com.searchengine.dao;

import com.searchengine.entity.Segmentation;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SegmentationDao {
    //查看所有分词
    List<Segmentation> selectAllSeg();
    //加入新分词
    int insertSeg(String word);
    //查询单个分词对应的id
    Segmentation selectOneSeg(String word);
    //根据id查询
    Segmentation selectOneById(@Param("id") int id);
    //查询最大id
    int getMaxId();
    //批量插入分词
    boolean insertBatchSeg(@org.apache.ibatis.annotations.Param("segs")List<String> segs);
}
