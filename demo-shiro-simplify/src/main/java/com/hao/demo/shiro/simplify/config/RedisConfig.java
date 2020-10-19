package com.hao.demo.shiro.simplify.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.hao.demo.shiro.simplify.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis集群配置
 */
@Configuration
@Slf4j
public class RedisConfig {
    /**
     * 实例化 RedisTemplate 对象
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(false);
        return redisTemplate;
    }
    /**
     * 操作工具类
     */
    @Bean
    public RedisUtil redisUtil(RedisTemplate<String, Object> redisTemplate){
        log.info("初始化redisUtil");
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.setRedisTemplate(redisTemplate);
        return redisUtil;
    }
}
