package com.tracker.common.config;


import com.tracker.common.service.DistanceCalculationStrategy;
import com.tracker.common.service.HaversineDistanceCalculation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for defining beans in the Spring application context.
 * Provides a bean definition for the DistanceCalculationStrategy, specifically using the HaversineDistanceCalculation implementation.
 * This setup allows for easy swapping of different distance calculation strategies if needed.
 */
@Configuration
public class AppConfig {

    @Bean
    public DistanceCalculationStrategy distanceCalculationStrategy() {
        return new HaversineDistanceCalculation();
        // return new EuclideanStrategy(); ....
    }
}