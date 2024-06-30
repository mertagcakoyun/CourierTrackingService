package com.tracker.store.entity;

import jakarta.persistence.*;
import lombok.*;
/**
 * Entity class representing a store.
 * Contains fields for ID, name, latitude, and longitude.
 * Uses JPA annotations for ORM mapping and Lombok annotations for boilerplate code generation.
 */

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "latitude", nullable = false)
    private double lat;

    @Column(name = "longitude", nullable = false)
    private double lng;
}
