package com.nhnacademy.booklay.server.repository.mypage;

import com.nhnacademy.booklay.server.entity.QRestockingNotification;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;

public class RestockingNotificationRepositoryImpl extends QuerydslRepositorySupport implements
    RestockingNotificationRepositoryCustom {

  public RestockingNotificationRepositoryImpl() {
    super(RestockingNotification.class);
  }

  @Override
  public Page<RestockingNotification> retrieveRegister(Long memberId, Pageable pageable) {
    QRestockingNotification restockingNotification = QRestockingNotification.restockingNotification;

    List<RestockingNotification> content = from(restockingNotification).where(
            restockingNotification.member.memberNo.eq(memberId))
        .select(restockingNotification)
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    JPQLQuery<Long> count = from(restockingNotification)
        .select(restockingNotification.count());
    return PageableExecutionUtils.getPage(content, pageable, count::fetchFirst);
  }
}
