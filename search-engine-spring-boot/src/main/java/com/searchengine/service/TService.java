package com.searchengine.service;

import com.searchengine.entity.T;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TService {
    boolean insert1(List<String> segs);
    boolean insert2(List<T> relations);
    int getMaxId();
}
