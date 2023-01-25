package com.nhnacademy.booklay.server.exception.member;

import com.nhnacademy.booklay.server.entity.MemberAuthority;

public class MemberAuthorityNotFoundException extends RuntimeException {
    public MemberAuthorityNotFoundException() {
        super("MemberAuthority Not Found");
    }
}
