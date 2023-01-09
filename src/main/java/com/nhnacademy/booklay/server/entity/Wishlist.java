package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wishlist {

    @EmbeddedId
    private Pk pk;

    @Column(name = "listed_at")
    private LocalDateTime listedAt;

    @Builder
    public Wishlist(Pk pk, LocalDateTime listedAt) {
        this.pk = pk;
        this.listedAt = listedAt;
    }

    @Embeddable
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pk implements Serializable {
        @Column(name = "member_no")
        private Long memberId;

        @Column(name = "product_no")
        private Long productId;
    }

}
