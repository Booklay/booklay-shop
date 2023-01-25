package com.nhnacademy.booklay.server.exception.delivery;

public class DeliveryDestinationLimitExceededException extends RuntimeException {
    public DeliveryDestinationLimitExceededException() {
        super("Delivery destination limit exceeded : ");
    }
}
