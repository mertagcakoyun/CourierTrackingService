package com.tracker.store.controller;

import com.tracker.store.controller.StoreEntranceLogController;
import com.tracker.store.dto.StoreEntranceLogDto;
import com.tracker.store.service.StoreEntranceLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(StoreEntranceLogController.class)
public class StoreEntranceLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StoreEntranceLogService storeEntranceLogService;

    @Test
    public void testGetLogsByCourierId() throws Exception {
        Long courierId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        StoreEntranceLogDto log1 = new StoreEntranceLogDto();
        log1.setCourierId(courierId);
        log1.setStoreId(1L);
        log1.setLat(40.7128);
        log1.setLng(-74.0060);
        log1.setEntranceTime(LocalDateTime.now());

        StoreEntranceLogDto log2 = new StoreEntranceLogDto();
        log2.setCourierId(courierId);
        log2.setStoreId(2L);
        log2.setLat(34.0522);
        log2.setLng(-118.2437);
        log2.setEntranceTime(LocalDateTime.now());

        Page<StoreEntranceLogDto> pageableLogs = new PageImpl<>(Arrays.asList(log1, log2), pageable, 2);

        when(storeEntranceLogService.getLogsByCourierId(courierId, pageable)).thenReturn(pageableLogs);

        mockMvc.perform(get("/api/store-entrance-logs/courier/{courierId}", courierId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].courierId").value(courierId))
                .andExpect(jsonPath("$.content[0].storeId").value(1L))
                .andExpect(jsonPath("$.content[1].courierId").value(courierId))
                .andExpect(jsonPath("$.content[1].storeId").value(2L));
    }
}
