package com.dineconnect.backend.restaurant.dto;

import com.dineconnect.backend.review.model.OverallReview;
import java.util.List;
import lombok.Builder;

@Builder
public record RestaurantResponseWithoutHref(
        String id,
        String name,
        String description,
        String cuisine,
        String contactNumber,
        String address,
        int priceRange,
        String type,
        List<String> keywords,
        OverallReview overallReview,
        List<String> imageUrls,
        Double reservationCharge
) {}
