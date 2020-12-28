package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/test")
    public void test() {
        ResponseEntity<String> resp = restTemplate.getForEntity("https://client.badssl.com/", String.class);
        log.info("Resp: {}", resp.getBody());
    }

}
