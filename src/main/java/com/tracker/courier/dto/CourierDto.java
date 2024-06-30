package com.tracker.courier.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class CourierDto {
    private Long id;
    private String username;
    private Double totalDistance;
    private List<CourierLocationLogDto> locationLogs;
}
