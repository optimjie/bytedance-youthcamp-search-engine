package com.searchengine.common;

import lombok.Data;

@Data
public class SegResult {
    int recordId;        //文本数据id
    String word;        //分词
    int count;         //分词出现次数
    double tidifValue;//分词对应该文本的权重值
}
