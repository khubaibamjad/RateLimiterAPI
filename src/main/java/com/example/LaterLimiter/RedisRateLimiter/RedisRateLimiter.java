package com.example.LaterLimiter.RedisRateLimiter;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    long capacity = 10;
    double refillRate = 2.0;

    public RedisRateLimiter(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    Boolean allow(String clientId)
    {
        String key = "ratelimit:" + clientId;

        String tokenStr = (String) redisTemplate.opsForHash().get("key", "tokens");
        String timeStamp = (String) redisTemplate.opsForHash().get("key","timestamp");
        return true;
    }

}
