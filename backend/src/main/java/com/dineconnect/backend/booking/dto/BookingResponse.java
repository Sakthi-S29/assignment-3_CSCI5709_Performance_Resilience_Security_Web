package com.dineconnect.backend.booking.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.dineconnect.backend.booking.model.BookingStatus;

import lombok.Builder;

@Builder
public record BookingResponse(
    String id,
    String restaurantId,
    String restaurantName,
    LocalDate bookingDate,
    LocalTime bookingTime,
    int numberOfGuests,
    BookingStatus status
) {}

