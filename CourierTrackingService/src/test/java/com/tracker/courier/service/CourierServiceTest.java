package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.dto.CourierDto;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierRequest;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CourierServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private DistanceService distanceService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveCourier() {
        CourierRequest request = new CourierRequest();
        request.setUsername("Test Courier");

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setUsername("Test Courier");

        when(objectMapper.convertValue(request, Courier.class)).thenReturn(courier);
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);
        when(objectMapper.convertValue(courier, CourierDto.class)).thenReturn(new CourierDto());

        CourierDto result = courierService.saveCourier(request);

        assertNotNull(result);
        verify(courierRepository, times(1)).save(any(Courier.class));
    }

    @Test
    public void testGetCourierById() {
        Long courierId = 1L;
        Courier courier = new Courier();
        courier.setId(courierId);
        courier.setUsername("Test Courier");

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(objectMapper.convertValue(courier, CourierDto.class)).thenReturn(new CourierDto());

        Optional<CourierDto> result = courierService.getCourierById(courierId);

        assertTrue(result.isPresent());
        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    public void testUpdateCourier() {
        Long courierId = 1L;
        CourierRequest request = new CourierRequest();
        request.setUsername("Updated Courier");

        Courier courier = new Courier();
        courier.setId(courierId);
        courier.setUsername("Test Courier");

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));
        when(courierRepository.save(any(Courier.class))).thenReturn(courier);
        when(objectMapper.convertValue(courier, CourierDto.class)).thenReturn(new CourierDto());

        CourierDto result = courierService.updateCourier(courierId, request);

        assertNotNull(result);
        verify(courierRepository, times(1)).findById(courierId);
        verify(courierRepository, times(1)).save(any(Courier.class));
    }

    @Test
    public void testUpdateCourier_NotFound() {
        Long courierId = 1L;
        CourierRequest request = new CourierRequest();
        request.setUsername("Updated Courier");

        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courierService.updateCourier(courierId, request);
        });

        verify(courierRepository, times(1)).findById(courierId);
        verify(courierRepository, times(0)).save(any(Courier.class));
    }

    @Test
    public void testDeleteCourierById() {
        Long courierId = 1L;

        doNothing().when(courierRepository).deleteById(courierId);

        courierService.deleteCourierById(courierId);

        verify(courierRepository, times(1)).deleteById(courierId);
    }

    @Test
    public void testGetAllCouriers() {
        Courier courier1 = new Courier();
        courier1.setId(1L);
        courier1.setUsername("Courier 1");

        Courier courier2 = new Courier();
        courier2.setId(2L);
        courier2.setUsername("Courier 2");

        List<Courier> couriers = Arrays.asList(courier1, courier2);
        Page<Courier> pagedCouriers = new PageImpl<>(couriers);
        Pageable pageable = PageRequest.of(0, 10);

        when(courierRepository.findAll(pageable)).thenReturn(pagedCouriers);
        when(objectMapper.convertValue(any(Courier.class), eq(CourierDto.class))).thenReturn(new CourierDto());

        Page<CourierDto> result = courierService.getAllCouriers(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(courierRepository, times(1)).findAll(pageable);
    }
}
