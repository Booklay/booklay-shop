package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.ObjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ObjectFile, Long> {

}
