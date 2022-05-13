package com.searchengine.service;

import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.entity.T;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordSegService {

    List<Integer> queryRecordBySeg(Segmentation segmentation);

    boolean addBatch(List<RecordSeg> relations);
}
