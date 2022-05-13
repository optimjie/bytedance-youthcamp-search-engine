package com.searchengine.service.impl;

import com.searchengine.dao.RecordSegDao;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.RecordSegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordSegServiceImpl implements RecordSegService {

    @Autowired
    private RecordSegDao recordSegDao;

    @Override
    public List<Integer> queryRecordBySeg(Segmentation segmentation) {
        List<Integer> recordIdlist= new ArrayList<>();
        for (RecordSeg recordSeg : recordSegDao.selectRecordBySeg(segmentation.getId())) {
            recordIdlist.add(recordSeg.getDataId());
        }
        return recordIdlist;
    }

    @Override
    public int addBatch(List<RecordSeg> recordSegList) {
        int count = 0;
        for (RecordSeg recordSeg : recordSegList) {
            count+=recordSegDao.insertRecordSeg(recordSeg);
        }
        return count;
    }
}
