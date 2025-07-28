package com.dineconnect.backend.review.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.dineconnect.backend.restaurant.service.RestaurantServiceUtil;
import com.dineconnect.backend.review.dto.*;
import com.dineconnect.backend.review.model.OverallReview;
import com.dineconnect.backend.review.model.Review;
import com.dineconnect.backend.review.repository.ReviewRepository;
import com.dineconnect.backend.user.service.CustomUserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomUserDetailsServiceImpl userDetailsService;
    private final RestaurantServiceUtil restaurantServiceUtil;

    @Cacheable(value = "reviews", key = "#restaurantId")
    public List<ReviewResponse> getReviewsByRestaurantId(String restaurantId) {
        restaurantServiceUtil.checkIfRestaurantExists(restaurantId);
        return reviewRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new RuntimeException("No reviews found for restaurant with ID: " + restaurantId))
                .stream()
                .map(ReviewResponse::buildReviewResponse)
                .toList();
    }

    public ReviewResponse addReview(String restaurantId, ReviewRequest review) {
        restaurantServiceUtil.checkIfRestaurantExists(restaurantId);
        Review savedReview =  reviewRepository.save(convertToReview(restaurantId, review));
        return ReviewResponse.buildReviewResponse(savedReview);
    }

    public Review convertToReview(String restaurantId, ReviewRequest reviewRequest) {

        return Review.builder()
                .restaurantId(restaurantId)
                .title(reviewRequest.title())
                .content(reviewRequest.content())
                .rating(reviewRequest.rating())
                .reviewerName(userDetailsService.getCurrentUsername())
                .reviewedAt(java.time.LocalDateTime.now())
                .build();
    }

    @Cacheable(value = "overallReview", key = "#restaurantId")
    public OverallReview getOverallReview(String restaurantId) {
        List<Review> reviews = reviewRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new RuntimeException("No reviews found for restaurant with ID: " + restaurantId));

        double overallRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        return new OverallReview(overallRating, String.format("/api/restaurants/%s/reviews",restaurantId));
    }

    
}
