package com.nhnacademy.booklay.server.service.member;

import com.nhnacademy.booklay.server.dto.member.MemberDto;
import com.nhnacademy.booklay.server.entity.Member;
import java.util.List;

public interface MemberService {
    MemberDto getMember(long memberId);

    List<MemberDto> getMembers();
}
