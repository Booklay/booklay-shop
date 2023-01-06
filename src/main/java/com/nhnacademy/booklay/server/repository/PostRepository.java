package com.nhnacademy.booklay.server.repository;

import com.nhnacademy.booklay.server.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
