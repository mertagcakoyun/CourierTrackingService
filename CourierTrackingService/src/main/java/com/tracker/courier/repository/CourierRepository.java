package com.tracker.courier.repository;

import com.tracker.courier.entity.Courier;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {
    @Override
    @Lock(value = LockModeType.OPTIMISTIC)
    Optional<Courier> findById(Long courierId);
}