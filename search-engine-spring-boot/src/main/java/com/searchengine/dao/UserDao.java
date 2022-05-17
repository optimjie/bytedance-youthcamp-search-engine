package com.searchengine.dao;

import com.searchengine.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User queryOne(String username);

    int insertOne(User user);
}
