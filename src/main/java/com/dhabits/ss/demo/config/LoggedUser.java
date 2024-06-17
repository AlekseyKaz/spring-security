package com.dhabits.ss.demo.config;

import com.dhabits.ss.demo.data.enums.UserRoles;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class LoggedUser {
    private final long id;
    private final String login;
    private final String refreshToken;
    private final String password;
    private final List<UserRoles> authorities;


}