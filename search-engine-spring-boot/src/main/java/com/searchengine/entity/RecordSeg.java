package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordSeg {
    private Integer dataId;
    private Integer segId;
    private Double tidifValue;
    private Integer count;

}
