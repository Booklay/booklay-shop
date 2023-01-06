package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {
}
