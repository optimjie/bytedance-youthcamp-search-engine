package com.searchengine.service;

import com.searchengine.dto.Record;
import com.searchengine.entity.T;
import org.springframework.stereotype.Service;

import java.util.List;


public interface TService {
    boolean insert1(List<String> segs);
    boolean insert2(List<T> relations);
    int getMaxId();
    List<Record> getRord(String searchInfo, int pageSize, int pageNum);
}
