package com.dineconnect.backend.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    private String id;
    private String restaurantId;
    private String userId;
    private String customerName;
    private String customerPhone;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private int numberOfGuests;
    private BookingStatus status;
}
