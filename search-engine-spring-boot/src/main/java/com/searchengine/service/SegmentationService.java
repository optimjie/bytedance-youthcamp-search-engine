package com.searchengine.service;

import com.searchengine.entity.Segmentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SegmentationService {

    List<Segmentation> queryAllSeg();

    Boolean addSeg(String word,Long recordId,Double tidifValue);


}
