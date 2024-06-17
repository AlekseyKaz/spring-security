package com.dhabits.ss.demo.domain.service.impl;

import com.dhabits.ss.demo.data.entity.UserEntity;
import com.dhabits.ss.demo.domain.exception.UserException;
import com.dhabits.ss.demo.data.repository.UserRepository;
import com.dhabits.ss.demo.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserEntity findByLogin(String login) {
        return userRepository.findByLogin(login).orElseThrow(() -> new UserException(String.format("user %s not found", login)));
    }

    @Override
    @Transactional
    public boolean updateUser(UserEntity user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
