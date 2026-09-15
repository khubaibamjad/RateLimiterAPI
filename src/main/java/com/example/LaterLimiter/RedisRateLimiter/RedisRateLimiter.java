package com.example.LaterLimiter.RedisRateLimiter;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimiter {

    private final StringRedisTemplate redisTemplate;

    private final long capacity = 10;
    private final double refillRate = 2.0;

    public RedisRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean allow(String clientId)
    {
        String key = "ratelimit:" + clientId;

        String tokenStr = (String) redisTemplate.opsForHash().get(key, "tokens");
        String timeStampStr = (String) redisTemplate.opsForHash().get(key,"timestamp");


        long now = System.currentTimeMillis();


        double token = (tokenStr==null) ? capacity: Double.parseDouble(tokenStr);
        long timestamp = (timeStampStr==null) ? now: Long.parseLong(timeStampStr);


        double elapsedTime = (now - timestamp)/1000.0;
        double refillAmount = elapsedTime*refillRate;
        double newTokenCount = Math.min(capacity, token +refillAmount);

        boolean allowed;

        if(newTokenCount>=1)
        {
            newTokenCount = newTokenCount -1;
            allowed = true;
        }
        else
        {
            allowed = false;
        }
        redisTemplate.opsForHash().put(key, "tokens",String.valueOf(newTokenCount));
        redisTemplate.opsForHash().put(key,"timestamp", String.valueOf(now));

        return allowed;
    }

}
