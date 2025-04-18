package com.example.mock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor
@Getter
public class Request {

    @NonNull
    private String login;

    @NonNull
    private String password;
}
