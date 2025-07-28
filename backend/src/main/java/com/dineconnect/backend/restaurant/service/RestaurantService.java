package com.dineconnect.backend.restaurant.service;

import com.dineconnect.backend.restaurant.dto.FilterRequest;
import com.dineconnect.backend.restaurant.dto.RestaurantRequest;
import com.dineconnect.backend.restaurant.dto.RestaurantResponse;
import com.dineconnect.backend.restaurant.dto.RestaurantResponseWithoutHref;
import com.dineconnect.backend.restaurant.dto.RestaurantResponse.RestaurantResponseBuilder;
import com.dineconnect.backend.restaurant.exception.RestaurantAlreadyExistsException;
import com.dineconnect.backend.restaurant.exception.RestaurantNotFoundException;
import com.dineconnect.backend.restaurant.model.Restaurant;
import com.dineconnect.backend.restaurant.respository.RestaurantRepository;
import com.dineconnect.backend.review.service.ReviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.dineconnect.backend.review.model.OverallReview;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReviewService reviewService;
    private final RestaurantServiceUtil restaurantServiceUtil;

    public Restaurant createRestaurant(RestaurantRequest restaurant) {
        if(restaurantServiceUtil.checkIfRestaurantExists(restaurant.name(), restaurant.address())){
            throw new RestaurantAlreadyExistsException(
                String.format("Restaurant with name: %s and address: %s , already exists.", 
                restaurant.name(), restaurant.address())
            );
        }
            
        return restaurantRepository.save(buildRestaurant(restaurant));
    }

    @Cacheable("restaurants")
    public List<RestaurantResponse> getAllRestaurants(){
        
        return restaurantRepository.findAll()
            .stream()
            .map(restaurant -> 
                            restaurantResponseBuilder(
                                restaurant, 
                                reviewService.getOverallReview(restaurant.getId())
                            )
                            .href("/api/restaurant/"+ restaurant.getId())
                            .build()
                        )
            .toList();
    }
    @Cacheable(value = "restaurant", key = "#id")
    public RestaurantResponseWithoutHref getRestaurantById(String id) {
        Restaurant restaurant = restaurantRepository.findById(id)
            .orElseThrow(() -> 
                new RestaurantNotFoundException("Restaurant not found with id: " + id));

        
        
        return new RestaurantResponseWithoutHref(
            restaurant.getId(),
            restaurant.getName(), 
            restaurant.getDescription(), 
            restaurant.getCuisine(), 
            restaurant.getContactNumber(), 
            restaurant.getAddress(),
            restaurant.getPriceRange(),
            restaurant.getType(),
            restaurant.getKeywords(), 
            reviewService.getOverallReview(id),
            restaurant.getImageUrls(),
            restaurant.getReservationCharge()
        ); 
    }
    @CacheEvict(value = "restaurant", key= "#id")
    public void deleteRestaurant(String id) {
        restaurantServiceUtil.checkIfRestaurantExists(id);
        restaurantRepository.deleteById(id);
    }

    @CachePut(value = "restaurant", key = "#id")
    public Restaurant updateRestaurant(String id, RestaurantRequest restaurantRequest){
        restaurantServiceUtil.checkIfRestaurantExists(id);
        Restaurant restaurant = buildRestaurant(restaurantRequest);
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + id));
        existingRestaurant.setAddress(restaurant.getAddress());
        existingRestaurant.setContactNumber(restaurant.getContactNumber());
        existingRestaurant.setCuisine(restaurant.getCuisine());
        existingRestaurant.setDescription(restaurant.getDescription());
        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setImageUrls(restaurant.getImageUrls());
        existingRestaurant.setReservationCharge(restaurant.getReservationCharge());
        existingRestaurant.setKeywords(restaurant.getKeywords());
        existingRestaurant.setPriceRange(restaurant.getPriceRange());
        existingRestaurant.setType(restaurant.getType());
        return restaurantRepository.save(existingRestaurant);
    }

    

    public Restaurant buildRestaurant(RestaurantRequest restaurantRequest){
        return Restaurant.builder()
        .name(restaurantRequest.name())
        .address(restaurantRequest.address())
        .description(restaurantRequest.description())
        .cuisine(restaurantRequest.cuisine())
        .contactNumber(restaurantRequest.contactNumber())
        .address(restaurantRequest.address())
        .priceRange(restaurantRequest.priceRange())
        .type(restaurantRequest.type())
        .keywords(restaurantRequest.keywords())
        .imageUrls(restaurantRequest.imageUrls())
        .reservationCharge(restaurantRequest.reservationCharge())
        .build();
    }

    public RestaurantResponseBuilder restaurantResponseBuilder(Restaurant restaurant, OverallReview overallReview){
        return restaurantResponseBuilder(restaurant)
        .overallReview(overallReview);
    }

    public RestaurantResponseBuilder restaurantResponseBuilder(Restaurant restaurant){
        return RestaurantResponse.builder()
        .id(restaurant.getId())
        .name(restaurant.getName())
        .description(restaurant.getDescription())
        .cuisine(restaurant.getCuisine())
        .contactNumber(restaurant.getContactNumber())
        .address(restaurant.getAddress())
        .priceRange(restaurant.getPriceRange())
        .type(restaurant.getType())
        .keywords(restaurant.getKeywords())
        .imageUrls(restaurant.getImageUrls())
        .reservationCharge(restaurant.getReservationCharge());
    }

    public List<RestaurantResponse> filterRestaurants(FilterRequest filter) {
        List<Restaurant> restaurants;

        boolean hasCuisine = !filter.cuisines().isEmpty();
        boolean hasType = !filter.types().isEmpty();
        boolean hasKeywords = !filter.keywords().isEmpty();
        boolean hasAnyFilter = hasCuisine || hasType || hasKeywords || filter.priceRange() < 100;

        if (!hasAnyFilter) {
            // No filters at all, return everything
            restaurants = restaurantRepository.findAll();
        } else {
            // Apply partial filter
            restaurants = restaurantRepository.findAll().stream()
                .filter(r -> !hasCuisine || filter.cuisines().contains(r.getCuisine()))
                .filter(r -> !hasType || filter.types().contains(r.getType()))
                .filter(r -> r.getPriceRange() <= filter.priceRange())
                .filter(r -> !hasKeywords || r.getKeywords().stream().anyMatch(filter.keywords()::contains))
                .toList();
        }

        return restaurants.stream()
            .map(r -> restaurantResponseBuilder(r, reviewService.getOverallReview(r.getId()))
                .href("/api/restaurant/" + r.getId())
                .build()
            )
            .toList();
    }

}
