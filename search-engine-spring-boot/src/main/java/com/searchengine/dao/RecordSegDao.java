package com.searchengine.dao;

import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordSegDao {
    //查看所有关系
    List<RecordSeg> selectAllRecordSeg();
    //根据recordid和segid查询单个关系
    RecordSeg selectOneRecordSeg(@Param("dataId") Integer recordId, @Param("segId") Integer segId);
    //根据segId查找对应的recordId
    List<RecordSeg> selectRecordBySeg(@Param("segId") Integer segId);
    //存储关系表
    int insertRecordSeg(RecordSeg recordSeg);
    //更新关系表
    int updateRecordSeg(RecordSeg recordSeg);

}
