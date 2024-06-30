package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.dto.CourierDto;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierRequest;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourierService {

    @Autowired
    private CourierRepository courierRepository;

    @Autowired
    private DistanceService distanceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional()
    public CourierDto saveCourier(CourierRequest courierRequest) {
        Courier courier = objectMapper.convertValue(courierRequest, Courier.class);
        courier.setTotalDistance(0.0);
        Courier savedCourier = courierRepository.save(courier);
        return convertToDto(savedCourier);
    }

    @Transactional
    public Optional<CourierDto> getCourierById(Long id) {
        return courierRepository.findById(id).map(this::convertToDto);
    }

    @Transactional
    public CourierDto updateCourier(Long id, CourierRequest courierRequest) {
        Optional<Courier> optionalCourier = courierRepository.findById(id);
        if (optionalCourier.isPresent()) {
            Courier courier = optionalCourier.get();
            courier.setUsername(courierRequest.getUsername());
            Courier savedCourier = courierRepository.save(courier);
            return convertToDto(savedCourier);
        } else {
            throw new ResourceNotFoundException("Courier not found");
        }
    }

    @Transactional
    public void updateTotalDistanceForCourier(Courier courier) {
        double calculatedDistance = distanceService.calculateTotalDistance(courier.getId());
        courier.setTotalDistance(calculatedDistance);
        courierRepository.save(courier);
    }

    @Transactional
    public void deleteCourierById(Long id) {
        courierRepository.deleteById(id);
    }

    @Transactional
    public Page<CourierDto> getAllCouriers(Pageable pageable) {
        return courierRepository.findAll(pageable).map(this::convertToDto);
    }

    private CourierDto convertToDto(Courier courier) {
        CourierDto courierDto = objectMapper.convertValue(courier, CourierDto.class);
        List<CourierLocationLogDto> locationLogs = courier.getLocationLogs().stream()
                .map(this::convertToLocationLogDto)
                .collect(Collectors.toList());
        courierDto.setLocationLogs(locationLogs);
        return courierDto;
    }


     CourierLocationLogDto convertToLocationLogDto(CourierLocationLog locationLog) {
        CourierLocationLogDto locationLogDto = objectMapper.convertValue(locationLog, CourierLocationLogDto.class);
        locationLogDto.setCourierId(locationLog.getCourier().getId());
        return locationLogDto;
    }
}
