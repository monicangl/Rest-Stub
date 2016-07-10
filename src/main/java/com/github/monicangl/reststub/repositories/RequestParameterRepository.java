package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.RequestParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RequestParameterRepository extends JpaRepository<RequestParameter, Long> {
    Set<RequestParameter> findBySchemaId(Long id);
    void deleteBySchemaId(Long id);
}

