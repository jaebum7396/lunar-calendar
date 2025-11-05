package com.codism.model.dto.response;

import com.codism.model.entity.SipsungMaster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 십성 상세 정보 응답 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SipsungDetailResponse {

    private String sipsungName;        // 십성 이름 (예: "비견")
    private String sipsungHanja;       // 십성 한자 (예: "比肩")
    private String sipsungCategory;    // 십성 카테고리 (예: "비겁", "식상", "재성", "관성", "인성")
    private String relationshipType;   // 관계 유형 (예: "형제", "자식", "재물", "직장", "학문")
    private String eumyangCondition;   // 음양 조건 (예: "same", "different")
    private String description;        // 간단한 설명
    private String personality;        // 성격 특성 (줄바꿈 포함)
    private String fortune;            // 운세 특성 (줄바꿈 포함)

    /**
     * Entity로부터 DTO 생성
     */
    public SipsungDetailResponse(SipsungMaster sipsung) {
        this.sipsungName = sipsung.getSipsungName();
        this.sipsungHanja = sipsung.getSipsungHanja();
        this.sipsungCategory = sipsung.getSipsungCategory();
        this.relationshipType = sipsung.getRelationshipType();
        this.eumyangCondition = sipsung.getEumyangCondition();
        this.description = sipsung.getDescription();
        this.personality = sipsung.getPersonality();
        this.fortune = sipsung.getFortune();
    }
}
