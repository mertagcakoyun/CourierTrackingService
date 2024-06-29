package com.tracker.courier.controller;

import com.tracker.courier.dto.CourierDto;
import com.tracker.courier.dto.request.CourierRequest;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.service.CourierLocationService;
import com.tracker.courier.service.CourierService;
import com.tracker.courier.service.DistanceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courier")
public class CourierController {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DistanceService distanceService;

    @Autowired
    private CourierService courierService;

    // Get all Couriers with pagination
    @GetMapping
    public ResponseEntity<Page<CourierDto>> getAllCouriers(Pageable pageable) {
        Page<CourierDto> couriers = courierService.getAllCouriers(pageable);
        return ResponseEntity.ok(couriers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourierDto> getCourierById(@PathVariable Long id) {
        Optional<CourierDto> courier = courierService.getCourierById(id);
        return courier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<CourierDto> createCourier(@Valid @RequestBody CourierRequest courierRequest) {
        CourierDto savedCourier = courierService.saveCourier(courierRequest);
        return new ResponseEntity<>(savedCourier, HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourier(@PathVariable Long id) {
        courierRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/distance")
    public Double getTotalTravelDistance(@PathVariable Long id) {
        return distanceService.calculateTotalDistance(id);
    }

    @GetMapping("/{id}/distanceV2")
    public Double getTotalTravelDistanceFromDB(@PathVariable Long id) {
        return distanceService.getTotalDistanceFromDB(id);
    }
}
