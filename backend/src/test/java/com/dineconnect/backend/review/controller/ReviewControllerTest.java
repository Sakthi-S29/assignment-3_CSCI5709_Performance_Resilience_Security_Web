package com.dineconnect.backend.review.controller;

import com.dineconnect.backend.config.TestConfig;
import com.dineconnect.backend.review.dto.ReviewRequest;
import com.dineconnect.backend.review.dto.ReviewResponse;
import com.dineconnect.backend.review.service.ReviewService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ReviewController.class)
@Import(TestConfig.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewService reviewService;

    private ReviewController reviewController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        reviewController = new ReviewController(reviewService);
        mockMvc = MockMvcBuilders.standaloneSetup(reviewController).build();
    }

    @Test
    void testAddReview() throws Exception {
        String restaurantId = "res123";
        ReviewRequest reviewRequest = new ReviewRequest("Nice", "Good taste", 4.5);

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .id("rev001")
                .reviewerName("John Doe")
                .title("Nice")
                .content("Good taste")
                .rating(4.5)
                .reviewedAt(LocalDateTime.now())
                .build();

        when(reviewService.addReview(restaurantId, reviewRequest)).thenReturn(reviewResponse);

        mockMvc.perform(post("/api/restaurants/{restaurantId}/reviews", restaurantId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Review added successfully"))
                .andExpect(jsonPath("$.data.reviewerName").value("John Doe"))
                .andExpect(jsonPath("$.data.title").value("Nice"));
    }

    @Test
    void testGetReviews() throws Exception {
        String restaurantId = "res123";
        ReviewResponse response = ReviewResponse.builder()
                .id("rev001")
                .reviewerName("John Doe")
                .title("Nice")
                .content("Good taste")
                .rating(4.5)
                .reviewedAt(LocalDateTime.now())
                .build();

        when(reviewService.getReviewsByRestaurantId(restaurantId)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/restaurants/{restaurantId}/reviews", restaurantId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Reviews retrieved successfully"))
                .andExpect(jsonPath("$.data[0].reviewerName").value("John Doe"));
    }
}
