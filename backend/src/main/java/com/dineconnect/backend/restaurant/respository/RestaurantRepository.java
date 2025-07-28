package com.dineconnect.backend.restaurant.respository;

import com.dineconnect.backend.restaurant.model.Restaurant;

import java.util.Optional;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {

    Optional<Restaurant> findByNameAndAddress(String name, String address);

    @Query("""
    {
      'cuisine': { $in: ?0 },
      'type': { $in: ?1 },
      'priceRange': { $lte: ?2 },
      'keywords': { $in: ?3 }
    }
    """)
    List<Restaurant> filterRestaurants(
        List<String> cuisines,
        List<String> types,
        int priceRange,
        List<String> keywords
    );
}
