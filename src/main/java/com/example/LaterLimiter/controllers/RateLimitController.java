package com.example.LaterLimiter.controllers;


import com.example.LaterLimiter.RedisRateLimiter.RedisRateLimiter;
import com.example.LaterLimiter.components.NaiveRateLimiter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    StringRedisTemplate redisTemplate;
    NaiveRateLimiter naiveRateLimiter = new NaiveRateLimiter();
    RedisRateLimiter redisRateLimiter = new RedisRateLimiter(redisTemplate);

    public RateLimitController(NaiveRateLimiter naiveRateLimiter,RedisRateLimiter redisRateLimiter)
    {
        this.naiveRateLimiter=naiveRateLimiter;
        this.redisRateLimiter=redisRateLimiter;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> getMethod(HttpServletRequest request)
    {
        String clientId = request.getRemoteAddr();
        boolean allowed = naiveRateLimiter.allow(clientId);
        if(allowed)
        {
            return ResponseEntity.ok("Pong");
        }
        else{
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Rate Limit exceeded");
        }
    }

    @GetMapping("/ping-api-redis")
    public ResponseEntity<String> getMethodRedis (HttpServletRequest request)
    {
        String clientId = request.getRemoteAddr();
        boolean allowed = redisRateLimiter.allow(clientId);
        if(allowed)
        {
            return ResponseEntity.ok("allowed Redis");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Token Limit exceeded");
        }
    }

}
