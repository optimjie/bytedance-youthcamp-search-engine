package com.searchengine.service;

import com.searchengine.dto.RecordDto;
import com.searchengine.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {

    List<Record> queryAllRecord();

    List<Record> selectPartialRecords(int limit, int offset);

    List<Record> queryRecordByWord(String word);

    List<Record> queryRecordFilter(String word);

    List<RecordDto> search(String searchInfo);

    Boolean addRecord(Record record);

}
