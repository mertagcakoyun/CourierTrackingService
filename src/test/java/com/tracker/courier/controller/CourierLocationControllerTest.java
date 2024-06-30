package com.tracker.courier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.courier.dto.CourierLocationLogDto;
import com.tracker.courier.dto.request.CourierLocationLogRequest;
import com.tracker.courier.service.CourierLocationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CourierLocationController.class)
public class CourierLocationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourierLocationService courierLocationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLogLocation() throws Exception {
        CourierLocationLogRequest request = new CourierLocationLogRequest();
        request.setCourierId(1L);
        request.setLat(40.7128);
        request.setLng(-74.0060);

        CourierLocationLogDto response = new CourierLocationLogDto();
        response.setCourierId(1L);
        response.setLat(40.7128);
        response.setLng(-74.0060);

        when(courierLocationService.logLocation(any(CourierLocationLogRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/courier-location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courierId").value(1L))
                .andExpect(jsonPath("$.lat").value(40.7128))
                .andExpect(jsonPath("$.lng").value(-74.0060));
    }
}
