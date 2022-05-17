package com.searchengine.service;

import com.searchengine.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {

    boolean checkUserName(String username);

    User addToken(User user);

    int register(User user);
}
