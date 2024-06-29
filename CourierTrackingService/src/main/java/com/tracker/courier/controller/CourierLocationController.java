package com.tracker.courier.controller;

import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.courier.service.CourierLocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier-location")
public class CourierLocationController {

    @Autowired
    private CourierLocationLogRepository locationLogRepository;

    @Autowired
    private CourierLocationService courierLocationService;

    @GetMapping
    public List<CourierLocationLog> getAllLocations() {
        return locationLogRepository.findAll();
    } // todo pageable yapalım

    @PostMapping
    public CourierLocationLog logLocation( @Valid @RequestBody CourierLocationLogRequest request) {
        return courierLocationService.logLocation(request);
    }

}

