package com.tracker.courier.scheduler;


import com.tracker.courier.entity.Courier;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

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

