package com.example.shiro.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Permission implements Serializable {

    private int id;
    private String name;
    private String url;
}
