package com.example.shiro.service.impl;

import com.example.shiro.dao.RoleMapper;
import com.example.shiro.dao.UserMapper;
import com.example.shiro.domain.Role;
import com.example.shiro.domain.User;
import com.example.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Override

    public User findAllUserInfoByUsername(String username) {
        User user = userMapper.findByUsername(username);
        List<Role> roleList = roleMapper.findRoleListByUserId(user.getId());
        user.setRoleList(roleList);
        return user;
    }

    @Override
    public User findSimpleUserInfoById(int userId) {
        return null;
    }

    @Override
    public User findSimpleUserInfoByUsername(String username) {
        return null;
    }
}
