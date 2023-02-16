package com.nhnacademy.booklay.server.repository.post;

import com.nhnacademy.booklay.server.entity.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
}
