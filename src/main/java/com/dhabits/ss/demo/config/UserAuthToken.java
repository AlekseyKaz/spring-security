package com.dhabits.ss.demo.config;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthToken extends AbstractAuthenticationToken {
    private final LoggedUser loggedUser;
    private final String credentials;

    public UserAuthToken(LoggedUser loggedUser,
                         String credentials,
                         Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.loggedUser = loggedUser;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public LoggedUser getPrincipal() {
        return loggedUser;
    }
}
