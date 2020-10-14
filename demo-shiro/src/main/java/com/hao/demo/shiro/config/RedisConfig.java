package com.hao.demo.shiro.config;

import com.hao.demo.shiro.util.FastJsonRedisSerializer;
import com.hao.demo.shiro.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    //集群节点
//    @Value("${spring.redis.cluster.nodes}")
//    private String clusterNodes;
    //最大重定向数
//    @Value("${spring.redis.cluster.max-redirects}")
//    private Integer maxRedirects;
    //操作超时时间
//    @Value("${spring.redis.timeout}")
//    private Integer timeout;
//    @Bean(name = "jedisClusterConfig")
//    public RedisClusterConfiguration getClusterConfiguration() {
//
//        if(isCluster){
//            Map<String, Object> source = new HashMap<>();
//            source.put("spring.redis.cluster.nodes", clusterNodes);
//            source.put("spring.redis.cluster.timeout", timeout);
//            source.put("spring.redis.cluster.max-redirects", maxRedirects);
//
//            return new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
//        }else{
//            return null;
//        }
//    }

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

//    @Bean
//    public JedisClusterUtil jedisClusterUtil(@Qualifier(value = "redisTemplate")RedisTemplate<String, Object> redisTemplate){
//        if(isCluster){
//            JedisClusterUtil jedisClusterUtil = new JedisClusterUtil();
//            jedisClusterUtil.setRedisTemplate(redisTemplate);
//            return jedisClusterUtil;
//        }else{
//            return null;
//        }
//    }

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
