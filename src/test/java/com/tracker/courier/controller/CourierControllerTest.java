package com.tracker.courier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.courier.dto.CourierDto;
import com.tracker.courier.dto.request.CourierRequest;
import com.tracker.courier.service.CourierService;
import com.tracker.common.service.DistanceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CourierController.class)
public class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierService courierService;

    @MockBean
    private DistanceService distanceService;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void testGetCourierById() throws Exception {
        CourierDto courierDto = new CourierDto();
        courierDto.setId(1L);

        when(courierService.getCourierById(1L)).thenReturn(Optional.of(courierDto));

        mockMvc.perform(get("/api/courier/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testCreateCourier() throws Exception {
        CourierRequest courierRequest = new CourierRequest();
        courierRequest.setUsername("test");

        CourierDto courierDto = new CourierDto();
        courierDto.setId(1L);

        when(courierService.saveCourier(any(CourierRequest.class))).thenReturn(courierDto);

        mockMvc.perform(post("/api/courier")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courierRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    public void testDeleteCourier() throws Exception {
        Mockito.doNothing().when(courierService).deleteCourierById(1L);

        mockMvc.perform(delete("/api/courier/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testGetTotalTravelDistance() throws Exception {
        when(distanceService.calculateTotalDistance(1L)).thenReturn(100.0);

        mockMvc.perform(get("/api/courier/{id}/distanceV2", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(100.0));
    }

    @Test
    public void testGetTotalTravelDistanceFromDB() throws Exception {
        when(distanceService.getTotalDistanceFromDB(1L)).thenReturn(200.0);

        mockMvc.perform(get("/api/courier/{id}/distance", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(200.0));
    }
}
