package com.searchengine.service.impl;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.searchengine.dao.SegmentationDao;
import com.searchengine.dao.TDao;
import com.searchengine.dto.Record;
import com.searchengine.entity.RecordSeg;
import com.searchengine.entity.T;
import com.searchengine.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    public boolean insert2(List<T> relations, String tableName) {
        tDao.insert2(relations, tableName);
        return true;
    }

    @Override
    public int getMaxId() {
        return tDao.getMaxId();
    }

    @Override
    public Map<String, Object> getRcord(String searchInfo, int pageSize, int pageNum) {
        // int offset = pageSize * (pageNum - 1);
        // StringBuilder sb = new StringBuilder();
        // JiebaSegmenter segmenter = new JiebaSegmenter();
        // List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);
        // boolean first = true;
        // for (int i = 0; i < segTokens.size(); i++) {
        //     if (segmentationDao.selectOneSeg(segTokens.get(i).word) == null) continue;
        //     int segId = segmentationDao.selectOneSeg(segTokens.get(i).word).getId();
        //     if (first) { sb.append(segId); first = false; }
        //     else sb.append(',').append(segId);
        // }
        // System.out.println(sb.toString().equals(""));
        // if(sb.toString().equals("")){
        //     return null;
        // } else {
        //     List<Record> records = tDao.getRecord(sb.toString(), pageSize, offset);
        //     int recordsNum = tDao.getRecordsNum(sb.toString());
        //     Map<String, Object> mp = new HashMap<>();
        //     mp.put("recordsNum", recordsNum);
        //     mp.put("records", records);
        //     return mp;
        // }
        return null;
    }

    @Override
    public Map<String, Object> getRcordUseSplit(String searchInfo, int pageSize, int pageNum) {
        int offset = pageSize * (pageNum - 1);
        StringBuilder sb = new StringBuilder();
        JiebaSegmenter segmenter = new JiebaSegmenter();

        // -----处理过滤词-----start
        String[] words = searchInfo.split("\\s+");
        List<String> filterWord = new ArrayList<>();
        boolean find = false;
        int filterWordIndex = -1;
        for (int i = 0; i < words.length; i++) {
            String str = words[i];
            if (Pattern.matches("^-.*?$", str)) {
                if (!find) {
                    filterWordIndex = searchInfo.indexOf(str);
                    find = true;
                }
                filterWord.add(str.substring(1));
            }

        }
        if (filterWordIndex != -1) {
            searchInfo = searchInfo.substring(0, filterWordIndex);
        }
        // -----处理过滤词-----end

        List<SegToken> segTokens = segmenter.process(searchInfo, JiebaSegmenter.SegMode.SEARCH);
        boolean first = true;
        for (int i = 0; i < segTokens.size(); i++) {
            if (segmentationDao.selectOneSeg(segTokens.get(i).word) == null) continue;
            if ("".equals(segTokens.get(i).word.trim())) continue;
            int segId = segmentationDao.selectOneSeg(segTokens.get(i).word).getId();
            int idx = segId % 100;
            if (first) {
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                first = false;
            } else {
                sb.append("union").append('\n');
                sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
            }
        }
        String info = sb.toString();
        String filterInfo = "";
        if ("".equals(info)) return null;
        boolean filterWordInSegmentation = false;
        if (filterWord.size() > 0) {
            sb.delete(0, sb.length());
            boolean fi = true;
            for (int i = 0; i < filterWord.size(); i++) {
                if (segmentationDao.selectOneSeg(filterWord.get(i)) == null) continue;
                filterWordInSegmentation = true;
                int segId = segmentationDao.selectOneSeg(filterWord.get(i)).getId();
                int idx = segId % 100;
                if (fi) {
                    sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                    fi = false;
                } else {
                    sb.append("union").append('\n');
                    sb.append("select * from data_seg_relation_").append(idx).append(" where seg_id = ").append(segId).append('\n');
                }
            }
            filterInfo = sb.toString();
        }
        List records = null;
        int recordsNum = 0;
        if (filterWord.size() > 0 && filterWordInSegmentation) {
            records = tDao.getRecordUseSplitFilter(info, filterInfo, pageSize, offset);
            recordsNum = tDao.getRecordsNumFilter(info, filterInfo);
        } else {
            records = tDao.getRecordUseSplit(info, pageSize, offset);
            recordsNum = tDao.getRecordsNum(info);
        }
        Map<String, Object> mp = new HashMap<>();
        mp.put("recordsNum", recordsNum);
        mp.put("records", records);
        return mp;
    }

    @Override
    public int createNewTable(String tableName) {
        tDao.createNewTable(tableName);
        return 0;
    }
}
