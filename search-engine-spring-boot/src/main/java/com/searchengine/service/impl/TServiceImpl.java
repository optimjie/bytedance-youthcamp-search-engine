package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dao.TDao;
import com.searchengine.dto.Record;
import com.searchengine.entity.T;
import com.searchengine.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TServiceImpl implements TService {
    @Autowired
    private TDao tDao;
    @Autowired
    private SegmentationDao segmentationDao;
    @Override
    public boolean insert1(List<String> segs) {
        tDao.insert1(segs);
        return true;
    }
    @Override
    public boolean insert2(List<T> relations) {
        tDao.insert2(relations);
        return true;
    }

    @Override
    public int getMaxId() {
        return tDao.getMaxId();
    }

    @Override
    public List<Record> getRord(String searchInfo, int pageSize, int pageNum) {
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);
        boolean first = true;
        for (int i = 0; i < segTokens.size(); i++) {
            if (segmentationDao.selectOneSeg(segTokens.get(i).word) == null) continue;
            int segId = segmentationDao.selectOneSeg(segTokens.get(i).word).getId();
            if (first) { sb.append(segId); first = false; }
            else sb.append(',').append(segId);
        }
        return tDao.getRecord(sb.toString(), pageSize, offset);
    }
}