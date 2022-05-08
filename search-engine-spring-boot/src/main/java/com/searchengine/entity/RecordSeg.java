package com.searchengine.entity;

import lombok.Data;

@Data
public class RecordSeg {
    private Long id;
    private Long dataId;
    private Long segId;
    private double tidifValue;
    private int count;
    private double weight;

}
