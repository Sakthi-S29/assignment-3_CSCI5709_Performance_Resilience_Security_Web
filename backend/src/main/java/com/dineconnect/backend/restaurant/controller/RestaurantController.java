package com.dineconnect.backend.restaurant.controller;


import com.dineconnect.backend.dto.DineConnectResponse;
import com.dineconnect.backend.restaurant.dto.FilterRequest;
import com.dineconnect.backend.restaurant.service.RestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurant", description = "Endpoints for managing restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping
    @Operation(summary = "Get all restaurants", description = "Retrieves a list of all restaurants", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.OK)
    public DineConnectResponse getRestaurants() {
        return new DineConnectResponse(
                "success",
                "Restaurants retrieved successfully",
                restaurantService.getAllRestaurants()
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID", description = "Retrieves a restaurant by its ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ResponseStatus(HttpStatus.OK)
    public DineConnectResponse getRestaurantById(@PathVariable String id) {
        return new DineConnectResponse(
                "success",
                "Restaurant retrieved successfully",
                restaurantService.getRestaurantById(id));
    }

    //restaurant search filter
    @PostMapping("/filter")
    @Operation(
        summary = "Filter restaurants",
        description = "Filters restaurants based on cuisine, type, price range, and keywords",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @ResponseStatus(HttpStatus.OK)
    public DineConnectResponse filterRestaurants(@RequestBody FilterRequest filter) {
        return new DineConnectResponse(
                "success",
                "Filtered restaurants retrieved successfully",
                restaurantService.filterRestaurants(filter)
        );

    }

}
