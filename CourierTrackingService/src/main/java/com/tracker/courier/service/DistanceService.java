package com.tracker.courier.service;

import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistanceService {

    @Autowired
    private CourierLocationLogRepository locationLogRepository;

    public double calculateTotalDistance(Long courierId) {
        List<CourierLocationLog> locationLogs = locationLogRepository.findByCourierIdOrderByTimestampAsc(courierId);
        double totalDistance = 0.0;

        for (int i = 1; i < locationLogs.size(); i++) {
            CourierLocationLog prev = locationLogs.get(i - 1);
            CourierLocationLog current = locationLogs.get(i);
            totalDistance += calculateDistance(prev.getLat(), prev.getLng(), current.getLat(), current.getLng());
        }

        return totalDistance;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Haversine formula to calculate the distance between two points on the Earth's surface
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }
}
