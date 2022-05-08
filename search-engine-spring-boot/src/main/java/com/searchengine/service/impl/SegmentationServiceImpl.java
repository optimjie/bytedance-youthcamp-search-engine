package com.searchengine.service.impl;

import com.searchengine.dao.RecordSegDao;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.Segmentation;
import com.searchengine.service.SegmentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SegmentationServiceImpl implements SegmentationService {

    @Autowired
    private SegmentationDao segmentationDao;

    @Autowired
    private RecordSegDao recordSegDao;

    @Override
    public List<Segmentation> queryAllSeg() {
        return segmentationDao.selectAllSeg();
    }

    @Override
    public Boolean addSeg(String word,Long dataId) {

        Segmentation segmentation = new Segmentation();
        Segmentation seg = segmentationDao.selectOneSeg(word);
        if (seg == null){
            //分词不存在 加入分词表
            segmentationDao.insertSeg(word);
        }
        Long segId = segmentationDao.selectOneSeg(word).getId();
        //加入关系表
        RecordSeg recordSeg = new RecordSeg();
        recordSeg.setSegId(segId);
        recordSeg.setDataId(dataId);
        if (recordSegDao.selectOneRecordSeg(dataId,segId)==null) {
            recordSeg.setCount(1);
            recordSegDao.insertRecordSeg(recordSeg);
        }
        else {
            //文中出现次数+1
            int count = recordSeg.getCount();
            count++;
            recordSeg.setCount(count);
            recordSegDao.updateRecordSeg(recordSeg);
        }

        return true;
    }


}
