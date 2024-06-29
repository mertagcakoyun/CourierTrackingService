package com.tracker.courier.controller;

import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.courier.service.CourierLocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

