package com.dineconnect.backend.review.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ReviewRequest(
    @NotNull
    String title,
    @Length(min = 1, max = 1000, message = "Content must be between 1 and 1000 characters")
    String content,
    
    @Positive(message = "Rating must be a positive number")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    Double rating) {}
