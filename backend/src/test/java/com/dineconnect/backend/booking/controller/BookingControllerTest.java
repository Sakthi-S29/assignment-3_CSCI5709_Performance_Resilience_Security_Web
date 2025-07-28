package com.dineconnect.backend.booking.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.dineconnect.backend.booking.dto.BookingRequest;
import com.dineconnect.backend.booking.dto.BookingResponse;
import com.dineconnect.backend.booking.model.BookingStatus;
import com.dineconnect.backend.booking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookingService bookingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        BookingController bookingController = new BookingController(bookingService);
        
        // Configure MockMvc with proper argument resolvers for security
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController)
                .setCustomArgumentResolvers(new AuthenticationPrincipalArgumentResolver())
                .build();
        
        objectMapper.registerModule(new JavaTimeModule());
        
        // Clear security context before each test
        SecurityContextHolder.clearContext();
    }

    private void setAuthenticatedUser(String email) {
        if (email != null) {
            // Create a simple UserDetails implementation
            UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(email)
                    .password("test")
                    .authorities("ROLE_USER")
                    .build();
            
            TestingAuthenticationToken authentication = new TestingAuthenticationToken(userDetails, null, "ROLE_USER");
            authentication.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            // For unauthenticated user, clear the context
            SecurityContextHolder.clearContext();
        }
    }

    @Test
    void testCreateBooking() throws Exception {
        setAuthenticatedUser("john@example.com");

        BookingRequest request = new BookingRequest("rest123", LocalDate.now(), LocalTime.of(18, 30), 4);
        BookingResponse response = BookingResponse.builder()
                .id("bk001")
                .restaurantId("rest123")
                .restaurantName("Testaurant")
                .bookingDate(request.bookingDate())
                .bookingTime(request.bookingTime())
                .numberOfGuests(4)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingService.createBooking("john@example.com", request)).thenReturn(response);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Booking confirmed"))
                .andExpect(jsonPath("$.data.restaurantId").value("rest123"));
    }

    @Test
    void testGetMyBookings() throws Exception {
        setAuthenticatedUser("john@example.com");

        BookingResponse response = BookingResponse.builder()
                .id("bk001")
                .restaurantId("rest123")
                .restaurantName("Testaurant")
                .bookingDate(LocalDate.now())
                .bookingTime(LocalTime.of(19, 0))
                .numberOfGuests(2)
                .status(BookingStatus.CONFIRMED)
                .build();

        when(bookingService.getUserBookings("john@example.com")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bookings fetched"))
                .andExpect(jsonPath("$.data[0].restaurantId").value("rest123"));
    }

    @Test
    void testCancelBooking() throws Exception {
        setAuthenticatedUser("john@example.com");

        doNothing().when(bookingService).cancelBooking("bk001", "john@example.com");

        mockMvc.perform(delete("/api/bookings/bk001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Booking cancelled"));
    }
}