package com.tracker.common.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;

/**
 * Abstract base entity class to be extended by other entity classes.
 * Provides common fields for auditing such as createdAt, createdBy, modifiedAt, modifiedBy, and version.
 * Uses JPA and Hibernate annotations for automatic timestamping and auditing.
 */
@Data
@MappedSuperclass
@ToString(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<T> implements Serializable {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Instant createdAt;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_at")
    private Instant modifiedAt;


    @LastModifiedBy
    @Column(name = "modified_by")
    private String modifiedBy;

    @Version
    @Column(name = "version")
    private Integer version;


    @ToString.Include
    @EqualsAndHashCode.Include
    public abstract T getId();

}
