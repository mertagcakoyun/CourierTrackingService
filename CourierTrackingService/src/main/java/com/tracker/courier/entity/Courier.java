package com.tracker.courier.entity;

import com.tracker.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.io.Serial;
import java.util.List;

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

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourierLocationLog> locationLogs;
}
