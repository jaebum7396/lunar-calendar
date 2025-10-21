package com.codism.model.dto;

import com.codism.model.entity.SinsalMaster;
import com.codism.model.enums.SinsalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * 사주 상세 정보 응답 DTO
 * 천간지지, 십성, 신살 등 전체 사주 정보 포함
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SajuDetailResponse {

    // 기본 정보
    private LocalDate birthDate;
    private String birthTime;
    private boolean isSolarCalendar;
    private int age;

    // 사주 팔자
    private SajuPillar year;    // 연주
    private SajuPillar month;   // 월주
    private SajuPillar day;     // 일주
    private SajuPillar hour;    // 시주

    // 대운, 세운
    private SajuPillar daeun;   // 대운
    private SajuPillar seun;    // 세운

    // 신살
    private List<SinsalInfo> sinsalList;

    /**
     * 사주 기둥 (연월일시 각각)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SajuPillar {
        private String cheongan;        // 천간 (한글: 갑, 을, 병...)
        private String cheonganHanja;   // 천간 (한자: 甲, 乙, 丙...)
        private String cheonganColor;   // 천간 색상 (한글: 초록, 파랑...)
        private String cheonganColorHex; // 천간 색상 (HEX: #7FB77E)
        private String jiji;            // 지지 (한글: 자, 축, 인...)
        private String jijiHanja;       // 지지 (한자: 子, 丑, 寅...)
        private String jijiColor;       // 지지 색상 (한글)
        private String jijiColorHex;    // 지지 색상 (HEX)
        private String sipsung;         // 십성 (비견, 겁재, 식신...)
    }

    /**
     * 신살 정보 (간략 버전 - 클릭 시 별도 API로 상세 조회)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SinsalInfo {
        private String sinsalName;          // 신살 이름 (천을귀인, 망신살...)
        private SinsalType sinsalType;      // 길신/흉신 (Enum)
        private String icon;                // 아이콘 (☀️, ⚠️...)

        /**
         * Entity로부터 DTO 생성
         */
        public SinsalInfo(SinsalMaster sinsal) {
            this.sinsalName = sinsal.getSinsalName();
            this.sinsalType = sinsal.getSinsalType();
            this.icon = sinsal.getIcon();
        }
    }
}
