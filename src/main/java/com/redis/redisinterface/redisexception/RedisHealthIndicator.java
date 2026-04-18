package com.redis.redisinterface.redisexception;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis Health Check Component
 * Provides health status of Redis connection.
 *
 * Note: Health indicator interface was moved in Spring Boot 4.0.
 * This is a placeholder component for future implementation.
 */
@Component
public class RedisHealthIndicator {

    private final RedisTemplate<String, ?> redisTemplate;
    
    public RedisHealthIndicator(RedisTemplate<String, ?> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    public boolean isHealthy() {
        try {
            if (redisTemplate.getConnectionFactory() != null) {
                var connection = redisTemplate.getConnectionFactory().getConnection();
                if (connection != null) {
                    connection.ping();
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}