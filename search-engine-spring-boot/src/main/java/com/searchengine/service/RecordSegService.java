package com.searchengine.service;

import com.searchengine.entity.Segmentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordSegService {

    List<Long> queryRecordBySeg(Segmentation segmentation);
}
