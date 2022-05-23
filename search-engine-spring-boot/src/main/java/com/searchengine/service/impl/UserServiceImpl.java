package com.searchengine.service.impl;

import com.searchengine.dao.UserDao;
import com.searchengine.entity.LoginUser;
import com.searchengine.entity.User;
import com.searchengine.service.UserService;
import com.searchengine.utils.JwtUtil;
import com.searchengine.utils.RedisUtil_db0;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RedisUtil_db0 redisUtil;

    @Override
    public String checkToken(String username) {
        User user = userDao.queryOne(username);
        String token = (String) redisUtil.get("login" + user.getId());
        return token;
    }

    @Override
    public boolean checkUserName(String username) {
        User user = userDao.queryOne(username);
        return user == null;
    }


    @Override
    public int register(User user) {
        user.setId(UUID.randomUUID().toString().replaceAll("-",""));
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return userDao.insertOne(user);
    }

    @Override
    public Map<String, String> login(User user) {
        Map<String, String> rs = new HashMap<>();
        //用户认证
        LoginUser userDetails = (LoginUser) userDetailsService.loadUserByUsername(user.getUsername());
        //判断认证是否通过
        if(passwordEncoder.matches(user.getPassword(), userDetails.getPassword())){
            //认证通过
            //使用userId生成一个jwt,存到redis中
            User detailsUser = userDetails.getUser();
            String id = detailsUser.getId();
            String jwt = JwtUtil.createJWT("ourTeam", 1000 * 60 * 60 * 6, id);//6小时

            rs.put("token", jwt);
            rs.put("username", detailsUser.getUsername());
            rs.put("message", "success");

            redisUtil.set("login" + detailsUser.getId(), jwt,60 * 60 * 6); //6小时
            return rs;
        }else{
            rs.put("message", "failure");
            return rs;
        }
    }

    @Override
    public User getUserByName(String username) {
        return userDao.queryOne(username);
    }
}