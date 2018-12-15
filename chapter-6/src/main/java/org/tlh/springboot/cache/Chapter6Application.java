package org.tlh.springboot.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by 离歌笑tlh/hu ping on 2018/12/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@EnableCaching
@SpringBootApplication
public class Chapter6Application {

    public static void main(String[] args) {
        SpringApplication.run(Chapter6Application.class,args);
    }

    //处理redis缓存，设置序列化器，在Spring boot 2.0以后配置有所变化，在官方文档中有说明
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(){
        RedisCacheConfiguration config=RedisCacheConfiguration.defaultCacheConfig();
        //设置key的序列化器
        StringRedisSerializer keySerializer=new StringRedisSerializer();
        config=config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer));
        //设置value的序列化器
        Jackson2JsonRedisSerializer valueSerializer=new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper mapper=new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        valueSerializer.setObjectMapper(mapper);
        config=config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));
        return config;
    }

}
