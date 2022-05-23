package com.searchengine.dao;

import com.searchengine.dto.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.T;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDao {
    boolean insert1(@Param("segs")List<String> segs);  // 添加分词表
    boolean insert2(@Param("relations")List<T> relations, @Param("tableName")String tableName);  // 添加关系表
    int getMaxId();
    List<Record> getRecord(@Param("segIds")String segIds, @Param("pageSize")int pageSize, @Param("offset")int offset);
    List<Record> getRecordUseSplit(@Param("info")String info, @Param("pageSize")int pageSize, @Param("offset")int offset);
    int getRecordsNum(@Param("segIds")String segIds);
    int createNewTable(@Param("tableName")String tableName);
}
