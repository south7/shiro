package com.example.shiro.controller;

import com.example.shiro.domain.JsonData;
import com.example.shiro.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("pub")
public class PublicController {
    @RequestMapping("need_login")
    public JsonData needLogin(){

        return JsonData.buildSuccess("温馨提示: 请使用对应的账号登录",-2);
    }

    @RequestMapping("not_permit")
    public JsonData notPermit(){

        return JsonData.buildSuccess("温馨提示: 拒绝访问,没有权限",-3);
    }

    @RequestMapping("index")
    public JsonData index(){
        List<String> list=new ArrayList<>();
        list.add("昨日晴空a");
        list.add("昨日晴空ab");
        list.add("昨日晴空abc");
        return JsonData.buildSuccess(list);
    }

    @RequestMapping("login")
    public JsonData login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken=new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try{
            subject.login(usernamePasswordToken);//调用CustomRealm中doGetAuthenticationInfo方法
            return JsonData.buildSuccess(subject.getSession().getId(),"登录成功");
        }catch (Exception e){
            e.printStackTrace();
            return JsonData.buildError("账号或密码错误");
        }
    }
    /**
     * logout不用开发实现
     * logout映射shiro自带的LogoutFilter过滤器，清除对应session->回调 shiroFilterFactoryBean.setLoginUrl("/pub/need_login")设置的方法
     * logout请求时需要带上token,这样才能清除对应用户的session
     */
}
