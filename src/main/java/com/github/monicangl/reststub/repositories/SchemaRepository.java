package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;

import java.util.Set;

public interface SchemaRepository extends JpaRepository<Schema, Long> {
    Set<Schema> findByMethodAndContextPathIgnoringCaseAndRequestBody(HttpMethod method, String contextPath, String requestBody);
}