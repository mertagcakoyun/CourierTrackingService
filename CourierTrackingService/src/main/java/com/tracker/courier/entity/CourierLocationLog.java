package com.tracker.courier.entity;

import com.tracker.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "courier_location")
@Entity
public class CourierLocationLog extends BaseEntity<Long> {

    @Serial
    private static final long serialVersionUID = -5436631024276352543L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double lat;

    @Column(name = "longitude", nullable = false)
    private Double lng;

    @Column(name = "time", nullable = false)
    private LocalDateTime timestamp;

    @ToString.Include
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id", referencedColumnName = "id", nullable = false)
    private Courier courier;
}
