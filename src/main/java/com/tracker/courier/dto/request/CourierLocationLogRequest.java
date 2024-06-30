package com.tracker.courier.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourierLocationLogRequest {
    @NotNull
    private Long courierId;
    @NotNull
    private Double lat;
    @NotNull
    private Double lng;
}
