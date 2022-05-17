package com.searchengine.controller;

import com.searchengine.entity.Role;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/existUserName")
    public void existUserName(@RequestParam("username") String username){
        boolean result = userService.checkUserName(username);
    }

    @PostMapping(value = "/login")
    @ResponseBody
    public User login(@RequestBody User user){
        User user_token = userService.addToken(user);
        return user_token;
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public Map<String, String> register(@RequestBody User user) {
        Map<String, String> map = new HashMap<>();
        if (!userService.checkUserName(user.getUsername())) {
            logger.error("用户名已存在");
            map.put("message", "userNameExist");
            return map;
        }else{
            int register = userService.register(user);
            if(register > 0){
                map.put("message", "success");
            }else{
                map.put("message", "failure");
            }
            return map;
        }
    }
}
