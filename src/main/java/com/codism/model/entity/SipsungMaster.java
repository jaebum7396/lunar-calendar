package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 십성(十星) 마스터 엔티티
 * 10개의 십성 데이터 (비견, 겁재, 식신, 상관, 편재, 정재, 편관, 정관, 편인, 정인)
 */
@Entity
@Table(name = "sipsung_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SipsungMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sipsung_id")
    private Long sipsungId;

    @Column(name = "sipsung_name", nullable = false, unique = true, length = 20)
    private String sipsungName;

    @Column(name = "sipsung_hanja", length = 20)
    private String sipsungHanja;

    @Column(name = "sipsung_category", length = 20)
    private String sipsungCategory;

    @Column(name = "relationship_type", length = 50)
    private String relationshipType;

    @Column(name = "eumyang_condition", length = 20)
    private String eumyangCondition;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "personality", columnDefinition = "TEXT")
    private String personality;

    @Column(name = "fortune", columnDefinition = "TEXT")
    private String fortune;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
