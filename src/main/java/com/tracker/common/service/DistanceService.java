package com.tracker.common.service;

import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class responsible for handling operations related to calculating the distance traveled by couriers.
 * It interacts with repositories to fetch courier data and location logs, and uses a strategy pattern for distance calculation.
 * Provides methods to retrieve the total distance from the database and to calculate the distance from location logs.
 */
@Service
public class DistanceService {

    @Autowired
    private CourierLocationLogRepository locationLogRepository;

    @Autowired
    private CourierRepository courierRepository;

    private final DistanceCalculationStrategy distanceCalculationStrategy;

    @Autowired
    public DistanceService(DistanceCalculationStrategy distanceCalculationStrategy) {
        this.distanceCalculationStrategy = distanceCalculationStrategy;
    }

    public double getTotalDistanceFromDB(Long courierId) {
        Optional<Courier> courier = courierRepository.findById(courierId);
        if (courier.isEmpty()) {
            throw new ResourceNotFoundException("courier not found");
        }
        if (!courier.get().getLocationLogs().isEmpty()) {
            return courier.get().getTotalDistance();
        }
        return 0.0;
    }

    public double calculateTotalDistance(Long courierId) {
        List<CourierLocationLog> locationLogs = locationLogRepository.findByCourierIdOrderByTimestampAsc(courierId);
        double totalDistance = 0.0;

        for (int i = 1; i < locationLogs.size(); i++) {
            CourierLocationLog prev = locationLogs.get(i - 1);
            CourierLocationLog current = locationLogs.get(i);
            totalDistance += distanceCalculationStrategy.calculateDistance(prev.getLat(), prev.getLng(), current.getLat(), current.getLng());
        }

        return totalDistance;
    }
}
