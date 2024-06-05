package com.dhabits.ss.demo.service.impl;

import com.dhabits.ss.demo.domain.dto.UserRequestDto;
import com.dhabits.ss.demo.domain.dto.UserResponseDto;
import com.dhabits.ss.demo.domain.entity.UserEntity;
import com.dhabits.ss.demo.domain.exception.UserException;
import com.dhabits.ss.demo.domain.mapper.UserMapper;
import com.dhabits.ss.demo.repository.UserRepository;
import com.dhabits.ss.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    @Override
    public UserResponseDto create(UserRequestDto request) {
        try {
            UserEntity entity = userMapper.toEntity(request);
            UserEntity save = userRepository.save(entity);
            return userMapper.toDto(save);
        } catch (DataIntegrityViolationException ex) {
            throw new UserException(String.format("user %s already exist", request.getLogin()));
        }
    }
}
