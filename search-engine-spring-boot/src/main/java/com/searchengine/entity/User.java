package com.searchengine.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;

    private String username;

    private String password;

    private String token;

    //权限
    //权限默认为普通用户
    private Role role = Role.DEFAULT;
}
