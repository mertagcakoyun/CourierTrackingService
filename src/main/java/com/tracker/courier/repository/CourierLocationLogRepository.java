package com.tracker.courier.repository;

import com.tracker.courier.entity.CourierLocationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierLocationLogRepository extends JpaRepository<CourierLocationLog, Long> {
    List<CourierLocationLog> findByCourierIdOrderByTimestampAsc(Long courierId);
}