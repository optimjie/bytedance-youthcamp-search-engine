package com.searchengine.service;

import com.searchengine.dto.RecordDto;
import com.searchengine.entity.Record;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RecordService {

    List<Record> queryAllRecord();

    List<Record> queryRecordByWord(String word);

<<<<<<< HEAD
    List<Record> queryRecordFilter(String word);
=======
    List<RecordDto> search(String searchInfo);

    Boolean addRecord(Record record);
>>>>>>> d25f4753dc6ea31faadb14ca9d5c71b674e4faaa
}
