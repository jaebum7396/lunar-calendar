package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
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
public class OhangRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    @Comment("오행 관계 ID (Primary Key)")
    private Long relationId;

    @Column(name = "source_ohang", nullable = false, length = 10)
    @Comment("출발 오행 (목, 화, 토, 금, 수)")
    private String sourceOhang;

    @Column(name = "target_ohang", nullable = false, length = 10)
    @Comment("대상 오행 (목, 화, 토, 금, 수)")
    private String targetOhang;

    @Column(name = "relation_type", nullable = false, length = 20)
    @Comment("관계 유형 (상생, 상극)")
    private String relationType;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("관계 설명 (목생화, 화극금 등)")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;
}
