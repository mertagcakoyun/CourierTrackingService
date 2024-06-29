package com.tracker.courier.service;

import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.courier.entity.Courier;
import com.tracker.courier.entity.CourierLocationLog;
import com.tracker.courier.repository.CourierLocationLogRepository;
import com.tracker.courier.repository.CourierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DistanceServiceTest {

    @InjectMocks
    private DistanceService distanceService;

    @Mock
    private CourierLocationLogRepository locationLogRepository;

    @Mock
    private CourierRepository courierRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTotalDistanceFromDB_CourierNotFound() {
        Long courierId = 1L;
        when(courierRepository.findById(courierId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            distanceService.getTotalDistanceFromDB(courierId);
        });

        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    public void testGetTotalDistanceFromDB_CourierFound_NoLogs() {
        Long courierId = 1L;
        Courier courier = new Courier();
        courier.setId(courierId);
        courier.setTotalDistance(0.0);
        courier.setLocationLogs(Collections.emptyList());

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        double result = distanceService.getTotalDistanceFromDB(courierId);

        assertEquals(0.0, result);
        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    public void testGetTotalDistanceFromDB_CourierFound_WithLogs() {
        Long courierId = 1L;
        Courier courier = new Courier();
        courier.setId(courierId);
        courier.setTotalDistance(100.0);

        CourierLocationLog log1 = new CourierLocationLog();
        CourierLocationLog log2 = new CourierLocationLog();
        List<CourierLocationLog> logs = Arrays.asList(log1, log2);

        courier.setLocationLogs(logs);

        when(courierRepository.findById(courierId)).thenReturn(Optional.of(courier));

        double result = distanceService.getTotalDistanceFromDB(courierId);

        assertEquals(100.0, result);
        verify(courierRepository, times(1)).findById(courierId);
    }

    @Test
    public void testCalculateTotalDistance_NoLogs() {
        Long courierId = 1L;
        when(locationLogRepository.findByCourierIdOrderByTimestampAsc(courierId)).thenReturn(Collections.emptyList());

        double result = distanceService.calculateTotalDistance(courierId);

        assertEquals(0.0, result);
        verify(locationLogRepository, times(1)).findByCourierIdOrderByTimestampAsc(courierId);
    }

    @Test
    public void testCalculateTotalDistance_WithLogs() {
        Long courierId = 1L;
        CourierLocationLog log1 = new CourierLocationLog();
        log1.setLat(40.0);
        log1.setLng(29.0);

        CourierLocationLog log2 = new CourierLocationLog();
        log2.setLat(41.0);
        log2.setLng(30.0);

        List<CourierLocationLog> logs = Arrays.asList(log1, log2);

        when(locationLogRepository.findByCourierIdOrderByTimestampAsc(courierId)).thenReturn(logs);

        double result = distanceService.calculateTotalDistance(courierId);

        assertTrue(result > 0.0);
        verify(locationLogRepository, times(1)).findByCourierIdOrderByTimestampAsc(courierId);
    }

    @Test
    public void testCalculateDistance() {
        double lat1 = 40.0;
        double lon1 = 29.0;
        double lat2 = 41.0;
        double lon2 = 30.0;

        double result = distanceService.calculateDistance(lat1, lon1, lat2, lon2);

        assertTrue(result > 0.0);
    }

    @Test
    void shouldReturnZero_whenSamePointsSent() {
        var lat = 43.0012;
        var lon = 24.9224;
        var distance = distanceService.calculateDistance(lat, lon, lat, lon);
        assertEquals(0.0, distance);
    }

    @Test
    void shouldReturnNonZero_whenDifferentPointSent() {
        var lat1 = 41.00000;
        var lon1 = 29.00000;
        var lat2 = 42.00000;
        var lon2 = 29.00000;
        var expectedDistance = 111195;

        var distance = distanceService.calculateDistance(lat1, lon1, lat2, lon2);
        assertEquals(expectedDistance, distance, 10.0);
    }
}
