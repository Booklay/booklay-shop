package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String name;

    @Column(name = "zie_code")
    private String zipCode;

    @Column
    private String address;

    @Column(name = "is_default_destination")
    private Boolean isDefaultDestination;
}
