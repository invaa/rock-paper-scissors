package com.games.rps.api.config;


import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;
import java.util.concurrent.Executor;

@Slf4j
@Configuration
@EnableRedisRepositories(basePackages = "com.games.rps.api.repo")
@EncryptablePropertySource("classpath:application.properties")
@ComponentScan({"com.games.rps.api.controller", "com.games.rps.api.service"})
public class ApplicationConfig {

    @Value("${rps.cleanup.on.shutdown:false}")
    private boolean isCleanUp;

    @Getter
    @Value("${rps.game.max.rounds:20}")
    private int maxRounds;

    @Autowired
    private RedisConnectionFactory factory;

    @PreDestroy
    public void cleanRedis() {
        if (isCleanUp) {
            factory.getConnection().flushDb();
            log.info("Database flushed.");
        }
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        return template;
    }

    @Bean
    public Executor asyncExecutor(
            @Value("${rpc.async.thread.pool.min.size:8}") int minPoolSize,
            @Value("${rpc.async.thread.pool.max.size:8}") int maxPoolSize,
            @Value("${rpc.async.queue.capacity:100000}") int queueCapacity,
            @Value("${rpc.async.thread.name.prefix:rpc-game-}") String threadNamePrefix
            ) {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(minPoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }
}
