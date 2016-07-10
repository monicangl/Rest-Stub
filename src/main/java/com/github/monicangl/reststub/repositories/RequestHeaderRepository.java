package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.RequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RequestHeaderRepository extends JpaRepository<RequestHeader, Long> {
    Set<RequestHeader> findBySchemaId(Long id);
    void deleteBySchemaId(Long id);
}
