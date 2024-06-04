package com.dhabits.ss.demo.controller;

import com.dhabits.ss.demo.domain.dto.UserRequestDto;
import com.dhabits.ss.demo.domain.dto.UserResponseDto;
import com.dhabits.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
@PostMapping("/create")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDto request) {
    UserResponseDto userResponseDto = userService.create(request);
    return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }
}
