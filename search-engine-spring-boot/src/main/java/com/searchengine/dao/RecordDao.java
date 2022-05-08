package com.searchengine.dao;

import com.searchengine.entity.Record;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordDao {

    List<Record> selectAllRecords();

    Record selectById(@Param("id") Long id);
}