
package com.dineconnect.backend.config;
import com.dineconnect.backend.review.service.ReviewService;
import com.dineconnect.backend.security.filter.JwtFilter;
import com.dineconnect.backend.security.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.mockito.Mockito;

@Configuration
public class TestConfig {

    @Bean
    public ReviewService reviewService() {
        return Mockito.mock(ReviewService.class);
    }

    @Bean
    public JwtService jwtService() {
        return Mockito.mock(JwtService.class);
    }

    @Bean
    public JwtFilter jwtFilter(JwtService jwtService) {
        return Mockito.mock(JwtFilter.class);
    }
}
