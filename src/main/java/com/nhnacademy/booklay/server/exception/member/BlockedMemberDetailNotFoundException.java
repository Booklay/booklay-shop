package com.nhnacademy.booklay.server.exception.member;

public class BlockedMemberDetailNotFoundException extends RuntimeException {
    public BlockedMemberDetailNotFoundException(Long blockedMemberDetailId) {
        super("blockedMemberDetail Not Found, blockedMemberDetail Id : " + blockedMemberDetailId);
    }
}
