package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.BookSubscribe;
import com.nhnacademy.booklay.server.entity.QBookSubscribe;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class BookSubscribeRepositoryImpl extends QuerydslRepositorySupport implements
    BookSubscribeRepositoryCustom {

    public BookSubscribeRepositoryImpl() {
        super(BookSubscribe.class);
    }

    @Override
    public List<Long> findBooksProductIdBySubscribeId(Long subscribeId) {
        QBookSubscribe bookSubscribe = QBookSubscribe.bookSubscribe;

        return from(bookSubscribe)
            .where(bookSubscribe.pk.subscribeId.eq(subscribeId))
            .select(bookSubscribe.productNo.id)
            .fetch();
    }
}
