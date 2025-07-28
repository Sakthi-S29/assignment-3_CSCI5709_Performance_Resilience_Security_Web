package com.dineconnect.backend.booking.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dineconnect.backend.booking.model.Booking;
import com.dineconnect.backend.booking.model.BookingStatus;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByUserId(String userId);
    List<Booking> findByRestaurantId(String restaurantId);
    List<Booking> findByRestaurantIdAndBookingDate(String restaurantId, LocalDate bookingDate);
    List<Booking> findByRestaurantIdOrderByBookingDateDesc(String restaurantId);
    List<Booking> findByRestaurantIdAndBookingDateOrderByBookingTimeAsc(String restaurantId, LocalDate bookingDate);
    
    boolean existsByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
    String userId,
    String restaurantId,
    LocalDate bookingDate,
    LocalTime bookingTime,
    BookingStatus status
    );

    Booking findByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
    String userId,
    String restaurantId,
    LocalDate bookingDate,
    LocalTime bookingTime,
    BookingStatus status
    );

}

