package com.dineconnect.backend.booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.dineconnect.backend.booking.dto.BookingRequest;
import com.dineconnect.backend.booking.service.BookingService;
import com.dineconnect.backend.dto.DineConnectResponse;
import com.dineconnect.backend.dto.DineConnectResponseNoData;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "User Bookings", description = "User endpoints for managing restaurant reviews")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DineConnectResponse create(@AuthenticationPrincipal UserDetails user, @RequestBody BookingRequest request) {
        return new DineConnectResponse("success", "Booking confirmed", bookingService.createBooking(user.getUsername(), request));
    }

    @GetMapping
    public DineConnectResponse getMyBookings(@AuthenticationPrincipal UserDetails user) {
        return new DineConnectResponse("success", "Bookings fetched", bookingService.getUserBookings(user.getUsername()));
    }

    @DeleteMapping("/{id}")
    public DineConnectResponseNoData cancel(@PathVariable String id, @AuthenticationPrincipal UserDetails user) {
        bookingService.cancelBooking(id, user.getUsername());
        return new DineConnectResponseNoData("success", "Booking cancelled");
    }
}
