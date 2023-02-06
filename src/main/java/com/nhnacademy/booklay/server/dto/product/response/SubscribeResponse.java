package com.nhnacademy.booklay.server.dto.product.response;

import com.nhnacademy.booklay.server.entity.Subscribe;
import lombok.Getter;

@Getter
public class SubscribeResponse {
     private final Long id;

     private final int subscribeWeek;

     private final int subscribeDay;

     private final String publisher;

    public SubscribeResponse(Subscribe subscribe) {
        this.id = subscribe.getId();
        this.subscribeWeek = subscribe.getSubscribeWeek();
        this.subscribeDay = subscribe.getSubscribeDay();
        this.publisher = subscribe.getPublisher();
    }
}
