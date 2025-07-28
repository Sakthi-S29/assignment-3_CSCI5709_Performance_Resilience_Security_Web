package com.dineconnect.backend.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record BookingRequest(
    String restaurantId,
    LocalDate bookingDate,
    LocalTime bookingTime,
    int numberOfGuests
) {
    
}
