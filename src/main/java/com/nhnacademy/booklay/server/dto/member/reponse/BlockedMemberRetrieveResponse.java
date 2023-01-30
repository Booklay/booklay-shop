package com.nhnacademy.booklay.server.dto.member.reponse;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockedMemberRetrieveResponse {
    private Long id;
    private Long memberNo;
    private String memberId;
    private String name;
    private String reason;
    private LocalDateTime blockedAt;
    private LocalDateTime releasedAt;
}
