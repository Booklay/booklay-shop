package com.nhnacademy.booklay.server.repository.member;

import com.nhnacademy.booklay.server.entity.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthorityRepository
    extends JpaRepository<MemberAuthority, MemberAuthority.Pk> {
}
