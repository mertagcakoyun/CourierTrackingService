package com.tracker.courier.controller;

import com.tracker.courier.entity.Courier;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.service.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DistanceService distanceService;

    @GetMapping
    public List<Courier> getAllCouriers() {
        return courierRepository.findAll();
    }

    @PostMapping
    public Courier createCourier(@RequestBody Courier courier) {
        return courierRepository.save(courier);
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
}
