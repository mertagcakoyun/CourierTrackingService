package com.tracker.courier.service;

import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.common.service.DistanceCalculationStrategy;
import com.tracker.common.service.DistanceService;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DistanceServiceTest {

    @Mock
    private CourierLocationLogRepository locationLogRepository;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private DistanceCalculationStrategy distanceCalculationStrategy;

    @InjectMocks
    private DistanceService distanceService;

    private Courier courier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courier = new Courier();
        courier.setId(1L);
        courier.setTotalDistance(10.0);

        CourierLocationLog log1 = new CourierLocationLog();
        log1.setLat(40.9923307);
        log1.setLng(29.1244229);

        CourierLocationLog log2 = new CourierLocationLog();
        log2.setLat(40.9933307);
        log2.setLng(29.1254229);

        CourierLocationLog log3 = new CourierLocationLog();
        log3.setLat(40.9943307);
        log3.setLng(29.1264229);

        List<CourierLocationLog> logs = Arrays.asList(log1, log2, log3);
        courier.setLocationLogs(logs);
    }

    @Test
    void testGetTotalDistanceFromDB_CourierFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.of(courier));
        double totalDistance = distanceService.getTotalDistanceFromDB(1L);
        assertEquals(10.0, totalDistance);
    }

    @Test
    void testGetTotalDistanceFromDB_CourierNotFound() {
        when(courierRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> distanceService.getTotalDistanceFromDB(1L));
    }

    @Test
    void testCalculateTotalDistance_NoLogs() {
        when(locationLogRepository.findByCourierIdOrderByTimestampAsc(courier.getId())).thenReturn(Arrays.asList());
        double totalDistance = distanceService.calculateTotalDistance(courier.getId());
        assertEquals(0.0, totalDistance);
        verify(distanceCalculationStrategy, never()).calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }
}
