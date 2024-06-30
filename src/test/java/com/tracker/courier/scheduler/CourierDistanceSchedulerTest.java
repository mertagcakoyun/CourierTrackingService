package com.tracker.courier.scheduler;

import com.tracker.courier.entity.Courier;
import com.tracker.courier.repository.CourierRepository;
import com.tracker.courier.service.CourierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class CourierDistanceSchedulerTest {

    @InjectMocks
    private CourierDistanceScheduler courierDistanceScheduler;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private CourierService courierService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateCourierTotalDistances() {
        // Arrange
        Courier courier1 = new Courier();
        courier1.setId(1L);
        courier1.setUsername("Courier 1");

        Courier courier2 = new Courier();
        courier2.setId(2L);
        courier2.setUsername("Courier 2");

        List<Courier> couriers = Arrays.asList(courier1, courier2);

        when(courierRepository.findAll()).thenReturn(couriers);

        // Act
        courierDistanceScheduler.updateCourierTotalDistances();

        // Assert
        verify(courierRepository, times(1)).findAll();
        verify(courierService, times(1)).updateTotalDistanceForCourier(courier1);
        verify(courierService, times(1)).updateTotalDistanceForCourier(courier2);
    }
}
