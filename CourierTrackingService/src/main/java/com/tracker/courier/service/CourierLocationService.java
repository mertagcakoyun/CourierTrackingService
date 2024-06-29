package com.tracker.courier.service;

import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.store.entity.Store;
import com.tracker.store.repository.StoreRepository;
import com.tracker.store.service.StoreEntranceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CourierLocationService {

    @Autowired
    private CourierLocationLogRepository locationLogRepository;

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreEntranceService storeEntranceService;

    public List<CourierLocationLog> getAllLocations() {
        return locationLogRepository.findAll();
    }

    public CourierLocationLog logLocation(CourierLocationLogRequest locationLogRequest) {
        Optional<Courier> optionalCourier = courierRepository.findById(locationLogRequest.getCourierId());
        if (optionalCourier.isPresent()) {
            Courier courier = optionalCourier.get();
            CourierLocationLog locationLog = CourierLocationLog.builder()
                    .courier(courier)
                    .lat(locationLogRequest.getLat())
                    .lng(locationLogRequest.getLng())
                    .timestamp(LocalDateTime.now())
                    .build();

            List<CourierLocationLog> logs = courier.getLocationLogs();
            if (!logs.isEmpty()) {
                CourierLocationLog lastLog = logs.get(logs.size() - 1);
                double distance = calculateDistance(lastLog.getLat(), lastLog.getLng(), locationLog.getLat(), locationLog.getLng());
                courier.setTotalDistance(courier.getTotalDistance() + distance);
                courierRepository.save(courier);
            }

            CourierLocationLog savedLocationLog = locationLogRepository.save(locationLog);
            checkStoreProximity(locationLog);
            return savedLocationLog;
        } else {
            throw new RuntimeException("Courier not found");
        }
    }

    private void checkStoreProximity(CourierLocationLog locationLog) {
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            double distance = calculateDistance(locationLog.getLat(), locationLog.getLng(), store.getLat(), store.getLng());
            if (distance <= 100 && !storeEntranceService.isReEntry(locationLog.getCourier().getId(), store.getId(), locationLog.getTimestamp())) {
                storeEntranceService.logEntrance(locationLog.getCourier(), store, locationLog.getLat(), locationLog.getLng());
            }
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) { // todo buraya dikkat et çoklama boşa
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
