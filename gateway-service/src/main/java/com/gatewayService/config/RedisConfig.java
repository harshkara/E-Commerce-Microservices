package com.gatewayService.config;

import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@Configuration
public class RedisConfig {

   @Bean
    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory connectionFactory){

       RedisTemplate<String,String> template = new RedisTemplate<>();
       template.setConnectionFactory(connectionFactory);

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
