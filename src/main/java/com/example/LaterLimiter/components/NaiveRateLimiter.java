package com.example.LaterLimiter.components;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class NaiveRateLimiter {

    ConcurrentHashMap<String, AtomicInteger> clientCounters = new ConcurrentHashMap<>();

    public boolean allow(String ClientId) {
        AtomicInteger counter = clientCounters.computeIfAbsent(ClientId, key -> new AtomicInteger(0));
       int currentCount = counter.incrementAndGet();
       return currentCount<=5;
    }
}
