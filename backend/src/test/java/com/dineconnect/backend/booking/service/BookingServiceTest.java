package com.dineconnect.backend.booking.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.dineconnect.backend.booking.dto.BookingRequest;
import com.dineconnect.backend.booking.dto.BookingResponse;
import com.dineconnect.backend.booking.exception.BookingAlreadyCancelledException;
import com.dineconnect.backend.booking.exception.BookingAlreadyExistsException;
import com.dineconnect.backend.booking.exception.BookingNotFoundException;
import com.dineconnect.backend.booking.model.Booking;
import com.dineconnect.backend.booking.model.BookingStatus;
import com.dineconnect.backend.booking.repository.BookingRepository;
import com.dineconnect.backend.restaurant.dto.RestaurantResponseWithoutHref;
import com.dineconnect.backend.restaurant.service.RestaurantService;
import com.dineconnect.backend.restaurant.service.RestaurantServiceUtil;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private RestaurantServiceUtil restaurantServiceUtil;

    @InjectMocks
    private BookingService bookingService;

    private BookingRequest request;
    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = new BookingRequest("rest123", LocalDate.now(), LocalTime.of(18, 0), 2);
        booking = Booking.builder()
                .id("bk001")
                .restaurantId("rest123")
                .userId("user1")
                .bookingDate(request.bookingDate())
                .bookingTime(request.bookingTime())
                .numberOfGuests(2)
                .status(BookingStatus.CONFIRMED)
                .build();
    }

    @Test
    void createBooking_shouldThrowIfConfirmedBookingExists() {
        when(bookingRepository.existsByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
                any(), any(), any(), any(), eq(BookingStatus.CONFIRMED)))
            .thenReturn(true);

        assertThatThrownBy(() -> bookingService.createBooking("user1", request))
                .isInstanceOf(BookingAlreadyExistsException.class);
    }

    @Test
    void createBooking_shouldReactivateCancelledBooking() {
        Booking cancelled = new Booking(booking.getId(), booking.getRestaurantId(), "user1", null, null,
                booking.getBookingDate(), booking.getBookingTime(), 2, BookingStatus.CANCELLED);

        when(bookingRepository.findByUserIdAndRestaurantIdAndBookingDateAndBookingTimeAndStatus(
                any(), any(), any(), any(), eq(BookingStatus.CANCELLED)))
            .thenReturn(cancelled);
        when(bookingRepository.save(any())).thenReturn(booking);
        when(restaurantService.getRestaurantById("rest123"))
        .thenReturn(new RestaurantResponseWithoutHref(
            "rest123",
            "Testaurant",
            "Test description",
            "Indian",
            "1234567890",
            "123 Main St",
            2,
            "Fine Dining",
            List.of("vegan", "family"),
            null,
            List.of("img1.jpg", "img2.jpg"),
            5.0
        ));


        BookingResponse response = bookingService.createBooking("user1", request);
        assertThat(response.status()).isEqualTo(BookingStatus.CONFIRMED);
    }

    @Test
    void getUserBookings_shouldThrowIfNoneFound() {
        when(bookingRepository.findByUserId("user1")).thenReturn(List.of());

        assertThatThrownBy(() -> bookingService.getUserBookings("user1"))
                .isInstanceOf(BookingNotFoundException.class);
    }

    @Test
    void cancelBooking_shouldThrowIfAlreadyCancelled() {
        booking.setUserId("user1");
        booking.setStatus(BookingStatus.CANCELLED);
        when(bookingRepository.findById("bk001")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking("bk001", "user1"))
                .isInstanceOf(BookingAlreadyCancelledException.class);
    }

    @Test
    void cancelBooking_shouldThrowIfNotOwner() {
        booking.setUserId("user2");
        when(bookingRepository.findById("bk001")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking("bk001", "user1"))
                .isInstanceOf(SecurityException.class);
    }
}
