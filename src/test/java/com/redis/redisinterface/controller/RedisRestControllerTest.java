package com.redis.redisinterface.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redis.redisinterface.bean.RedisResponse;
import com.redis.redisinterface.bean.UserSession;
import com.redis.redisinterface.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RedisRestControllerTest {

    @Mock
    private RedisService<UserSession> redisService;

    @InjectMocks
    private RedisRestController redisRestController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreate_SuccessfulSave() {
        UserSession userSession = new UserSession("1", "session1", "portal1", "2023-01-01", "active");

        doNothing().when(redisService).save(eq("1"), any(UserSession.class));
        when(redisService.findById("1")).thenReturn(userSession);

        ResponseEntity<RedisResponse> response = redisRestController.create(userSession);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(true, response.getBody().isSuccess());
    }

    @Test
    public void testCreate_NullUserSession() {
        ResponseEntity<RedisResponse> response = redisRestController.create(null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().isSuccess());
    }

    @Test
    public void testCreate_FailedToVerifySavedData() {
        UserSession userSession = new UserSession("1", "session1", "portal1", "2023-01-01", "active");

        doNothing().when(redisService).save(eq("1"), any(UserSession.class));
        when(redisService.findById("1")).thenReturn(null);

        ResponseEntity<RedisResponse> response = redisRestController.create(userSession);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().isSuccess());
    }

    @Test
    public void testCreate_InternalServerError() {
        UserSession userSession = new UserSession("1", "session1", "portal1", "2023-01-01", "active");

        doThrow(new RuntimeException("Redis exception")).when(redisService).save(eq("1"), any(UserSession.class));

        ResponseEntity<RedisResponse> response = redisRestController.create(userSession);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().isSuccess());
    }
}