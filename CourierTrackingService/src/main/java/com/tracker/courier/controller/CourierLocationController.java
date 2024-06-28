package com.tracker.courier.controller;

import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.store.service.StoreEntranceService;
import com.tracker.store.repository.StoreRepository;
import com.tracker.store.entity.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/courier-location")
public class CourierLocationController {

    @Autowired
    private CourierLocationLogRepository locationLogRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreEntranceService storeEntranceService;

    @GetMapping
    public List<CourierLocationLog> getAllLocations() {
        return locationLogRepository.findAll();
    }

    @PostMapping
    public CourierLocationLog logLocation(@RequestBody CourierLocationLog locationLog) {
        locationLog.setTimestamp(LocalDateTime.now());
        CourierLocationLog savedLocationLog = locationLogRepository.save(locationLog);

        checkStoreProximity(locationLog);

        return savedLocationLog;
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

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                + Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        return distance;
    }
}
