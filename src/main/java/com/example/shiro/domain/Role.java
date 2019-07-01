package com.example.shiro.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
@Getter
@Setter
public class Role implements Serializable {
    private int id;
    private String name;
    private String description;
    private List<Permission> permissionList;
}
