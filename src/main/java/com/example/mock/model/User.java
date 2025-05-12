package com.example.mock.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Getter
@Setter
public class User {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    private Date date;

    public User (String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }
}
