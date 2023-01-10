package com.nhnacademy.booklay.server.exception.member;

import com.nhnacademy.booklay.server.dto.category.request.CategoryCURequest;
import com.nhnacademy.booklay.server.dto.member.MemberCreateRequest;

public class CreateMemberFailedException extends RuntimeException {
    public CreateMemberFailedException(String memberId) {
        super("Failed to Create Member \n"
            + "   ID : " + memberId);
    }
}
