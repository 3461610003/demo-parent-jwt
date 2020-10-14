package com.hao.demo.shiro.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro子配置
 */
@Configuration
@Slf4j
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager){
        log.info("1111   初始化ShiroFilterFactoryBean");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, Filter> map = new HashMap<>();
        map.put("user", getJWTFilter());
        map.put("auth", getJWTAuthFilter());

        shiroFilterFactoryBean.setFilters(map);
        //拦截器
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<>();
        //配置不会被拦截的链接 顺序判断
        filterChainDefinitionMap.put("/static/**","anon");
        filterChainDefinitionMap.put("/login","anon");
        filterChainDefinitionMap.put("/informs/**","anon");
        filterChainDefinitionMap.put("/img-code","anon");
        filterChainDefinitionMap.put("/sms-code","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/favicon.ico","anon");

        //swagger
//        filterChainDefinitionMap.put("/configuration/ui","anon");
//        filterChainDefinitionMap.put("/swagger-ui.html","anon");
//        filterChainDefinitionMap.put("/webjars/**","anon");
//        filterChainDefinitionMap.put("/images/**","anon");
//        filterChainDefinitionMap.put("/swagger-resources/**","anon");

        filterChainDefinitionMap.put("/admins/current","user");
        filterChainDefinitionMap.put("/admins/menus","user");
        filterChainDefinitionMap.put("/logout","user");
        // 后台首页过滤
        filterChainDefinitionMap.put("/index/**","user");
        filterChainDefinitionMap.put("/market/queryMarket","user");

        //过滤链定义，从上向下执行，一般将/**放在最下面 ==》注意！！！
        //authc：所有url都必须认证通过才能访问；anno：所有url都可以匿名访问
        filterChainDefinitionMap.put("/**","auth");
        //如果不设置默认会自动寻找Web工程目录下的 login.jsp 页面
        shiroFilterFactoryBean.setLoginUrl("/unAuth");
//        //登陆成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index");
//
//        //未授权的页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /**
     * 自定义过滤器
     * @return MyFilter
     */
    @Bean
    public JWTFilter getJWTFilter(){
        return new JWTFilter();
    }

    /**
     * 设置过滤优先级 低于shiro
     * @return
     */
    @Bean
    public FilterRegistrationBean<JWTFilter> registration2() {
        FilterRegistrationBean<JWTFilter> registration = new FilterRegistrationBean<>(getJWTFilter());
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 自定义过滤器
     * @return MyFilter
     */
    @Bean
    public JWTAuthFilter getJWTAuthFilter(){
        return new JWTAuthFilter();
    }

    /**
     * 设置过滤优先级 低于shiro
     * @return
     */
    @Bean
    public FilterRegistrationBean<JWTAuthFilter> registration() {
        FilterRegistrationBean<JWTAuthFilter> registration = new FilterRegistrationBean<>(getJWTAuthFilter());
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 安全管理器
     * @return DefaultWebSecurityManager
     */
    @Bean
    public DefaultWebSecurityManager securityManager(){

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        securityManager.setRealm(adminUserRealm());
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        //关闭shiro自带的session
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);

        securityManager.setSubjectDAO(subjectDAO);
        // 自定义缓存实现 使用redis
 //       securityManager.setCacheManager(cacheManager());
        return securityManager;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }

//    /**
//     * 配置shiro redisManager
//     * <p>
//     * 使用的是shiro-redis开源插件
//     *
//     * @return
//     */
//    @Bean
//    public RedisManager redisManager() {
//        RedisManager redisManager = new RedisManager();
//        redisManager.setHost(host);
//        redisManager.setPort(port);
//        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
//        return redisManager;
//    }

    /**
     * 配置shiro redisClusterManager
     * <p>
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
//    @Bean
//    public RedisClusterManager redisClusterManager(){
//        RedisClusterManager redisClusterManager = new RedisClusterManager();
//        redisClusterManager.setHost(hosts);
//        redisClusterManager.setTimeout(1800);
//        return  redisClusterManager;
//    }

//    /**
//     * cacheManager 缓存 redis实现
//     * <p>
//     * 使用的是shiro-redis开源插件
//     *
//     * @return
//     */
//    @Bean(name = "RedisShiroCacheManager")
//    public RedisCacheManager cacheManager() {
//        RedisCacheManager redisCacheManager = new RedisCacheManager();
//        redisCacheManager.setExpire(1800);// 配置缓存过期时间
//        //redisCacheManager.setRedisManager(redisClusterManager());
//        redisCacheManager.setRedisManager(redisManager());
//        return redisCacheManager;
//    }


    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    @Bean
    public AdminUserRealm adminUserRealm(){
        return new AdminUserRealm();
    }

}
