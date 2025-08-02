package com.microhabit.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "http://localhost:3001")
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from MicroHabit Backend!";
    }
}
