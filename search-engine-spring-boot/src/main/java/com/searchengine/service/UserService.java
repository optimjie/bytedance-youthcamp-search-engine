package com.searchengine.service;

import com.searchengine.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserService {

    String checkToken(String username);

    boolean checkUserName(String username);

    int register(User user);

    Map<String, String> login(User user);

    User getUserByName(String username);
}
