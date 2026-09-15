package com.example.LaterLimiter.controllers;


import com.example.LaterLimiter.components.NaiveRateLimiter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RateLimitController {

    NaiveRateLimiter naiveRateLimiter = new NaiveRateLimiter();

    public RateLimitController(NaiveRateLimiter naiveRateLimiter)
    {
        this.naiveRateLimiter=naiveRateLimiter;
    }

    @GetMapping("/ping")
    public ResponseEntity<String> getMethod (HttpServletRequest request)
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

}
