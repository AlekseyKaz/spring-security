package com.dhabits.ss.demo.domain.mapper;

import com.dhabits.ss.demo.domain.dto.UserRequestDto;
import com.dhabits.ss.demo.domain.dto.UserResponseDto;
import com.dhabits.ss.demo.domain.entity.UserEntity;
import com.dhabits.ss.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;

    public UserEntity toEntity(UserRequestDto requestDto) {
     return UserEntity.builder()
             .login(requestDto.getLogin())
             .password(passwordEncoder.encode(requestDto.getPassword()))
             .build();
 }
 public UserResponseDto toDto(UserEntity user) {
     return new UserResponseDto(user.getLogin());
 }
}
