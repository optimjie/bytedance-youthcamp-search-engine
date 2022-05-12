package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 分词实体类
 */
@Data
@AllArgsConstructor
public class Segmentation {
    private int id;
    private String word;
}
