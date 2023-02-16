package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

}
