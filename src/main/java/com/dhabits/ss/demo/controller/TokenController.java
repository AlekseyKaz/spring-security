package com.dhabits.ss.demo.controller;

import com.dhabits.ss.demo.config.AuthService;
import com.dhabits.ss.demo.config.LoggedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@RequestMapping("/token")
@RestController
@RequiredArgsConstructor
public class TokenController {
    private final AuthService authService;
    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> getToken() {
       return new ResponseEntity<>( authService.authenticate(), HttpStatusCode.valueOf(200));
    }
    @GetMapping("/refresh")
    public ResponseEntity<Map<String, String>> getFreshToken(@AuthenticationPrincipal LoggedUser loggedUser) {
        return new ResponseEntity<>(authService.getUpdatedToken(loggedUser), HttpStatusCode.valueOf(200));
    }

}
