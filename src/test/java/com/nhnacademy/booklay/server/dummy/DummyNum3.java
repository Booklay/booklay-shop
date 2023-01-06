package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.OwnedEbook;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import org.springframework.test.util.ReflectionTestUtils;

public class DummyNum3 {
    public static RestockingNotification getDummyRestockingNotification() {


        RestockingNotification restockingNotification = RestockingNotification.builder()
            .product(null)
            .member(null)
            .build();

        ReflectionTestUtils.setField(restockingNotification, "restockingNotificationId", new RestockingNotification.Pk(null, null));

        return restockingNotification;
    }

    public static OwnedEbook getDummyOwnedEbook() {
        OwnedEbook ownedEbook = OwnedEbook.builder()
            .product(null)
            .member(null)
            .build();

        ReflectionTestUtils.setField(ownedEbook, "ownedEbookId", 1L);

        return ownedEbook;
    }
}
