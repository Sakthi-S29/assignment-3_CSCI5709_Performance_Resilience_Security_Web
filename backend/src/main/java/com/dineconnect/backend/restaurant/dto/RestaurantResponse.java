package com.dineconnect.backend.restaurant.dto;

import com.dineconnect.backend.review.model.OverallReview;
import java.util.List;
import lombok.Builder;

@Builder
public record RestaurantResponse(
        String id,
        String name,
        String description,
        String cuisine,
        String contactNumber,
        String address,
        OverallReview overallReview,
        String href,
        List<String> keywords,
        int priceRange,
        String type,
        List<String> imageUrls,
        Double reservationCharge
) {}
