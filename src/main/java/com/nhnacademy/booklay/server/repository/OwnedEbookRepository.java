package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.OwnedEbook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnedEbookRepository extends JpaRepository<OwnedEbook, Long> {
}
