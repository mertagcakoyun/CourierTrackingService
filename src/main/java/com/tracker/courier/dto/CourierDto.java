package com.tracker.courier.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * Data transfer object (DTO) for courier information.
 * Contains fields for the courier's ID, username, total distance traveled, and a list of location logs.
 * Uses Lombok annotations for getter and setter methods.
 */

@Getter
@Setter
public class CourierDto {
    private Long id;
    private String username;
    private Double totalDistance;
    private List<CourierLocationLogDto> locationLogs;
}
