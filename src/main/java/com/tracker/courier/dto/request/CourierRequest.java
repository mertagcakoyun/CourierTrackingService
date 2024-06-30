package com.tracker.courier.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourierRequest {
    @NotNull
    private String username;
}