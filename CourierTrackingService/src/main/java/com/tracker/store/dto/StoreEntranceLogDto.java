package com.tracker.store.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreEntranceLogDto {
    private Long id;
    private Long courierId;
    private Long storeId;
    private Double lat;
    private Double lng;
    private LocalDateTime entranceTime;
}

