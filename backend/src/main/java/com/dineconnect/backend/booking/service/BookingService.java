package com.dineconnect.backend.booking.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dineconnect.backend.booking.dto.BookingRequest;
import com.dineconnect.backend.booking.dto.BookingResponse;
import com.dineconnect.backend.booking.exception.BookingAlreadyCancelledException;
import com.dineconnect.backend.booking.exception.BookingAlreadyExistsException;
import com.dineconnect.backend.booking.exception.BookingNotFoundException;
import com.dineconnect.backend.booking.model.Booking;
import com.dineconnect.backend.booking.model.BookingStatus;
import com.dineconnect.backend.booking.repository.BookingRepository;
import com.dineconnect.backend.restaurant.service.RestaurantService;
import com.dineconnect.backend.restaurant.service.RestaurantServiceUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RestaurantService restaurantService;
    private final RestaurantServiceUtil restaurantServiceUtil;

    public BookingResponse createBooking(String userId, BookingRequest request) {
        Booking booking = Booking.builder()
                .restaurantId(request.restaurantId())
                .userId(userId)
                .bookingDate(request.bookingDate())
                .bookingTime(request.bookingTime())
                .numberOfGuests(request.numberOfGuests())
                .status(BookingStatus.CONFIRMED)
                .build();

        // Check if a cancelled booking already exists for the same slot
        Booking existing = bookingRepository.findByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
            userId,
            request.restaurantId(),
            request.bookingDate(),
            request.bookingTime(),
            BookingStatus.CANCELLED
        );

        if (existing != null) {
            // Reactivate the cancelled booking
            existing.setStatus(BookingStatus.CONFIRMED);
            existing.setNumberOfGuests(request.numberOfGuests());
            Booking reactivated = bookingRepository.save(existing);

            String restaurantName = restaurantService.getRestaurantById(request.restaurantId()).name();
            return mapToResponse(reactivated, restaurantName);
        }

        boolean confirmedBookingExists = bookingRepository.existsByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
            userId,
            request.restaurantId(),
            request.bookingDate(),
            request.bookingTime(),
            BookingStatus.CONFIRMED
        );
            
        if (confirmedBookingExists) {
            throw new BookingAlreadyExistsException("You already have a confirmed booking at this time.");
        }
            
        
        booking = bookingRepository.save(booking);
        String restaurantName = restaurantService.getRestaurantById(request.restaurantId()).name();

        return mapToResponse(booking, restaurantName);
    }

    @Cacheable(value = "userBookings", key = "#userId")
    public List<BookingResponse> getUserBookings(String userId) {
        List<Booking> bookings = bookingRepository.findByUserId(userId);
        
        if (bookings.isEmpty()) {
            throw new BookingNotFoundException("No bookings found for user: " + userId);
        }
    
        return bookings.stream()
                .map(b -> mapToResponse(b, ""))
                .collect(Collectors.toList());
    }

    @CachePut(value = "userBookings", key = "#userId")
    public void cancelBooking(String bookingId, String userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking with ID " + bookingId + " not found"));
        
        if (!booking.getUserId().equals(userId)) {
            throw new SecurityException("Unauthorized to cancel this booking");
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingAlreadyCancelledException("This booking has already been cancelled.");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    private BookingResponse mapToResponse(Booking booking, String restaurantName) {
        return BookingResponse.builder()
                .id(booking.getId())
                .restaurantId(booking.getRestaurantId())
                .restaurantName(restaurantName)
                .bookingDate(booking.getBookingDate())
                .bookingTime(booking.getBookingTime())
                .numberOfGuests(booking.getNumberOfGuests())
                .status(booking.getStatus())
                .build();
    }

    @Cacheable(value = "restaurantBookings", key = "#restaurantId + #date")
    public List<BookingResponse> getRestaurantBookings(String restaurantId, LocalDate date) {
        return date != null
                ? getBookingsByRestaurantAndDate(restaurantId, date)
                : getBookingsByRestaurant(restaurantId);
    }
    
    public List<BookingResponse> getBookingsByRestaurant(String restaurantId) {
        restaurantServiceUtil.checkIfRestaurantExists(restaurantId);
        return bookingRepository.findByRestaurantIdOrderByBookingDateDesc(restaurantId)
                .stream()
                .map(b -> mapToResponse(b, ""))
                .toList();
    }

    
    public List<BookingResponse> getBookingsByRestaurantAndDate(String restaurantId, LocalDate date) {
        return bookingRepository.findByRestaurantIdAndBookingDateOrderByBookingTimeAsc(restaurantId, date)
                .stream()
                .map(b -> mapToResponse(b, ""))
                .toList();
    }    

}
