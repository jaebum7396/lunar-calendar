package com.codism.model.entity;

import com.codism.model.enums.SinsalCategory;
import com.codism.model.enums.SinsalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 신살(神殺) 마스터 엔티티
 * 50+ 신살 데이터 (천을귀인, 망신살, 도화살 등)
 */
@Entity
@Table(name = "sinsal_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinsalMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sinsal_id")
    private Long sinsalId;

    @Column(name = "sinsal_name", nullable = false, unique = true, length = 50)
    private String sinsalName;

    @Column(name = "sinsal_hanja", length = 50)
    private String sinsalHanja;

    @Enumerated(EnumType.STRING)
    @Column(name = "sinsal_type", nullable = false, length = 20)
    private SinsalType sinsalType;

    @Column(name = "icon", length = 20)
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    private SinsalCategory category;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "detail_description", columnDefinition = "TEXT")
    private String detailDescription;

    @Column(name = "effect_positive", columnDefinition = "TEXT")
    private String effectPositive;

    @Column(name = "effect_negative", columnDefinition = "TEXT")
    private String effectNegative;

    @Column(name = "is_active")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
