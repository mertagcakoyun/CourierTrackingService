package com.tracker.courier.controller;

import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.courier.service.CourierLocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing courier location logs.
 * Provides an endpoint for logging courier locations.
 * Uses CourierLocationService to handle the business logic for logging locations.
 */
@RestController
@RequestMapping("/api/courier-location")
public class CourierLocationController {

    @Autowired
    private CourierLocationService courierLocationService;

    @PostMapping
    public CourierLocationLogDto logLocation(@Valid @RequestBody CourierLocationLogRequest request) {
        return courierLocationService.logLocation(request);
    }

}

