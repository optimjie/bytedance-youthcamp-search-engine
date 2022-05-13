package com.searchengine.dao;

import com.searchengine.entity.T;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TDao {
    boolean insert1(@Param("segs")List<String> segs);
    boolean insert2(@Param("relations")List<T> relations);
    int getMaxId();
}
