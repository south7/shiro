package com.example.shiro.service;

import com.example.shiro.dao.RoleMapper;
import com.example.shiro.dao.UserMapper;
import com.example.shiro.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public interface UserService {


    public User findAllUserInfoByUsername(String username);
    public User findSimpleUserInfoById(int userId);
    public User findSimpleUserInfoByUsername(String username);
}
