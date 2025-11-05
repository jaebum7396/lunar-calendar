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
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "일주 동물 정보")
public class IljuAnimalResponse {

    @Schema(description = "전체 조합 (예: 센스 있는 검은 원숭이)", example = "센스 있는 검은 원숭이")
    private String fullCharacteristic;
}
