package com.dineconnect.backend.review.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String restaurantId;
    private String reviewerName;
    private String title;
    private String content;
    private Double rating;
    private LocalDateTime reviewedAt;
}
