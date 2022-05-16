package com.searchengine.controller;

import com.searchengine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/existUserName")
    public void existUserName(@RequestParam("username") String username){
        boolean result = userService.checkUserName(username);
    }
}
