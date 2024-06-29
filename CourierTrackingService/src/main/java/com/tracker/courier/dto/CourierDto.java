package com.tracker.courier.dto;

import com.tracker.courier.entity.CourierLocationLog;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CourierDto {
    private Long id;
    private String username;
    private Double totalDistance;
    private List<CourierLocationLogDto> locationLogs;
}
