package com.tracker.courier.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
/**
 * Data transfer object (DTO) for courier location log information.
 * Contains fields for the log's ID, courier ID, latitude, longitude, and timestamp.
 * Uses Lombok annotations for getter and setter methods.
 */

@Getter
@Setter
public class CourierLocationLogDto {
    private Long id;
    private Long courierId;
    private Double lat;
    private Double lng;
    private LocalDateTime timestamp;
}
