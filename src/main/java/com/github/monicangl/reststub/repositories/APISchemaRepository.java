package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.APISchema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface APISchemaRepository extends JpaRepository<APISchema, Long> {
    Set<APISchema> findByMethodAndContextPathIgnoringCaseAndRequestBody(String method, String contextPath, String requestBody);
}