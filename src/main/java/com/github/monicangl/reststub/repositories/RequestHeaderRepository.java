package com.github.monicangl.reststub.repositories;

import com.github.monicangl.reststub.models.RequestHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestHeaderRepository extends JpaRepository<RequestHeader, Long> {
}
