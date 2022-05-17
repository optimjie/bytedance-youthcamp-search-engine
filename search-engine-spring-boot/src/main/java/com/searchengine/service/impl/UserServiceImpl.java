package com.searchengine.service.impl;

import com.searchengine.dao.UserDao;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public boolean checkUserName(String username) {
        User user = userDao.queryOne(username);
        return user == null;
    }

    @Override
    public User addToken(User user) {
        User result = userDao.queryOne(user.getUsername());
        if(result != null){
            result.setToken(JwtUtil.createToken(result.getUsername()));
            return result;
        }else{
            return null;
        }
    }

    @Override
    public int register(User user) {
        user.setId(UUID.randomUUID().toString().replaceAll("-",""));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userDao.insertOne(user);
    }
}
