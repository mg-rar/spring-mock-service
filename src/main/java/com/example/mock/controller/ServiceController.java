package com.example.mock.controller;

import com.example.mock.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ServiceController {
    private final Random random = new Random();

    private void delay() {
        try {
            long delay = (long)1000 + random.nextInt(1001);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<String> getMethod() {
        delay();
        return new ResponseEntity<>("{\"login\":\"Login1\",\"status\":\"ok\"}", HttpStatus.OK);
    }

    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> postMethod(@Valid @RequestBody User request) {
        delay();

        User user = new User(request.getLogin(), request.getPassword());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
