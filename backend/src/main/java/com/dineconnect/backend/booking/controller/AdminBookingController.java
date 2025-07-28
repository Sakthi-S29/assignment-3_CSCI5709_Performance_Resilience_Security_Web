package com.dineconnect.backend.booking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dineconnect.backend.booking.dto.BookingResponse;
import com.dineconnect.backend.booking.service.BookingService;
import com.dineconnect.backend.dto.DineConnectResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
@Tag(name = "Admin Booking Management", description = "Admin endpoints for managing restaurant reviews")
public class AdminBookingController {

    private final BookingService bookingService;

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "Get all bookings for a restaurant", description = "View all bookings for a specific restaurant by admin")
    public DineConnectResponse getBookingsForRestaurant(
            @PathVariable String restaurantId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<BookingResponse> bookings = bookingService.getRestaurantBookings(restaurantId, date);

        return new DineConnectResponse("success", "Bookings fetched", bookings);
    }
}

