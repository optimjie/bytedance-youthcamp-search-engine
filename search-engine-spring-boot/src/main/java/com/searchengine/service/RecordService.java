package com.searchengine.service;

import com.searchengine.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {

    List<Record> queryAllRecord();
}