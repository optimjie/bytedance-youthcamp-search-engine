package com.searchengine.springboot;

import com.searchengine.dao.RecordSegDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.RecordSeg;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RecordSegDao recordSegDao;
    @Test
    void contextLoads() {
        RecordSeg recordSeg = new RecordSeg();
        Long l = new Long(1);
        recordSeg.setDataId(l);
        recordSeg.setSegId(l);
        recordSeg.setCount(2);
        recordSegDao.updateRecordSeg(recordSeg);

    }

}
