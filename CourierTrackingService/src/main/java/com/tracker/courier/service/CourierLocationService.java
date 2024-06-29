package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.common.exception.OptimisticLockingException;
import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.store.entity.Store;
import com.tracker.store.repository.StoreRepository;
import com.tracker.store.service.StoreEntranceLogService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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
    private DistanceService distanceService;

    @Autowired
    private StoreEntranceLogService storeEntranceService;


    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public CourierLocationLogDto logLocation(CourierLocationLogRequest locationLogRequest) {
        int retryCount = 0;
        final int maxRetries = 3;
        while (retryCount < maxRetries) {
            try {
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
                        double distance = distanceService.calculateDistance(lastLog.getLat(), lastLog.getLng(), locationLog.getLat(), locationLog.getLng());
                        courier.setTotalDistance(courier.getTotalDistance() + distance);
                        courierRepository.save(courier);
                    }

                    CourierLocationLog savedLocationLog = locationLogRepository.save(locationLog);
                    checkStoreProximity(locationLog);
                    return convertToLocationLogDto(savedLocationLog);
                } else {
                    throw new ResourceNotFoundException("Courier not found");
                }
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    throw new OptimisticLockingException("Failed to update Courier after " + maxRetries + " attempts");
                }
            }
        }
        throw new OptimisticLockingException("Unexpected error while updating Courier");
    }

    private void checkStoreProximity(CourierLocationLog locationLog) {
        List<Store> stores = storeRepository.findAll();
        for (Store store : stores) {
            double distance = distanceService.calculateDistance(locationLog.getLat(), locationLog.getLng(), store.getLat(), store.getLng());
            if (distance <= 100 && !storeEntranceService.isReEntry(locationLog.getCourier().getId(), store.getId(), locationLog.getTimestamp())) {
                storeEntranceService.logEntrance(locationLog.getCourier(), store, locationLog.getLat(), locationLog.getLng());
            }
        }
    }

    CourierLocationLogDto convertToLocationLogDto(CourierLocationLog locationLog) {
        CourierLocationLogDto locationLogDto = objectMapper.convertValue(locationLog, CourierLocationLogDto.class);
        locationLogDto.setCourierId(locationLog.getCourier().getId());
        return locationLogDto;
    }
}
