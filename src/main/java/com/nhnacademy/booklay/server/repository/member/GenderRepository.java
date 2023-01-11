package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.entity.Gender;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
    Optional<Gender> findByName(String name);
}
