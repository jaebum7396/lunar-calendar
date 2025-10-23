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
    private SajuPillar daeun;   // 현재 대운
    private SajuPillar seun;    // 현재 세운
    private SajuPillar wolun;   // 현재 월운

    // 대운 타임라인 (전체 대운 목록)
    private List<DaeunInfo> daeunList;

    // 합충형파해 분석
    private HapChungAnalysis hapChungAnalysis;

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
        private String cheonganSipsung; // 천간십성 (비견, 겁재, 식신...)
        private String jijiSipsung;     // 지지십성 (비견, 겁재, 식신...)
        private Integer year;           // 년도 (세운용, 예: 2025)
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

    /**
     * 대운 정보 (타임라인용)
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DaeunInfo {
        private int startAge;               // 시작 나이
        private int endAge;                 // 종료 나이
        private String cheongan;            // 천간
        private String cheonganHanja;       // 천간 한자
        private String jiji;                // 지지
        private String jijiHanja;           // 지지 한자
        private String cheonganSipsung;     // 천간 십성
        private String jijiSipsung;         // 지지 십성
        private boolean isCurrent;          // 현재 대운 여부
        private String interpretation;      // 간단 해석 (예: "정인운 - 학업과 안정")
        private Integer yearsRemaining;     // 현재 대운 남은 년수 (현재 대운이 아니면 null)
        private String periodInfo;          // 기간 정보 (예: "전반 5년 천간 영향, 후반 5년 지지 영향")
    }

    /**
     * 합충형파해 분석 결과
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HapChungAnalysis {
        private List<String> hapList;       // 합 (예: "일간-월간 천간합")
        private List<String> chungList;     // 충 (예: "년지-일지 육충")
        private List<String> hyeongList;    // 형 (예: "월지-시지 삼형")
        private List<String> paList;        // 파 (예: "년지-일지 육파")
        private List<String> haeList;       // 해 (예: "일지-시지 육해")
        private List<String> sajuToDaeun;   // 사주-대운 작용
        private List<String> sajuToSeun;    // 사주-세운 작용
        private List<String> sajuToWolun;   // 사주-월운 작용
    }
}
