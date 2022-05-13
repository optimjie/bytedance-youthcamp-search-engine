package com.searchengine.springboot;

import com.searchengine.dao.RecordSegDao;
import com.searchengine.utils.RedisUtil_db1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

    @Autowired
    private RecordSegDao recordSegDao;

    @Autowired
    private RedisUtil_db1 redisUtilDb1;

    @Test
    void contextLoads() {
//        RecordSeg recordSeg = new RecordSeg();
//        Long l = new Long(1);
//        recordSeg.setDataId(l);
//        recordSeg.setSegId(l);
//        recordSeg.setCount(2);
//        recordSegDao.updateRecordSeg(recordSeg);

    }

    @Test
    void testRedis(){
        redisUtilDb1.set("name", "joker");
    }

}
