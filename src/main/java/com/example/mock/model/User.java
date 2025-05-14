package com.example.mock.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @Setter
    private Date date;
}
