package com.codism.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 지지(地支) 마스터 엔티티
 * 12개의 지지 데이터 (자축인묘진사오미신유술해)
 */
@Entity
@Table(name = "jiji_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JijiMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "jiji_id")
    private Long jijiId;

    @Column(name = "jiji_korean", nullable = false, unique = true, length = 10)
    private String jijiKorean;

    @Column(name = "jiji_hanja", nullable = false, length = 10)
    private String jijiHanja;

    @Column(name = "jiji_english", length = 20)
    private String jijiEnglish;

    @Column(name = "animal", length = 20)
    private String animal;

    @Column(name = "ohang", nullable = false, length = 10)
    private String ohang;

    @Column(name = "direction", length = 10)
    private String direction;

    @Column(name = "season", length = 10)
    private String season;

    @Column(name = "month")
    private Integer month;

    @Column(name = "time_start")
    private Integer timeStart;

    @Column(name = "time_end")
    private Integer timeEnd;

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
