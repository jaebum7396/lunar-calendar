package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
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
public class SipsungMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sipsung_id")
    @Comment("십성 ID (Primary Key)")
    private Long sipsungId;

    @Column(name = "sipsung_name", nullable = false, unique = true, length = 20)
    @Comment("십성 이름 (비견, 겁재, 식신, 상관, 편재, 정재, 편관, 정관, 편인, 정인)")
    private String sipsungName;

    @Column(name = "sipsung_hanja", length = 20)
    @Comment("십성 한자 (比肩, 劫財, 食神, ...)")
    private String sipsungHanja;

    @Column(name = "sipsung_category", length = 20)
    @Comment("십성 분류 (비겁, 식상, 재성, 관성, 인성)")
    private String sipsungCategory;

    @Column(name = "relationship_type", length = 50)
    @Comment("관계 유형 (같은 오행, 내가 생하는 오행, 내가 극하는 오행, 나를 극하는 오행, 나를 생하는 오행)")
    private String relationshipType;

    @Column(name = "eumyang_condition", length = 20)
    @Comment("음양 조건 (같음, 다름)")
    private String eumyangCondition;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("십성 설명")
    private String description;

    @Column(name = "personality", columnDefinition = "TEXT")
    @Comment("성격 특성")
    private String personality;

    @Column(name = "fortune", columnDefinition = "TEXT")
    @Comment("운세 특성")
    private String fortune;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;
}
