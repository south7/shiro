package com.example.shiro.config;

import com.alibaba.druid.util.StringUtils;
import com.example.shiro.domain.Permission;
import com.example.shiro.domain.Role;
import com.example.shiro.domain.User;
import com.example.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

public class CustomRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 授权
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("授权 doGetAuthorizationInfo");
        User newUser=(User)principalCollection.getPrimaryPrincipal();
        User user = userService.findAllUserInfoByUsername(newUser.getUsername());
        List<String> stringRoleList=new ArrayList<>();
        List<String> stringPermissionList=new ArrayList<>();
        List<Role> roleList = user.getRoleList();
        for (Role role:roleList){
            stringRoleList.add(role.getName());
            List<Permission> permissionList = role.getPermissionList();
            for(Permission permission :permissionList){
                if(null!=permission)
                stringPermissionList.add(permission.getName());
            }
        }
        SimpleAuthorizationInfo simpleAuthorizationInfo=new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(stringRoleList);
        simpleAuthorizationInfo.addStringPermissions(stringPermissionList);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("认证 doGetAuthenticationInfo");
        String username = (String)authenticationToken.getPrincipal();
        User user = userService.findAllUserInfoByUsername(username);
        String pwd=user.getPassword();
        if(StringUtils.isEmpty(pwd)){
            return null;
        }
        return new SimpleAuthenticationInfo(user,user.getPassword(),this.getClass().getName());
    }
}
