package com.example.mock.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class User {
    @NotBlank
    private final String login;
    @NotBlank
    private final String password;
    @Setter
    private String date;

    public User (String login, String password) {
        this.login = login;
        this.password = password;
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
