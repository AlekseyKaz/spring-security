package com.dhabits.ss.demo.data.repository;

import com.dhabits.ss.demo.data.entity.UserActionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionsRepository extends JpaRepository <UserActionsEntity, Long> {
}
