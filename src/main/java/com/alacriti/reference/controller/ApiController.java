package com.alacriti.reference.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class ApiController {

    @GetMapping
    public String health(){
        return "Running health";
    }
}
