package com.dhabits.ss.demo.service;

import com.dhabits.ss.demo.domain.dto.UserRequestDto;
import com.dhabits.ss.demo.domain.dto.UserResponseDto;

public interface UserService {
    UserResponseDto create(UserRequestDto request);
}
