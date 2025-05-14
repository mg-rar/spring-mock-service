package com.example.mock.controller;

import com.example.mock.dbservice.DataBaseWorker;
import com.example.mock.fileservice.FileWorker;
import com.example.mock.model.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Random;

@RestController
public class ServiceController {
    @Autowired
    private DataBaseWorker dataBaseWorker;
    @Autowired
    private FileWorker fileWorker;

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
    public ResponseEntity<User> getMethod(@RequestParam String login) {
        delay();
        try {
            User user = dataBaseWorker.selectUserByLogin(login);
            fileWorker.writeUserToFile(user);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User not found", e);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error", e);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while writing the file", e);
        }
    }


    @GetMapping("/random")
    public ResponseEntity<String> getRandomUser() {
        try {
            return new ResponseEntity<>(fileWorker.readRandomLineFromFile(), HttpStatus.OK);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while reading the file", e);
        }
    }


    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postMethod(@Valid @RequestBody User user) {
        delay();

        try {
            user.setDate(new Date(System.currentTimeMillis()));
            int rowsAffected = dataBaseWorker.insertUser(user);
            if (rowsAffected > 0) {
                return new ResponseEntity<>("User " + user.getLogin() + " created successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("User " + user.getLogin() + " creation failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.badRequest().body("Invalid JSON format in request body: " + e.getMessage());
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Database error during user creation: " + e.getMessage(), e);
        }
    }
}
