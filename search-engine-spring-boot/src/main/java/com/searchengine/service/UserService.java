package com.searchengine.service;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    boolean checkUserName(String username);
}
