package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 십성 계산 규칙 엔티티
 * 일간과 대상의 오행, 음양에 따른 십성 계산 규칙
 */
@Entity
@Table(name = "sipsung_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SipsungRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Long ruleId;

    @Column(name = "sipsung_id", nullable = false)
    private Long sipsungId;

    @Column(name = "ilgan_ohang", length = 10)
    private String ilganOhang;

    @Column(name = "target_ohang", length = 10)
    private String targetOhang;

    @Column(name = "ilgan_eumyang", length = 10)
    private String ilganEumyang;

    @Column(name = "target_eumyang", length = 10)
    private String targetEumyang;

    @Column(name = "relationship", length = 50)
    private String relationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sipsung_id", insertable = false, updatable = false)
    private SipsungMaster sipsungMaster;
}
