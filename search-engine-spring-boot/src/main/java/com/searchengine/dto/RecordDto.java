package com.searchengine.dto;

import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecordDto extends Record {
    private List<RecordSeg> recordSegs = new ArrayList<>();
    private double weight;
}
