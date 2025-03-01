package com.tracker.courier.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tracker.common.entity.BaseEntity;
import com.tracker.store.entity.StoreEntranceLog;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.util.LinkedList;
import java.util.List;
/**
 * Entity class representing a courier.
 * Extends BaseEntity to inherit common fields and functionality.
 * Contains fields for ID, username, total distance traveled, and a list of location logs.
 * Uses JPA annotations for ORM mapping and Lombok annotations for boilerplate code generation.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "courier")
public class Courier extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = 7115339494607621408L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "username", length = 100, nullable = false)
    @NotBlank
    private String username;

    @Builder.Default
    @Column(name = "total_distance")
    private double totalDistance = 0.0;


    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @Builder.Default
    private List<CourierLocationLog> locationLogs = new LinkedList<>();
}
