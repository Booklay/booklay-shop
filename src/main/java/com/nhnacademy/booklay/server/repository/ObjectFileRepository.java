package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.dto.stroage.response.ObjectFileResponse;
import com.nhnacademy.booklay.server.entity.ObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObjectFileRepository extends JpaRepository<ObjectFile, Long> {
    ObjectFileResponse queryById(Long id);
}
