package com.dhabits.ss.demo.data.repository;

import com.dhabits.ss.demo.data.entity.ResourceObjectEntity;
import org.springframework.data.jpa.repository.*;

public interface ResourceObjectRepository extends JpaRepository<ResourceObjectEntity, Integer> {
}
