package com.example.LaterLimiter.components;

import org.springframework.stereotype.Component;
import tools.jackson.databind.ser.std.DelegatingSerializer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NaiveRateLimiter {

    ConcurrentHashMap<String, AtomicInteger> ClientIdentifier = new ConcurrentHashMap<>();

    public static boolean allow(String ClientId) {
        int limit = 5;
        int count = 0;

        count++;
       if (count <= 5)
       {
           return true;
       }
       return false;
    }
}
