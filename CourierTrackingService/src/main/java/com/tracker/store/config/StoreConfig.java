package com.tracker.store.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
class StoreDistanceConfig {

    @Value("${store.distance.radius}")
    private Double storeDistanceRadius;
}
