package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // Simple endpoint to validate the service is up and running
    @GetMapping("/health")
    public String healthCheck() {
        return "Service is up and running!";
    }
}
