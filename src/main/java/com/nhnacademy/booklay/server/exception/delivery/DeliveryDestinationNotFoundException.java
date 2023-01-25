package com.nhnacademy.booklay.server.exception.delivery;

public class DeliveryDestinationNotFoundException extends RuntimeException {
    public DeliveryDestinationNotFoundException(Long addressNo) {
        super("Delivery Destination Not Found, MemberNo : " + addressNo);
    }
}
