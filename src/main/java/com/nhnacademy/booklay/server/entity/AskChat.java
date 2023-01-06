package com.nhnacademy.booklay.server.entity;

import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_no")
    private Member member;

    @Column
    private String content;

    @Builder
    public AskChat(Long id, Member member, String content) {
        this.id = id;
        this.member = member;
        this.content = content;
    }
}
