package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Long cheonganId;

    @Column(name = "cheongan_korean", nullable = false, unique = true, length = 10)
    private String cheonganKorean;

    @Column(name = "cheongan_hanja", nullable = false, length = 10)
    private String cheonganHanja;

    @Column(name = "cheongan_english", length = 20)
    private String cheonganEnglish;

    @Column(name = "ohang", nullable = false, length = 10)
    private String ohang;

    @Column(name = "eumyang", nullable = false, length = 10)
    private String eumyang;

    @Column(name = "sequence_order", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "color", length = 20)
    private String color;

    @Column(name = "color_hex", length = 10)
    private String colorHex;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
