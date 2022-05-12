package com.searchengine.service.impl;

import com.searchengine.dao.TDao;
import com.searchengine.entity.T;
import com.searchengine.service.TService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TServiceImpl implements TService {
    @Autowired
    private TDao tDao;
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
}
