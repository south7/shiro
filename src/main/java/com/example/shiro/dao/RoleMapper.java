package com.example.shiro.dao;

import com.example.shiro.domain.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface RoleMapper {
    @Select("SELECT r.id as id ,r.name as name ,r.description as description FROM user_role ur " +
            " LEFT JOIN role r ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
    @Results(
            value = {
                    @Result(id=true,property = "id",column = "id"),
                    @Result(property = "name",column = "name"),
                    @Result(property = "description",column = "description"),
                    @Result(property = "permissionList",column = "id",
                    many = @Many(select = "com.example.shiro.dao.PermissionMapper.findPermissionListByRoleId",fetchType = FetchType.DEFAULT)
                   )
            }
    )
    List<Role>findRoleListByUserId(@Param("userId") int userId);
}
