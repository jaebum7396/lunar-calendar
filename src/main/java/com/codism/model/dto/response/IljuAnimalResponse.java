package com.codism.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 일주 동물 조회 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일주 동물 정보")
public class IljuAnimalResponse {

    @Schema(description = "간지명 (예: 임신)", example = "임신")
    private String ganjiName;

    @Schema(description = "천간 (예: 임)", example = "임")
    private String cheongan;

    @Schema(description = "지지 (예: 신)", example = "신")
    private String jiji;

    @Schema(description = "특징/성격 형용사 (예: 센스 있는)", example = "센스 있는")
    private String characteristic;

    @Schema(description = "색상 형용사 (예: 검은)", example = "검은")
    private String colorAdjective;

    @Schema(description = "동물 (예: 원숭이)", example = "원숭이")
    private String animal;

    @Schema(description = "전체 조합 (예: 센스 있는 검은 원숭이)", example = "센스 있는 검은 원숭이")
    private String fullCharacteristic;
}
