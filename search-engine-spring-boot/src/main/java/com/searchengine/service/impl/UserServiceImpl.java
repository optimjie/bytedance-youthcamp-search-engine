package com.searchengine.service.impl;

import com.searchengine.dao.UserDao;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean checkUserName(String username) {
        User user = userDao.queryOneById(username);
        return user == null;
    }
}
