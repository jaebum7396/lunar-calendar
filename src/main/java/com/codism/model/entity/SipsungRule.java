package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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
    @Comment("십성 규칙 ID (Primary Key)")
    private Long ruleId;

    @Column(name = "sipsung_id", nullable = false)
    @Comment("십성 마스터 ID (Foreign Key)")
    private Long sipsungId;

    @Column(name = "ilgan_ohang", length = 10)
    @Comment("일간의 오행 (목, 화, 토, 금, 수)")
    private String ilganOhang;

    @Column(name = "target_ohang", length = 10)
    @Comment("대상의 오행 (목, 화, 토, 금, 수)")
    private String targetOhang;

    @Column(name = "ilgan_eumyang", length = 10)
    @Comment("일간의 음양 (양, 음)")
    private String ilganEumyang;

    @Column(name = "target_eumyang", length = 10)
    @Comment("대상의 음양 (양, 음)")
    private String targetEumyang;

    @Column(name = "relationship", length = 50)
    @Comment("오행 관계 (같은 오행, 생하는 관계, 극하는 관계 등)")
    private String relationship;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sipsung_id", insertable = false, updatable = false)
    private SipsungMaster sipsungMaster;
}
