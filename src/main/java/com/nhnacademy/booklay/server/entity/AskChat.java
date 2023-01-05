package com.nhnacademy.booklay.server.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "ask_chat")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AskChat {

    @Id
    @Column(name = "ask_chat_no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String content;
}
