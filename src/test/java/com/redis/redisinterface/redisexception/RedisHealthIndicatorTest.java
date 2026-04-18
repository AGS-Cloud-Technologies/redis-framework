package com.redis.redisinterface.redisexception;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RedisHealthIndicatorTest {

    @Test
    void isHealthy_returnsTrueWhenPingSucceeds() throws Exception {
        RedisTemplate<String, Object> template = mock(RedisTemplate.class);
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        RedisConnection connection = mock(RedisConnection.class);

        when(template.getConnectionFactory()).thenReturn(factory);
        when(factory.getConnection()).thenReturn(connection);
        when(connection.ping()).thenReturn("PONG");

        RedisHealthIndicator indicator = new RedisHealthIndicator(template);

        boolean isHealthy = indicator.isHealthy();
        assertThat(isHealthy).isTrue();

        verify(connection, times(1)).ping();
    }

    @Test
    void isHealthy_returnsFalseWhenPingThrows() throws Exception {
        RedisTemplate<String, Object> template = mock(RedisTemplate.class);
        RedisConnectionFactory factory = mock(RedisConnectionFactory.class);
        RedisConnection connection = mock(RedisConnection.class);

        when(template.getConnectionFactory()).thenReturn(factory);
        when(factory.getConnection()).thenReturn(connection);
        when(connection.ping()).thenThrow(new RuntimeException("no redis"));

        RedisHealthIndicator indicator = new RedisHealthIndicator(template);

        boolean isHealthy = indicator.isHealthy();
        assertThat(isHealthy).isFalse();

        verify(connection, times(1)).ping();
    }

    @Test
    void isHealthy_returnsFalseWhenConnectionFactoryIsNull() {
        RedisTemplate<String, Object> template = mock(RedisTemplate.class);
        when(template.getConnectionFactory()).thenReturn(null);

        RedisHealthIndicator indicator = new RedisHealthIndicator(template);

        boolean isHealthy = indicator.isHealthy();
        assertThat(isHealthy).isFalse();
    }
}

