package com.games.rps.api.config;

import org.springframework.context.annotation.*;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

@Configuration
@PropertySource("classpath:test.properties")
@EnableRedisRepositories(basePackages = "com.games.rps.api.repo")
@ComponentScan({"com.games.rps.api.controller", "com.games.rps.api.service"})
public class RedisTestConfig {
    @Bean
    @Primary
    JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    public ApplicationConfig applicationConfigMock() {
        return new ApplicationConfig();
    }
}
