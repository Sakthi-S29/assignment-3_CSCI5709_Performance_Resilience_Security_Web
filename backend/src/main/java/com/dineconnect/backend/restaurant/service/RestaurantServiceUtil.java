package com.dineconnect.backend.restaurant.service;

import org.springframework.stereotype.Service;

import com.dineconnect.backend.restaurant.exception.RestaurantNotFoundException;
import com.dineconnect.backend.restaurant.respository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceUtil {

    private final RestaurantRepository restaurantRepository;

    public void checkIfRestaurantExists(String id){
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException("Restaurant not found with id: " + id);
        }
    }

    public boolean checkIfRestaurantExists(String name, String address){
        return restaurantRepository.findByNameAndAddress(name,address).isPresent();
    }
    
}
