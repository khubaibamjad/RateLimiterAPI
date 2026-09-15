package com.example.LaterLimiter.RedisRateLimiter;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    private final long capacity = 10;
    private final double refillRate = 2.0;

    public RedisRateLimiter(StringRedisTemplate redisTemplate)
    {
        this.redisTemplate = redisTemplate;
    }

    boolean allow(String clientId)
    {
        String key = "ratelimit:" + clientId;

        String tokenStr = (String) redisTemplate.opsForHash().get(key, "tokens");
        String timeStampStr = (String) redisTemplate.opsForHash().get(key,"timestamp");


        long now = System.currentTimeMillis();


        double token = (tokenStr==null) ? capacity: Double.parseDouble(tokenStr);
        long timestamp = (timeStampStr==null) ? now: Long.parseLong(timeStampStr);


        return true;
    }

}
