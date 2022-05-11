package com.searchengine.service;

import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordSegService {

    List<Integer> queryRecordBySeg(Segmentation segmentation);

    int addBatch(List<RecordSeg> recordSegList);
}
