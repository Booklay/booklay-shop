package com.nhnacademy.booklay.server.dto.product.author.response;

import com.nhnacademy.booklay.server.dto.member.reponse.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.entity.Member;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class RetrieveAuthorResponse {

    @NotNull
    Long authorNo;

    @Setter
    MemberForAuthorResponse member;

    @NotNull
    String name;


    public RetrieveAuthorResponse(Long authorNo, String name, MemberForAuthorResponse member) {
        this.authorNo = authorNo;
        this.name = name;
        this.member = member;
    }

    public RetrieveAuthorResponse(Long authorNo, String name, Member member) {
        this.authorNo = authorNo;
        this.name = name;
        this.member = new MemberForAuthorResponse(member.getMemberNo(), member.getMemberId());
    }

    public RetrieveAuthorResponse(Long authorNo, String name) {
        this.authorNo = authorNo;
        this.name = name;
    }
}
