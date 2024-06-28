package com.tracker.store.repository;

import com.tracker.store.entity.StoreEntranceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StoreEntranceLogRepository extends JpaRepository<StoreEntranceLog, Long> {
    List<StoreEntranceLog> findByCourierIdAndStoreIdAndEntranceTimeAfter(Long courierId, Long storeId, LocalDateTime entranceTime);
}
