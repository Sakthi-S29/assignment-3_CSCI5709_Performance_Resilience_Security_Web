package com.dineconnect.backend.restaurant.dto;

import java.util.List;

public record FilterRequest(
    List<String> cuisines,
    List<String> types,
    int priceRange,
    List<String> keywords
) {}
