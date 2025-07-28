package com.dineconnect.backend.booking.exception;

public class BookingAlreadyCancelledException extends RuntimeException {
    public BookingAlreadyCancelledException(String message) {
        super(message);
    }
}

