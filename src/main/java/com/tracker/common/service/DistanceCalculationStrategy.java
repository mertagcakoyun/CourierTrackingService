package com.tracker.common.service;

/**
 * Interface for defining a strategy to calculate the distance between two geographical points.
 * Provides a method to calculate the distance given the latitude and longitude of two points.
 */
public interface DistanceCalculationStrategy {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
}
