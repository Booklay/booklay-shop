package com.nhnacademy.booklay.server.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

    @Getter
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
