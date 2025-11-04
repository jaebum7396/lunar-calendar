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
 * 육십갑자(六十甲子) 마스터 엔티티
 * 천간(10) x 지지(12) = 60개 조합
 * 각 간지 조합별 특징 정보 저장
 */
@Entity
@Table(name = "yukship_ganji_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YukshipGanjiMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ganji_id")
    @Comment("간지 ID (Primary Key)")
    private Long ganjiId;

    @Column(name = "cheongan", nullable = false, length = 10)
    @Comment("천간 한글 (갑, 을, 병, 정, 무, 기, 경, 신, 임, 계)")
    private String cheongan;

    @Column(name = "jiji", nullable = false, length = 10)
    @Comment("지지 한글 (자, 축, 인, 묘, 진, 사, 오, 미, 신, 유, 술, 해)")
    private String jiji;

    @Column(name = "ganji_name", nullable = false, unique = true, length = 20)
    @Comment("간지 조합 (갑자, 을축, 병인...)")
    private String ganjiName;

    @Column(name = "characteristic", nullable = false, length = 50)
    @Comment("특징/성격 형용사 (센스 있는, 영리한, 용감한...)")
    private String characteristic;

    @Column(name = "sequence_order", nullable = false)
    @Comment("육십갑자 순서 (1~60)")
    private Integer sequenceOrder;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("간지 조합 상세 설명")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;
}
