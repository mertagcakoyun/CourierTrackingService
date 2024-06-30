package com.tracker.common.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HaversineDistanceCalculationTest {

    private final HaversineDistanceCalculation haversineDistanceCalculation = new HaversineDistanceCalculation();

    @Test
    public void testCalculateDistance_SamePoint() {
        double lat = 41.001;
        double lon = 29.002;

        double distance = haversineDistanceCalculation.calculateDistance(lat, lon, lat, lon);

        assertEquals(0.0, distance, 0.001);
    }

    @Test
    public void testCalculateDistance_DifferentPoints() {
        double lat1 = 41.001;
        double lon1 = 29.002;
        double lat2 = 42.001;
        double lon2 = 30.002;

        double distance = haversineDistanceCalculation.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance > 0);
    }

    @Test
    public void testCalculateDistance_KnownValues() {
        double lat1 = 39.9334; // Lat of Ankara
        double lon1 = 32.8597; // Lng of Ankara
        double lat2 = 41.0082; // Lat of Istanbul
        double lon2 = 28.9784; // Lng of Istanbul

        double expectedDistance = 351000; // Known distance in meters (approximately 351 km)
        double tolerance = 10000; // 10 km tolerance

        double distance = haversineDistanceCalculation.calculateDistance(lat1, lon1, lat2, lon2);

        assertEquals(expectedDistance, distance, tolerance);
    }


    @Test
    public void testCalculateDistance_ZeroLatDifference() {
        double lat1 = 41.001;
        double lon1 = 29.002;
        double lat2 = 41.001;
        double lon2 = 30.002;

        double distance = haversineDistanceCalculation.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance > 0);
    }

    @Test
    public void testCalculateDistance_ZeroLonDifference() {
        double lat1 = 41.001;
        double lon1 = 29.002;
        double lat2 = 42.001;
        double lon2 = 29.002;

        double distance = haversineDistanceCalculation.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(distance > 0);
    }
}
