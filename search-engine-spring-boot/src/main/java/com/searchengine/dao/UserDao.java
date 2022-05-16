package com.searchengine.dao;

import com.searchengine.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User queryOneById(String username);
}
