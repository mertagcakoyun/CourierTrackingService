package com.tracker.courier.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.common.exception.OptimisticLockingException;
import com.tracker.common.exception.ResourceNotFoundException;
import com.tracker.common.service.DistanceService;
import com.tracker.common.service.HaversineDistanceCalculation;
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
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourierLocationServiceTest {

    @InjectMocks
    private CourierLocationService courierLocationService;

    @Mock
    private CourierLocationLogRepository locationLogRepository;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreEntranceLogService storeEntranceService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HaversineDistanceCalculation haversineDistanceCalculation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogLocation_CourierNotFound() {
        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);

        when(courierRepository.findById(request.getCourierId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            courierLocationService.logLocation(request);
        });

        verify(courierRepository, times(1)).findById(request.getCourierId());
    }

    @Test
    public void testLogLocation_Success() {
        Courier courier = new Courier();
        courier.setId(1L);
        courier.setTotalDistance(0.0);

        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);
        request.setLat(40.0);
        request.setLng(29.0);

        CourierLocationLog lastLog = new CourierLocationLog();
        lastLog.setLat(39.0);
        lastLog.setLng(28.0);
        courier.setLocationLogs(Arrays.asList(lastLog));

        when(courierRepository.findById(request.getCourierId())).thenReturn(Optional.of(courier));
        when(haversineDistanceCalculation.calculateDistance(39.0, 28.0, 40.0, 29.0)).thenReturn(100.0);
        when(locationLogRepository.save(any(CourierLocationLog.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(objectMapper.convertValue(any(CourierLocationLog.class), eq(CourierLocationLogDto.class)))
                .thenAnswer(invocation -> new CourierLocationLogDto());

        CourierLocationLogDto result = courierLocationService.logLocation(request);

        assertNotNull(result);
        verify(courierRepository, times(1)).findById(request.getCourierId());
        verify(haversineDistanceCalculation, times(1)).calculateDistance(39.0, 28.0, 40.0, 29.0);
        verify(locationLogRepository, times(1)).save(any(CourierLocationLog.class));
    }

    @Test
    public void testCheckStoreProximity() {
        Courier courier = new Courier();
        courier.setId(1L);

        CourierLocationLog log = new CourierLocationLog();
        log.setLat(40.0);
        log.setLng(29.0);
        log.setCourier(courier);

        Store store = new Store();
        store.setId(1L);
        store.setLat(40.0);
        store.setLng(29.0);

        when(storeRepository.findAll()).thenReturn(Arrays.asList(store));
        when(haversineDistanceCalculation.calculateDistance(40.0, 29.0, 40.0, 29.0)).thenReturn(50.0);
        when(storeEntranceService.isReEntry(1L, 1L, log.getTimestamp())).thenReturn(false);

        courierLocationService.checkStoreProximity(log);

        verify(storeRepository, times(1)).findAll();
        verify(haversineDistanceCalculation, times(1)).calculateDistance(40.0, 29.0, 40.0, 29.0);
        verify(storeEntranceService, times(1)).isReEntry(1L, 1L, log.getTimestamp());
        verify(storeEntranceService, times(1)).logEntrance(courier, store, 40.0, 29.0);
    }

    @Test
    public void testConvertToLocationLogDto() {
        CourierLocationLog log = new CourierLocationLog();
        Courier courier = new Courier();
        courier.setId(1L);
        log.setCourier(courier);

        CourierLocationLogDto dto = new CourierLocationLogDto();
        dto.setCourierId(1L);

        when(objectMapper.convertValue(log, CourierLocationLogDto.class)).thenReturn(dto);

        CourierLocationLogDto result = courierLocationService.convertToLocationLogDto(log);

        assertEquals(1L, result.getCourierId());
        verify(objectMapper, times(1)).convertValue(log, CourierLocationLogDto.class);
    }
}
