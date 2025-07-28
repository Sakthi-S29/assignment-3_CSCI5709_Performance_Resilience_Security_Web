package com.dineconnect.backend.restaurant.model;

//Geo-Search Integration: Customers can discover nearby restaurants using Google Maps API
//with filtering options (e.g., cuisine, availability, ratings).
//        • Booking System: Real-time calendar interface showing table availability, enabling users to
//        book tables instantly.
//        • Review System: Users can post reviews and rate restaurants based on their dining experience.
//        • Photo Uploads: Restaurant owners can upload images of their venue and dishes to enhance
//their listings.
//        • Owner Dashboard: Admin panel for restaurant owners to manage reservations, edit restaurant
//details, and monitor customer feedback.
//        • Notifications: Automated messages confirming reservations and notifying users of any changes

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;



@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@CompoundIndexes({
    @CompoundIndex(name = "cuisine_type_idx", def = "{'cuisine': 1, 'type': 1}"),
    @CompoundIndex(name = "keyword_price_idx", def = "{'keywords': 1, 'priceRange': 1}")
})
public class Restaurant {
    @Id
    private String id;
    @Indexed
    private String name;
    private String description;
    @Indexed
    private String cuisine;
    private String contactNumber;
    private String address;

    @Indexed
    private String type;
    private int priceRange;
    @Indexed
    private List<String> keywords;
    //filter by price range, type, and keywords
    private List<String> imageUrls;
    private Double reservationCharge;
}

