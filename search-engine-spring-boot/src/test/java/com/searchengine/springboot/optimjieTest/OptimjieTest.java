package com.searchengine.springboot.optimjieTest;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

@SpringBootTest
class OptimjieTest {

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
}