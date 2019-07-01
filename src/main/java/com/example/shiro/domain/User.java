package com.example.shiro.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class User implements Serializable {

    private int id;
    private String username;
    private String password;
    private String salt;
    private List<Role> roleList;


}
