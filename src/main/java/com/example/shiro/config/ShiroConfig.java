package com.example.shiro.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.AbstractSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager){
        System.out.println("执行 ShiroFilterFactoryBean.shiroFilter");
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //未登录则调用
        shiroFilterFactoryBean.setLoginUrl("/pub/need_login");
        //未授权则调用
        shiroFilterFactoryBean.setUnauthorizedUrl("/pub/not_permit");

        //设置自定义Filter
        Map<String, Filter> filterMap=new HashMap<>();
        filterMap.put("customRoles",new CustomRolesAuthorizationFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        Map<String,String>filterChainDefinitionMap=new LinkedHashMap<>();//保证顺序执行
        filterChainDefinitionMap.put("/logout","logout");
        filterChainDefinitionMap.put("/pub/**","anon");
        filterChainDefinitionMap.put("/authc/**","authc");
        filterChainDefinitionMap.put("/admin/**","roles[admin]");//shiro内置filter
        //filterChainDefinitionMap.put("/admin/**","customRoles[admin,root]");//自定义filter
        filterChainDefinitionMap.put("/video/update","perms[video_update]");
        filterChainDefinitionMap.put("/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }
    @Bean
    public  SecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(sessionManager());
        defaultWebSecurityManager.setCacheManager(redisCacheManager());//redis缓存
        defaultWebSecurityManager.setRealm(customRealm());
        return defaultWebSecurityManager;
    }
    @Bean
    public CustomRealm customRealm(){
        CustomRealm customRealm=new CustomRealm();
        customRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return customRealm;
    }
    @Bean
    public SessionManager sessionManager(){
        CustomSessionManager customSessionManager=new CustomSessionManager();
        customSessionManager.setGlobalSessionTimeout(AbstractSessionManager.DEFAULT_GLOBAL_SESSION_TIMEOUT);//默认30分钟过期
        //customSessionManager.setGlobalSessionTimeout(30000);//30s
        customSessionManager.setSessionDAO(redisSessionDAO());//配置session持久化
        return customSessionManager;
    }
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher=new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);//md5散列2次
        return hashedCredentialsMatcher;
    }

    public RedisManager redisManager(){
        RedisManager redisManager=new RedisManager();
        redisManager.setHost("192.168.1.35:6379");
        return redisManager;
    }

    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager=new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(30);//单位:s
        return redisCacheManager;
    }

    public RedisSessionDAO redisSessionDAO(){
        RedisSessionDAO redisSessionDAO=new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        //设置SessionId生成器
        redisSessionDAO.setSessionIdGenerator(new CustomSessionIdGenerator());
        return redisSessionDAO;
    }

    //管理shiro一些bean的生命周期 即bean初始化 与销毁
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //加入注解的使用，不加入这个AOP注解不生效(shiro的注解 例如 @RequiresGuest)
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
        return authorizationAttributeSourceAdvisor;
    }
    //用来扫描上下文寻找所有的Advistor(通知器), 将符合条件的Advisor应用到切入点的Bean中，需要在LifecycleBeanPostProcessor创建后才可以创建
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator=new DefaultAdvisorAutoProxyCreator();
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }
}
