package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.common.exception.OptimisticLockingException;
import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.store.entity.Store;
import com.tracker.store.repository.StoreRepository;
import com.tracker.store.service.StoreEntranceLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourierLocationServiceTest {

    @Mock
    private CourierLocationLogRepository locationLogRepository;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private DistanceService distanceService;

    @Mock
    private StoreEntranceLogService storeEntranceService;

    @Mock
    private ObjectMapper objectMapper;


    @InjectMocks
    private CourierLocationService courierLocationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogLocation_successful() {
        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);
        request.setLat(40.7128);
        request.setLng(-74.0060);

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setLocationLogs(Collections.emptyList());
        courier.setTotalDistance(0);

        CourierLocationLog locationLog = CourierLocationLog.builder()
                .courier(courier)
                .lat(40.7128)
                .lng(-74.0060)
                .timestamp(LocalDateTime.now())
                .build();

        when(courierRepository.findById(request.getCourierId())).thenReturn(Optional.of(courier));
        when(locationLogRepository.save(any(CourierLocationLog.class))).thenReturn(locationLog);

        CourierLocationLogDto locationLogDto = new CourierLocationLogDto();
        locationLogDto.setCourierId(courier.getId());
        when(objectMapper.convertValue(any(CourierLocationLog.class), eq(CourierLocationLogDto.class))).thenReturn(locationLogDto);

        CourierLocationLogDto result = courierLocationService.logLocation(request);

        assertNotNull(result);
        assertEquals(courier.getId(), result.getCourierId());
        verify(courierRepository, times(1)).findById(request.getCourierId());
        verify(locationLogRepository, times(1)).save(any(CourierLocationLog.class));
    }

    @Test
    void testLogLocation_optimisticLocking() {
        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);
        request.setLat(40.7128);
        request.setLng(-74.0060);

        Courier courier = new Courier();
        courier.setId(1L);
        courier.setLocationLogs(Collections.emptyList());
        courier.setTotalDistance(0);

        when(courierRepository.findById(request.getCourierId())).thenReturn(Optional.of(courier));
        when(locationLogRepository.save(any(CourierLocationLog.class))).thenThrow(new ObjectOptimisticLockingFailureException(Courier.class, 1L));

        assertThrows(OptimisticLockingException.class, () -> courierLocationService.logLocation(request));

        verify(courierRepository, times(3)).findById(request.getCourierId());
        verify(locationLogRepository, times(3)).save(any(CourierLocationLog.class));
    }

    @Test
    void testLogLocation_resourceNotFound() {
        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);
        request.setLat(40.7128);
        request.setLng(-74.0060);

        when(courierRepository.findById(request.getCourierId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courierLocationService.logLocation(request));

        verify(courierRepository, times(1)).findById(request.getCourierId());
        verify(locationLogRepository, times(0)).save(any(CourierLocationLog.class));
    }

    @Test
    void testCheckStoreProximity_withinRange() {
        Courier courier = new Courier();
        courier.setId(1L);
        Store store = new Store();
        store.setId(1L);
        store.setLat(40.7128);
        store.setLng(-74.0060);

        CourierLocationLog locationLog = CourierLocationLog.builder()
                .courier(courier)
                .lat(40.7128)
                .lng(-74.0060)
                .timestamp(LocalDateTime.now())
                .build();

        when(storeRepository.findAll()).thenReturn(Collections.singletonList(store));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(50.0);
        when(storeEntranceService.isReEntry(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(false);

        courierLocationService.checkStoreProximity(locationLog);

        verify(storeEntranceService, times(1)).logEntrance(any(Courier.class), any(Store.class), anyDouble(), anyDouble());
    }

    @Test
    void testCheckStoreProximity_outOfRange() {
        Courier courier = new Courier();
        courier.setId(1L);
        Store store = new Store();
        store.setId(1L);
        store.setLat(40.7128);
        store.setLng(-74.0060);

        CourierLocationLog locationLog = CourierLocationLog.builder()
                .courier(courier)
                .lat(40.7128)
                .lng(-74.0060)
                .timestamp(LocalDateTime.now())
                .build();

        when(storeRepository.findAll()).thenReturn(Collections.singletonList(store));
        when(distanceService.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble())).thenReturn(150.0);

        courierLocationService.checkStoreProximity(locationLog);

        verify(storeEntranceService, times(0)).logEntrance(any(Courier.class), any(Store.class), anyDouble(), anyDouble());
    }
}
