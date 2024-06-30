package com.tracker.courier.scheduler;


import com.tracker.courier.entity.Courier;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Scheduler component for updating couriers' total distances periodically.
 * Uses a cron expression to schedule the task to run every hour.
 * Fetches all couriers from the repository and updates their total distances using the CourierService.
 */
@Component
public class CourierDistanceScheduler {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private CourierService courierService;

    @Scheduled(cron = "0 0 * * * ?") // set couriers total distance every hour with calculated distance
    public void updateCourierTotalDistances() {
        List<Courier> couriers = courierRepository.findAll();
        for (Courier courier : couriers) {
            courierService.updateTotalDistanceForCourier(courier);
        }
    }
}

