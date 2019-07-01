package com.example.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class AuthenticationTest {
    private SimpleAccountRealm simpleAccountRealm=new SimpleAccountRealm();
    private DefaultSecurityManager defaultSecurityManager=new DefaultSecurityManager();

    @Before
    public void init(){
        simpleAccountRealm.addAccount("admin","123456");
        defaultSecurityManager.setRealm(simpleAccountRealm);
    }

    @Test
    public void test(){
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken("admin", "123456");
        subject.login(usernamePasswordToken);
        System.out.println("认证结果: "+subject.isAuthenticated());

    }

}
