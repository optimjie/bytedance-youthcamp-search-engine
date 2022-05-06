package com.searchengine.controller;

import com.searchengine.entity.Record;
import com.searchengine.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;

    @GetMapping("/getAll")
    public List<Record> getAllRecord(){
        long start = System.currentTimeMillis();
        List<Record> records = recordService.queryAllRecord();
        long end=System.currentTimeMillis();
        System.out.println((end-start)+"ms");
        return records;
    }
}
