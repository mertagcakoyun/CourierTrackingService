package com.tracker.courier.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
/**
 * Data transfer object (DTO) for courier creation requests.
 * Contains a mandatory field for the courier's username.
 * Uses Lombok annotations for getter and setter methods.
 */
@Getter
@Setter
public class CourierRequest {
    @NotNull
    private String username;
}