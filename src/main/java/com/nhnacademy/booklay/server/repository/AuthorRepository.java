package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
