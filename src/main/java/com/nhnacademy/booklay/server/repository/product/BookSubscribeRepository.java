package com.nhnacademy.booklay.server.repository.product;

import com.nhnacademy.booklay.server.entity.BookSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookSubscribeRepository extends JpaRepository<BookSubscribe, BookSubscribe.Pk> {

}
