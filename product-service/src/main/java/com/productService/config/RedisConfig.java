package com.productService.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {

        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        StringRedisSerializer serializer = new StringRedisSerializer();

        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public LettuceClientConfigurationBuilderCustomizer
    lettuceClientConfigurationBuilderCustomizer() {

        return builder -> builder.commandTimeout(
                Duration.ofSeconds(1)
        );
    }

}
