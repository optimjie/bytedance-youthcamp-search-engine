package com.searchengine.common;


import com.searchengine.dao.SegmentationDao;
import com.searchengine.entity.Segmentation;
import com.searchengine.utils.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
/**
 * @author: 将某些数据缓存到全局变量中
 * @description: TODO
 * @date: 2022-06-03 18:24
 */
public class CodeCache {

    public static Trie trie = new Trie();

    @Autowired
    private SegmentationDao segmentationDao;

    @PostConstruct
    public void init() {
        List<Segmentation> segmentations = segmentationDao.selectAllSeg();
        for (int i = 0; i < segmentations.size(); i++) {
            String word = segmentations.get(i).getWord();
            trie.add(word);
        }
    }
}
