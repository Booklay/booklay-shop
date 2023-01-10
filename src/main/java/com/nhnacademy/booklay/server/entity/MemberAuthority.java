package com.nhnacademy.booklay.server.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "member_authority")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class MemberAuthority {

    @EmbeddedId
    private Pk pk;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    @MapsId("memberNo")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "authority_no")
    @MapsId("authorityId")
    private Authority authority;

    @Builder
    public MemberAuthority(Pk pk, Member member, Authority authority) {
        this.pk = pk;
        this.member = member;
        this.authority = authority;
    }

    @Embeddable
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pk implements Serializable {

        @Column(name = "member_no")
        private Long memberNo;

        @Column(name = "authority_no")
        private Long authorityId;
    }
}
