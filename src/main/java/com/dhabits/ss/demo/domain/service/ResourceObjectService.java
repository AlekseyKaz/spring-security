package com.dhabits.ss.demo.domain.service;

import com.dhabits.ss.demo.data.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.data.repository.ResourceObjectRepository;
import com.dhabits.ss.demo.domain.dto.ResourceObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResourceObjectService {

    private final ResourceObjectRepository repository;

    public Integer save(ResourceObject resourceObject) {
        return repository.save(new ResourceObjectEntity(
                resourceObject.getId(), resourceObject.getValue(),
                resourceObject.getPath())).getId();

    }

    public ResourceObject get(int id) {
        return repository.findById(id)
                .map(r -> new ResourceObject(r.getId(), r.getValue(), r.getPath()))
                .orElse(null);
    }

}
