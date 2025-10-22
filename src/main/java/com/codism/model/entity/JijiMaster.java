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
    @Comment("지지 ID (Primary Key)")
    private Long jijiId;

    @Column(name = "jiji_korean", nullable = false, unique = true, length = 10)
    @Comment("지지 한글명 (자, 축, 인, 묘, 진, 사, 오, 미, 신, 유, 술, 해)")
    private String jijiKorean;

    @Column(name = "jiji_hanja", nullable = false, length = 10)
    @Comment("지지 한자 (子, 丑, 寅, 卯, 辰, 巳, 午, 未, 申, 酉, 戌, 亥)")
    private String jijiHanja;

    @Column(name = "jiji_english", length = 20)
    @Comment("지지 영문명 (Ja, Chuk, In, ...)")
    private String jijiEnglish;

    @Column(name = "animal", length = 20)
    @Comment("띠 동물 (쥐, 소, 범, 토끼, 용, 뱀, 말, 양, 원숭이, 닭, 개, 돼지)")
    private String animal;

    @Column(name = "ohang", nullable = false, length = 10)
    @Comment("오행 속성 (목, 화, 토, 금, 수)")
    private String ohang;

    @Column(name = "direction", length = 10)
    @Comment("방위 (북, 북동, 동, 동남, 남, 남서, 서, 서북)")
    private String direction;

    @Column(name = "season", length = 10)
    @Comment("계절 (봄, 여름, 가을, 겨울)")
    private String season;

    @Column(name = "jiji_month")
    @Comment("해당 월 (1~12)")
    private Integer month;

    @Column(name = "time_start")
    @Comment("시작 시각 (1~23)")
    private Integer timeStart;

    @Column(name = "time_end")
    @Comment("종료 시각 (1~23)")
    private Integer timeEnd;

    @Column(name = "sequence_order", nullable = false)
    @Comment("지지 순서 (1~12)")
    private Integer sequenceOrder;

    @Column(name = "description", columnDefinition = "TEXT")
    @Comment("지지 설명 및 특성")
    private String description;

    @Column(name = "color", length = 20)
    @Comment("오행 색상 (한글)")
    private String color;

    @Column(name = "color_hex", length = 10)
    @Comment("오행 색상 (HEX 코드)")
    private String colorHex;

    @Column(name = "jijanggan", length = 10)
    @Comment("지장간(지지 본기) - 십성 계산용 (갑, 을, 병...)")
    private String jijanggan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @Comment("생성 일시")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    @Comment("수정 일시")
    private LocalDateTime updatedAt;
}
