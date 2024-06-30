package com.tracker.store.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tracker.courier.entity.Courier;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
/**
 * Entity class representing a log of a courier's entrance into a store.
 * Contains fields for ID, courier, store, latitude, longitude, and entrance time.
 * Uses JPA annotations for ORM mapping and Lombok annotations for boilerplate code generation.
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "store_entrance_log")
public class StoreEntranceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courier_id", nullable = false)
    @JsonBackReference
    private Courier courier;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @JsonBackReference
    private Store store;

    @Column(name = "latitude", nullable = false)
    private double lat;

    @Column(name = "longitude", nullable = false)
    private double lng;

    @Column(nullable = false)
    private LocalDateTime entranceTime;
}

