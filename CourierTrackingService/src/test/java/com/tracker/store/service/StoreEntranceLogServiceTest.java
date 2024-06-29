package com.tracker.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.courier.entity.Courier;
import com.tracker.store.dto.StoreEntranceLogDto;
import com.tracker.store.entity.Store;
import com.tracker.store.entity.StoreEntranceLog;
import com.tracker.store.repository.StoreEntranceLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoreEntranceLogServiceTest {

    @InjectMocks
    private StoreEntranceLogService storeEntranceLogService;

    @Mock
    private StoreEntranceLogRepository storeEntranceLogRepository;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetLogsByCourierId() {
        Long courierId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Courier courier = new Courier();
        courier.setId(courierId);

        Store store = new Store();
        store.setId(1L);

        StoreEntranceLog log1 = new StoreEntranceLog();
        log1.setCourier(courier);
        log1.setStore(store);

        StoreEntranceLog log2 = new StoreEntranceLog();
        log2.setCourier(courier);
        log2.setStore(store);

        List<StoreEntranceLog> logs = Arrays.asList(log1, log2);
        Page<StoreEntranceLog> pagedLogs = new PageImpl<>(logs);

        when(storeEntranceLogRepository.findByCourierId(courierId, pageable)).thenReturn(pagedLogs);
        when(objectMapper.convertValue(any(StoreEntranceLog.class), eq(StoreEntranceLogDto.class))).thenReturn(new StoreEntranceLogDto());

        Page<StoreEntranceLogDto> result = storeEntranceLogService.getLogsByCourierId(courierId, pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(storeEntranceLogRepository, times(1)).findByCourierId(courierId, pageable);
    }

    @Test
    public void testIsReEntry_NoReEntry() {
        Long courierId = 1L;
        Long storeId = 1L;
        LocalDateTime entranceTime = LocalDateTime.now();

        when(storeEntranceLogRepository.findByCourierIdAndStoreIdAndEntranceTimeAfter(courierId, storeId, entranceTime.minusMinutes(1)))
                .thenReturn(Collections.emptyList());

        boolean result = storeEntranceLogService.isReEntry(courierId, storeId, entranceTime);

        assertFalse(result);
        verify(storeEntranceLogRepository, times(1))
                .findByCourierIdAndStoreIdAndEntranceTimeAfter(courierId, storeId, entranceTime.minusMinutes(1));
    }

    @Test
    public void testIsReEntry_WithReEntry() {
        Long courierId = 1L;
        Long storeId = 1L;
        LocalDateTime entranceTime = LocalDateTime.now();

        StoreEntranceLog log = new StoreEntranceLog();
        List<StoreEntranceLog> recentEntries = Arrays.asList(log);

        when(storeEntranceLogRepository.findByCourierIdAndStoreIdAndEntranceTimeAfter(courierId, storeId, entranceTime.minusMinutes(1)))
                .thenReturn(recentEntries);

        boolean result = storeEntranceLogService.isReEntry(courierId, storeId, entranceTime);

        assertTrue(result);
        verify(storeEntranceLogRepository, times(1))
                .findByCourierIdAndStoreIdAndEntranceTimeAfter(courierId, storeId, entranceTime.minusMinutes(1));
    }

    @Test
    public void testLogEntrance_NoReEntry() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(1L);

        when(storeEntranceLogRepository.findByCourierIdAndStoreIdAndEntranceTimeAfter(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(Collections.emptyList());

        when(storeEntranceLogRepository.save(any(StoreEntranceLog.class))).thenAnswer(invocation -> invocation.getArgument(0));

        storeEntranceLogService.logEntrance(courier, store, 40.0, 29.0);

        verify(storeEntranceLogRepository, times(1)).save(any(StoreEntranceLog.class));
    }

    @Test
    public void testLogEntrance_WithReEntry() {
        Courier courier = new Courier();
        courier.setId(1L);

        Store store = new Store();
        store.setId(1L);

        StoreEntranceLog log = new StoreEntranceLog();
        List<StoreEntranceLog> recentEntries = Arrays.asList(log);

        when(storeEntranceLogRepository.findByCourierIdAndStoreIdAndEntranceTimeAfter(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(recentEntries);

        storeEntranceLogService.logEntrance(courier, store, 40.0, 29.0);

        verify(storeEntranceLogRepository, times(0)).save(any(StoreEntranceLog.class));
    }
}
