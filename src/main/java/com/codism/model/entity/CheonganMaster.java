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
 * 천간(天干) 마스터 엔티티
 * 10개의 천간 데이터 (갑을병정무기경신임계)
 */
@Entity
@Table(name = "cheongan_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheonganMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cheongan_id")
    @Comment("천간 ID (Primary Key)")
    private Long cheonganId;

    @Column(name = "cheongan_korean", nullable = false, unique = true, length = 10)
    @Comment("천간 한글명 (갑, 을, 병, 정, 무, 기, 경, 신, 임, 계)")
    private String cheonganKorean;

    @Column(name = "cheongan_hanja", nullable = false, length = 10)
    @Comment("천간 한자 (甲, 乙, 丙, 丁, 戊, 己, 庚, 辛, 壬, 癸)")
    private String cheonganHanja;

    @Column(name = "cheongan_english", length = 20)
    @Comment("천간 영문명 (Gap, Eul, Byeong, ...)")
    private String cheonganEnglish;

    @Column(name = "ohang", nullable = false, length = 10)
    @Comment("오행 속성 (목, 화, 토, 금, 수)")
    private String ohang;

    @Column(name = "eumyang", nullable = false, length = 10)
    @Comment("음양 속성 (양, 음)")
    private String eumyang;

    @Column(name = "sequence_order", nullable = false)
    @Comment("천간 순서 (1~10)")
    private Integer sequenceOrder;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("천간 설명 및 특성")
    private String description;

    @Column(name = "color", length = 20)
    @Comment("오행 색상 (한글)")
    private String color;

    @Column(name = "color_hex", length = 10)
    @Comment("오행 색상 (HEX 코드)")
    private String colorHex;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;
}
