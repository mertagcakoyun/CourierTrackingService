package com.tracker.common.service;

import org.springframework.stereotype.Service;

/**
 * Service class implementing DistanceCalculationStrategy to calculate distance using the Haversine formula.
 * The Haversine formula provides a way to calculate the distance between two points on the Earth given their latitude and longitude.
 * This implementation calculates the distance in meters.
 */
@Service
public class HaversineDistanceCalculation implements DistanceCalculationStrategy {

    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c * 1000;
    }
}
