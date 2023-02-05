package com.nhnacademy.booklay.server.repository.product.impl;

import com.nhnacademy.booklay.server.dto.member.response.MemberForAuthorResponse;
import com.nhnacademy.booklay.server.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.server.entity.Author;
import com.nhnacademy.booklay.server.entity.QAuthor;
import com.nhnacademy.booklay.server.entity.QMember;
import com.nhnacademy.booklay.server.repository.product.AuthorRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class AuthorRepositoryImpl extends QuerydslRepositorySupport
    implements AuthorRepositoryCustom {

    public AuthorRepositoryImpl() {
        super(Author.class);
    }

    @Override
    public Page<RetrieveAuthorResponse> findAllBy(Pageable pageable) {
        QAuthor author = QAuthor.author;
        QMember member = QMember.member;

        List<RetrieveAuthorResponse> content = from(author)
            .leftJoin(member).on(author.member.memberNo.eq(member.memberNo))
            .select(Projections.constructor(RetrieveAuthorResponse.class,
                                            author.authorId,
                                            author.name,
                                            Projections.constructor(MemberForAuthorResponse.class,
                                                                    member.memberNo,
                                                                    member.memberId)))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPQLQuery<Long> count = from(author)
            .select(author.count());
        return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
    }
}
