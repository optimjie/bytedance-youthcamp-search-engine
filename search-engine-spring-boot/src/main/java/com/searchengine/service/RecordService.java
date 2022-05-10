package com.searchengine.service;

import com.searchengine.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {

    List<Record> queryAllRecord();

    List<Record> queryRecordByWord(String word);

    List<Record> queryRecordFilter(String word);
}
