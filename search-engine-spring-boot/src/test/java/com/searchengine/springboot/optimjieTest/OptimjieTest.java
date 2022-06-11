package com.searchengine.springboot.optimjieTest;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dao.TDao;
import com.searchengine.entity.Record;
import com.searchengine.entity.T;
import com.searchengine.service.RecordService;
import com.searchengine.service.TService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class OptimjieTest {

    @Autowired
    private TDao tDao;

    @Autowired
    private SegmentationDao segmentationDao;

    @Autowired
    private RecordService recordService;

    @Test
    void queryRecordFilter() {
        val matches = Pattern.matches("^-.*?$", "-asdas");
    }

    @Test
    void BloomFilter() {
        BloomFilter<String> bf = BloomFilter.create(Funnels.stringFunnel(Charset.forName("UTF-8")),10000000);
        long a = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            bf.put("" + i);
        }
        for (int i = 0; i < 1000000; i++) {
            bf.mightContain("" + (int)(Math.random() % 20000000));
        }
        long b = System.currentTimeMillis();
        System.out.println(b - a);
    }

    @Test
    void insert() {
        // tDao.createNewTable("data_seg_relation_1");

        // tDao.createNewTable("data_seg_relation_test");
        List<T> list = new ArrayList<>();
        list.add(new T(1, 1, 1.0,1));
        list.add(new T(2, 2, 2.0,2));
        tDao.insert2(list, "data_seg_relation_test");
    }

    @Test
    void test1() {
        StringBuilder sb = new StringBuilder();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process("中国足球", JiebaSegmenter.SegMode.SEARCH);
        boolean first = true;
        for (int i = 0; i < segTokens.size(); i++) {
            if (segmentationDao.selectOneSeg(segTokens.get(i).word) == null) continue;
            int segId = segmentationDao.selectOneSeg(segTokens.get(i).word).getId();
            int idx = segId % 100;
            if (first) {
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(idx).append('\n');
                first = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(idx).append('\n');
            }
        }
        System.out.println("=======\n" + sb.toString() + "\n========");
    }

    @Test
    void test2() {
        StringBuilder sb = new StringBuilder();
        System.out.println("".equals(sb.toString()));
    }

    @Test
    void test3() {
        List<Record> records = recordService.queryAllRecord();
        System.out.println(records.size());
    }

    @Test
    void test4() {
        List<String> l = new ArrayList<>();
        l.add("11");
        l.add("22");
        tDao.insert1(l);
    }
}