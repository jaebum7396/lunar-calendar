package com.codism.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 간단한 사주 정보 조회 응답 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "간단한 사주 정보")
public class SimpleSajuResponse {

    @Schema(description = "생년월일", example = "1989-06-05")
    private String birthDate;

    @Schema(description = "출생 시간", example = "07:00")
    private String birthTime;

    @Schema(description = "나이", example = "36")
    private int age;

    @Schema(description = "성별 (M: 남성, F: 여성)", example = "M")
    private String gender;

    @Schema(description = "양력 여부", example = "true")
    private boolean solarCalendar;

    @Schema(description = "년주 (연간, 연지)")
    private PillarInfo year;

    @Schema(description = "월주 (월간, 월지)")
    private PillarInfo month;

    @Schema(description = "일주 (일간, 일지)")
    private PillarInfo day;

    @Schema(description = "시주 (시간, 시지)")
    private PillarInfo hour;

    @Schema(description = "신살 목록", example = "[\"천덕귀인\", \"문창귀인\", \"도화살\", \"역마살\"]")
    private List<String> sinsalList;

    @Schema(description = "일주 동물 특성", example = "창의적인 붉은 원숭이")
    private String fullCharacteristic;

    /**
     * 천간지지 정보
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "천간지지 정보")
    public static class PillarInfo {

        @Schema(description = "천간", example = "기")
        private String cheongan;

        @Schema(description = "지지", example = "사")
        private String jiji;
    }
}
