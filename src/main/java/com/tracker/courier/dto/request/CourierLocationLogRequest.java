package com.tracker.courier.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Data transfer object (DTO) for logging courier location requests.
 * Contains fields for courierId, latitude (lat), and longitude (lng), all of which are mandatory.
 * Uses Lombok annotations for boilerplate code generation.
 */

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
