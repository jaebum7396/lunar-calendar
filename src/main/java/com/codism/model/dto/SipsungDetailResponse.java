package com.codism.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 십성 상세 정보 응답 DTO
 */
@Data
@Builder
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
}
