package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 신살 계산 규칙 엔티티
 * 각 신살의 계산 규칙 데이터
 */
@Entity
@Table(name = "sinsal_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinsalRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "sinsal_id", nullable = false)
    private Long sinsalId;

    @Column(name = "rule_type", nullable = false, length = 30)
    private String ruleType;

    @Column(name = "condition_ilgan", columnDefinition = "TEXT")
    private String conditionIlgan;

    @Column(name = "condition_yenji", columnDefinition = "TEXT")
    private String conditionYenji;

    @Column(name = "condition_target", columnDefinition = "TEXT")
    private String conditionTarget;

    @Column(name = "match_position", length = 50)
    private String matchPosition;

    @Column(name = "require_all")
    private Boolean requireAll;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sinsal_id", insertable = false, updatable = false)
    private SinsalMaster sinsalMaster;
}
