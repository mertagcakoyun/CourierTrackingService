package com.tracker.courier.dto;

import com.tracker.courier.entity.CourierLocationLog;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CourierLocationLogDto {
    private Long id;
    private Long courierId;
    private Double lat;
    private Double lng;
    private LocalDateTime timestamp;
}
