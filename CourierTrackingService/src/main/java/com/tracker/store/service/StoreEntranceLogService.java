package com.tracker.store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.courier.entity.Courier;
import com.tracker.store.dto.StoreEntranceLogDto;
import com.tracker.store.entity.Store;
import com.tracker.store.entity.StoreEntranceLog;
import com.tracker.store.repository.StoreEntranceLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StoreEntranceLogService {

    @Autowired
    private StoreEntranceLogRepository storeEntranceLogRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Page<StoreEntranceLogDto> getLogsByCourierId(Long courierId, Pageable pageable) {
        Page<StoreEntranceLog> logs = storeEntranceLogRepository.findByCourierId(courierId, pageable);
        return logs.map(this::convertToDto);
    }

    public boolean isReEntry(Long courierId, Long storeId, LocalDateTime entranceTime) {
        List<StoreEntranceLog> recentEntries = storeEntranceLogRepository.findByCourierIdAndStoreIdAndEntranceTimeAfter(courierId, storeId, entranceTime.minusMinutes(1));
        return !recentEntries.isEmpty();
    }

    @Async
    @Transactional
    public void logEntrance(Courier courier, Store store, double lat, double lng) {
        if (!isReEntry(courier.getId(), store.getId(), LocalDateTime.now())) {
            StoreEntranceLog storeEntranceLog = StoreEntranceLog.builder()
                    .courier(courier)
                    .store(store)
                    .lat(lat)
                    .lng(lng)
                    .entranceTime(LocalDateTime.now())
                    .build();
            storeEntranceLogRepository.save(storeEntranceLog);
        }
    }

    private StoreEntranceLogDto convertToDto(StoreEntranceLog log) {
        StoreEntranceLogDto logDto = objectMapper.convertValue(log, StoreEntranceLogDto.class);
        logDto.setCourierId(log.getCourier().getId());
        logDto.setStoreId(log.getStore().getId());
        return logDto;
    }
}