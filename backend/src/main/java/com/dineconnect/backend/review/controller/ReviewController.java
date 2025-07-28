package com.dineconnect.backend.review.controller;

import com.dineconnect.backend.dto.DineConnectResponse;
import com.dineconnect.backend.review.dto.ReviewRequest;
import com.dineconnect.backend.review.service.ReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for managing restaurant reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Add review to restaurant", description = "Create a review for a specific restaurant", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.CREATED)
    public DineConnectResponse addReview(
            @PathVariable String restaurantId,
            @RequestBody  @Valid ReviewRequest review) {
        return new DineConnectResponse(
                "success",
                "Review added successfully",
                reviewService.addReview(restaurantId, review)
        );
    }

    @GetMapping
    @Operation(summary = "Get reviews for a restaurant", description = "Fetches all reviews for the given restaurant", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.OK)
    public DineConnectResponse getReviews(@PathVariable String restaurantId) {
        return new DineConnectResponse(
                "success",
                "Reviews retrieved successfully",
                reviewService.getReviewsByRestaurantId(restaurantId)
        );
    }
}
