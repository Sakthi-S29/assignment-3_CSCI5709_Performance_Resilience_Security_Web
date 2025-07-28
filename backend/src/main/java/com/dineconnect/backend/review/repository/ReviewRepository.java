package com.dineconnect.backend.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.dineconnect.backend.review.model.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

    public Optional<List<Review>> findByRestaurantId(String restaurantId);
    
}
