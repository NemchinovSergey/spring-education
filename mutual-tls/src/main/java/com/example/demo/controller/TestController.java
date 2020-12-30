package com.example.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
public class TestController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/test", params = "url")
    public ResponseEntity<String> test(@RequestParam String url, HttpServletRequest request) {
        try {
            ResponseEntity<String> resp = restTemplate.exchange(
                    url,
                    HttpMethod.valueOf(request.getMethod()),
                    HttpEntity.EMPTY,
                    String.class);
            log.info("Resp: {}", resp.getBody());
            return resp;
        } catch (HttpClientErrorException e) {
            log.error("HttpClientErrorException: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(e.getStatusCode())
                    .headers(e.getResponseHeaders())
                    .body(e.getResponseBodyAsString());
        }
    }

}
