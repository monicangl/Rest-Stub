package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.RequestParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestParameterRepository extends JpaRepository<RequestParameter, Long> {
}

