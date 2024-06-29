package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.courier.dto.CourierDto;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierRequest;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
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
    private ObjectMapper objectMapper;

    public CourierDto saveCourier(CourierRequest courierRequest) {
        Courier courier = objectMapper.convertValue(courierRequest, Courier.class);
        courier.setTotalDistance(0.0);
        Courier savedCourier = courierRepository.save(courier);
        return convertToDto(savedCourier);
    }

    public Optional<CourierDto> getCourierById(Long id) {
        return courierRepository.findById(id).map(this::convertToDto);
    }

    public CourierDto updateCourier(Long id, CourierRequest courierRequest) {
        Optional<Courier> optionalCourier = courierRepository.findById(id);
        if (optionalCourier.isPresent()) {
            Courier courier = optionalCourier.get();
            courier.setUsername(courierRequest.getUsername());
            Courier savedCourier = courierRepository.save(courier);
            return convertToDto(savedCourier);
        } else {
            throw new RuntimeException("Courier not found");
        }
    }

    public void deleteCourierById(Long id) {
        courierRepository.deleteById(id);
    }

    public Page<CourierDto> getAllCouriers(Pageable pageable) {
        return courierRepository.findAll(pageable).map(this::convertToDto); // todo stackoverflow exception
    }

    // Convert Courier entity to CourierDto
    private CourierDto convertToDto(Courier courier) {
        CourierDto courierDto = objectMapper.convertValue(courier, CourierDto.class);
        List<CourierLocationLogDto> locationLogs = courier.getLocationLogs().stream()
                .map(this::convertToLocationLogDto)
                .collect(Collectors.toList());
        courierDto.setLocationLogs(locationLogs);
        return courierDto;
    }

    // Convert CourierLocationLog entity to CourierLocationLogDto
    private CourierLocationLogDto convertToLocationLogDto(CourierLocationLog locationLog) {
        CourierLocationLogDto locationLogDto = objectMapper.convertValue(locationLog, CourierLocationLogDto.class);
        locationLogDto.setCourierId(locationLog.getCourier().getId());
        return locationLogDto;
    }
}
