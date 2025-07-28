package com.dineconnect.backend.review.service;

import com.dineconnect.backend.restaurant.service.RestaurantServiceUtil;
import com.dineconnect.backend.review.dto.ReviewRequest;
import com.dineconnect.backend.review.dto.ReviewResponse;
import com.dineconnect.backend.review.model.OverallReview;
import com.dineconnect.backend.review.model.Review;
import com.dineconnect.backend.review.repository.ReviewRepository;
import com.dineconnect.backend.user.service.CustomUserDetailsServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
        import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private CustomUserDetailsServiceImpl userDetailsService;

    @Mock
    private RestaurantServiceUtil restaurantServiceUtil;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final String restaurantId = "rest123";
    private final ReviewRequest reviewRequest = new ReviewRequest("Title", "Good food", 4.5);

    private final Review review = Review.builder()
            .restaurantId(restaurantId)
            .title("Title")
            .content("Good food")
            .rating(4.5)
            .reviewerName("john")
            .reviewedAt(LocalDateTime.now())
            .build();

    @Test
    void getReviewsByRestaurantId_shouldReturnReviewList() {
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(Optional.of(List.of(review)));

        List<ReviewResponse> result = reviewService.getReviewsByRestaurantId(restaurantId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).title()).isEqualTo("Title");
        verify(restaurantServiceUtil).checkIfRestaurantExists(restaurantId);
    }

    @Test
    void getReviewsByRestaurantId_shouldThrowIfNoReviews() {
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewsByRestaurantId(restaurantId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found for restaurant");

        verify(restaurantServiceUtil).checkIfRestaurantExists(restaurantId);
    }

    @Test
    void addReview_shouldSaveAndReturnReview() {
        when(userDetailsService.getCurrentUsername()).thenReturn("john");
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        ReviewResponse response = reviewService.addReview(restaurantId, reviewRequest);

        assertThat(response.title()).isEqualTo("Title");
        verify(restaurantServiceUtil).checkIfRestaurantExists(restaurantId);
        verify(reviewRepository).save(any(Review.class));
    }

    @Test
    void convertToReview_shouldMapFieldsCorrectly() {
        when(userDetailsService.getCurrentUsername()).thenReturn("john");

        Review result = reviewService.convertToReview(restaurantId, reviewRequest);

        assertThat(result.getRestaurantId()).isEqualTo(restaurantId);
        assertThat(result.getReviewerName()).isEqualTo("john");
        assertThat(result.getTitle()).isEqualTo("Title");
        assertThat(result.getRating()).isEqualTo(4.5);
    }

    @Test
    void getOverallReview_shouldReturnCorrectAverage() {
        Review review2 = Review.builder()
                .restaurantId(restaurantId)
                .title("Another")
                .content("Nice")
                .rating(3.5)
                .reviewerName("amy")
                .reviewedAt(LocalDateTime.now())
                .build();

        when(reviewRepository.findByRestaurantId(restaurantId))
                .thenReturn(Optional.of(List.of(review, review2)));

        OverallReview result = reviewService.getOverallReview(restaurantId);

        assertThat(result.overallRating()).isEqualTo(4.0);
        assertThat(result.href()).isEqualTo("/api/restaurants/rest123/reviews");
    }

    @Test
    void getOverallReview_shouldThrowIfNoReviews() {
        when(reviewRepository.findByRestaurantId(restaurantId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getOverallReview(restaurantId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("No reviews found");
    }
}
