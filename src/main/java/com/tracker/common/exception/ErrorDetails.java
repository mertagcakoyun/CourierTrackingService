package com.tracker.common.exception;


import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
/**
 * Class representing the details of an error response.
 * Contains fields for the timestamp, HTTP status, error message, and additional details.
 * Used to structure the error response returned by the global exception handler.
 */
@Getter
@Setter
public class ErrorDetails {
    private LocalDateTime timestamp;
    private HttpStatus status;
    private String message;
    private String details;

    public ErrorDetails(HttpStatus status, String message, String details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
