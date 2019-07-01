package com.example.shiro.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermission {

    private int id ;
    private int roleId;
    private int permissionId;
}
