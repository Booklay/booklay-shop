package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto getMember(Long memberId);

    List<MemberDto> getMembers();
}
