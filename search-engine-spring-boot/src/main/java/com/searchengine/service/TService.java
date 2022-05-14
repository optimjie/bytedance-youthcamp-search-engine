package com.searchengine.service;

import com.searchengine.dto.Record;
import com.searchengine.entity.T;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface TService {
    boolean insert1(List<String> segs);
    boolean insert2(List<T> relations);
    int getMaxId();
    Map<String, Object> getRcord(String searchInfo, int pageSize, int pageNum);
}
