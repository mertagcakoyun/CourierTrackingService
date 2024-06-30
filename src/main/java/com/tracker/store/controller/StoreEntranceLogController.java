package com.tracker.store.controller;

import com.tracker.store.dto.StoreEntranceLogDto;
import com.tracker.store.service.StoreEntranceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/store-entrance-logs")
public class StoreEntranceLogController {

    @Autowired
    private StoreEntranceLogService storeEntranceLogService;

    @GetMapping("/courier/{courierId}")
    public ResponseEntity<Page<StoreEntranceLogDto>> getLogsByCourierId(@PathVariable Long courierId, Pageable pageable) {
        Page<StoreEntranceLogDto> pageableLogs = storeEntranceLogService.getLogsByCourierId(courierId, pageable);
        return ResponseEntity.ok(pageableLogs);
    }
}