package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "member_authority")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {

    @EmbeddedId
    private Pk pk;

    @ManyToOne
    @JoinColumn(name = "member_no")
    @MapsId("memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "authority_no")
    @MapsId("authorityId")
    private Authority authority;


    @Embeddable
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pk implements Serializable {

        @Column(name = "member_no")
        private Long memberId;

        @Column(name = "authority_no")
        private Long authorityId;
    }
}
