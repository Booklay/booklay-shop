package com.nhnacademy.booklay.server.dummy;

import com.nhnacademy.booklay.server.entity.Image;
import com.nhnacademy.booklay.server.entity.OwnedEbook;
import com.nhnacademy.booklay.server.entity.Product;
import com.nhnacademy.booklay.server.entity.RestockingNotification;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class DummyNum3 {


    private static Product getDummyProductForDummy(){
        Image image = Image.builder()
            .address("")
            .ext("")
            .build();
        ReflectionTestUtils.setField(image, "id", 1L);
        Product product = Product.builder()
            .image(image)
            .price(1)
            .pointRate(1)
            .isSelling(true)
            .longDescription("")
            .pointMethod(true)
            .registedAt(LocalDateTime.now())
            .title("")
            .shortDescription("")
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        return product;
    }
    public static RestockingNotification getDummyRestockingNotification() {
        Image image = Image.builder()
            .address("")
            .ext("")
            .build();
        ReflectionTestUtils.setField(image, "id", 1L);
        Product product = Product.builder()
            .image(image)
            .price(1)
            .pointRate(1)
            .isSelling(true)
            .longDescription("")
            .pointMethod(true)
            .registedAt(LocalDateTime.now())
            .title("")
            .shortDescription("")
            .build();
        ReflectionTestUtils.setField(product, "id", 1L);
        RestockingNotification restockingNotification = RestockingNotification.builder()
            .product(product)
            .member(Dummy.getDummyMember())
            .build();

        ReflectionTestUtils.setField(restockingNotification, "pk", new RestockingNotification.Pk(null, null));

        return restockingNotification;
    }

    public static OwnedEbook getDummyOwnedEbook() {
        OwnedEbook ownedEbook = OwnedEbook.builder()
            .product(getDummyProductForDummy())
            .member(Dummy.getDummyMember())
            .build();

        ReflectionTestUtils.setField(ownedEbook, "id", 1L);

        return ownedEbook;
    }
}
