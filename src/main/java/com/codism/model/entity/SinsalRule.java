package com.codism.model.entity;

import com.codism.model.enums.SinsalRuleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

/**
 * 신살 계산 규칙 엔티티
 * 각 신살의 계산 규칙 데이터
 */
@Entity
@Table(name = "sinsal_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SinsalRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    @Comment("신살 규칙 ID (Primary Key)")
    private Long ruleId;

    @Column(name = "sinsal_id", nullable = false)
    @Comment("신살 마스터 ID (Foreign Key)")
    private Long sinsalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "rule_type", nullable = false, length = 30)
    @Comment("규칙 타입 (ILGAN_JIJI, YENJI_COMBINATION, JIJI_PATTERN 등)")
    private SinsalRuleType ruleType;

    @Column(name = "condition_ilgan", columnDefinition = "TEXT")
    @Comment("일간 조건 (JSON 배열 형식)")
    private String conditionIlgan;

    @Column(name = "condition_yenji", columnDefinition = "TEXT")
    @Comment("년지 조건 (JSON 배열 형식)")
    private String conditionYenji;

    @Column(name = "condition_target", columnDefinition = "TEXT")
    @Comment("대상 조건 (JSON 배열 형식)")
    private String conditionTarget;

    @Column(name = "match_position", length = 50)
    @Comment("매칭 위치 (year, month, day, hour, any)")
    private String matchPosition;

    @Column(name = "require_all")
    @Comment("모든 조건 필수 여부")
    private Boolean requireAll;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("규칙 설명")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sinsal_id", insertable = false, updatable = false)
    private SinsalMaster sinsalMaster;
}
