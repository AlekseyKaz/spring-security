package com.dhabits.ss.demo.data.repository;

import com.dhabits.ss.demo.data.entity.UserActionsEntity;
import com.dhabits.ss.demo.data.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActionsRepository extends JpaRepository <UserActionsEntity, Long> {
    Optional<UserActionsEntity> findByUser(UserEntity user);
}
