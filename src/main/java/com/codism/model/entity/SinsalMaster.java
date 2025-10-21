package com.codism.model.entity;

import com.codism.model.enums.SinsalCategory;
import com.codism.model.enums.SinsalType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
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
public class SinsalMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sinsal_id")
    @Comment("신살 ID (Primary Key)")
    private Long sinsalId;

    @Column(name = "sinsal_name", nullable = false, unique = true, length = 50)
    @Comment("신살 이름 (천을귀인, 망신살, 도화살 등)")
    private String sinsalName;

    @Column(name = "sinsal_hanja", length = 50)
    @Comment("신살 한자 (天乙貴人, 亡神殺, 桃花殺 등)")
    private String sinsalHanja;

    @Enumerated(EnumType.STRING)
    @Column(name = "sinsal_type", nullable = false, length = 20)
    @Comment("신살 유형 (GILSIN: 길신, HYUNGSIN: 흉신)")
    private SinsalType sinsalType;

    @Column(name = "icon", length = 20)
    @Comment("아이콘 (☀️, ⚠️, 🌸 등)")
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", length = 30)
    @Comment("신살 카테고리 (GWIIN: 귀인, SALSUNG: 살성)")
    private SinsalCategory category;

    @Column(name = "priority")
    @Comment("우선순위 (높을수록 중요)")
    private Integer priority;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("간략 설명")
    private String description;

    @Column(name = "detail_description", columnDefinition = "TEXT")
    @Comment("상세 설명")
    private String detailDescription;

    @Column(name = "effect_positive", columnDefinition = "TEXT")
    @Comment("긍정적 효과")
    private String effectPositive;

    @Column(name = "effect_negative", columnDefinition = "TEXT")
    @Comment("부정적 효과")
    private String effectNegative;

    @Column(name = "is_active")
    @Comment("활성화 여부")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;
}
