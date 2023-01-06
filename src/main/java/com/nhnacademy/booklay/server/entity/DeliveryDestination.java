package com.nhnacademy.booklay.server.entity;

import lombok.*;

import javax.persistence.*;

@Table(name = "delivery_destination")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryDestination {

    @Id
    @Column(name = "delivery_destination_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String name;

    @Column(name = "zip_code")
    private String zipCode;

    @Column
    private String address;

    @Column(name = "is_default_destination")
    private Boolean isDefaultDestination;

    @Builder
    public DeliveryDestination(Long id, Member member, String name, String zipCode, String address, Boolean isDefaultDestination) {
        this.id = id;
        this.member = member;
        this.name = name;
        this.zipCode = zipCode;
        this.address = address;
        this.isDefaultDestination = isDefaultDestination;
    }
}
