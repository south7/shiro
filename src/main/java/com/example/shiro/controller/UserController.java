package com.example.shiro.controller;


import com.example.shiro.domain.User;
import com.example.shiro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("pub")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("find_user_info")
    public Object findByUserInfo(@RequestParam("username")String username){
        User user = userService.findAllUserInfoByUsername(username);
        return  user;
    }


}
