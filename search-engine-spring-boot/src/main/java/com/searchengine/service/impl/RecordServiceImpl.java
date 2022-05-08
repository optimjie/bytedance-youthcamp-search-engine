package com.searchengine.service.impl;

import com.searchengine.dao.RecordDao;
import com.searchengine.entity.Record;
import com.searchengine.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordDao recordDao;

    @Override
    public List<Record> queryAllRecord() {
        return recordDao.selectAllRecords();
    }
}