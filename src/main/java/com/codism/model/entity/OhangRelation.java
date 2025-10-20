package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 오행 상생상극 관계 엔티티
 * 오행(목화토금수) 간의 상생/상극 관계
 */
@Entity
@Table(name = "ohang_relation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OhangRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private Long relationId;

    @Column(name = "source_ohang", nullable = false, length = 10)
    private String sourceOhang;

    @Column(name = "target_ohang", nullable = false, length = 10)
    private String targetOhang;

    @Column(name = "relation_type", nullable = false, length = 20)
    private String relationType;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
