package com.dhabits.ss.demo.domain.service;

import com.dhabits.ss.demo.data.entity.UserEntity;

public interface UserService {
    UserEntity findByLogin(String login);
    boolean updateUser(UserEntity user);

}
