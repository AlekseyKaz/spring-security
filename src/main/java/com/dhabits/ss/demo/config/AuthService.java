package com.dhabits.ss.demo.config;

import com.dhabits.ss.demo.data.entity.UserEntity;
import com.dhabits.ss.demo.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static com.dhabits.ss.demo.utils.SecurityUtils.extractUsername;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;

    private final TokenService tokenService;

    @Transactional
    public Map<String, String> authenticate() {
        String loggedUserName = extractUsername();
        UserEntity entity = userService.findByLogin(loggedUserName);
        String password = entity.getOpenPassword();

        Map<String, String> token = tokenService.createToken(loggedUserName, password);
        String refreshed = token.get("refresh_token");

        entity.setRefreshToken(refreshed);
        userService.updateUser(entity);
        String s = token.get("access_token");
        Map<String, String> response = new HashMap<>();
        response.put("token", s);
        return response;
    }

    public Map<String, String> getUpdatedToken(LoggedUser loggedUser) {
        Map<String, String> token = tokenService.refreshToken(loggedUser);
        String s = token.get("access_token");
        Map<String, String> response = new HashMap<>();
        response.put("token", s);
        return response;
    }
}
