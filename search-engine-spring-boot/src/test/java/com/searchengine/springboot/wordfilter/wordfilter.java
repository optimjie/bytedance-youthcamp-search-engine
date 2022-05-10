package com.searchengine.springboot.wordfilter;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class wordfilter {

    @Test
    void queryRecordFilter() {
        val matches = Pattern.matches("^-.*?$", "-asdas");
    }
}