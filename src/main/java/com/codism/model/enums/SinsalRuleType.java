package com.codism.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 신살 규칙 타입
 */
@Getter
@RequiredArgsConstructor
public enum SinsalRuleType {

    /**
     * 일간-지지 규칙
     * 예: 갑일에 축지 보면 천을귀인
     */
    ILGAN_JIJI("일간-지지"),

    /**
     * 년지 조합 규칙
     * 예: 년지가 인묘진 중 하나
     */
    YENJI_COMBINATION("년지조합"),

    /**
     * 지지 패턴 규칙
     * 예: 삼합, 육합
     */
    JIJI_PATTERN("지지패턴"),

    /**
     * 일간-년지 규칙
     */
    ILGAN_YENJI("일간-년지"),

    /**
     * 천간-지지 조합 규칙
     */
    CHEONGAN_JIJI("천간-지지"),

    /**
     * 년지를 기준으로 사주 전체에서 특정 지지 찾기
     * 예: 인오술 삼합에 묘가 있으면 도화살
     */
    YENJI_TO_ANY("년지-지지"),

    /**
     * 일주 조합 규칙 (일간 + 일지)
     * 예: 갑진일주, 을사일주
     */
    ILJU_COMBINATION("일주조합"),

    /**
     * 지지 쌍 규칙 (두 지지가 사주 내에 동시에 있는지)
     * 예: 귀문관살 - 진해, 자유, 인미
     */
    JIJI_PAIR("지지쌍"),

    /**
     * 지지-천간 규칙 (지지를 보고 천간을 찾음)
     * 예: 천덕귀인 - 사년(지지)에 병(천간)이 있으면
     */
    JIJI_TO_CHEONGAN("지지-천간");

    private final String description;

    /**
     * 문자열로 Enum 찾기
     */
    public static SinsalRuleType fromString(String ruleType) {
        if (ruleType == null) {
            return null;
        }

        try {
            return SinsalRuleType.valueOf(ruleType);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * DB 문자열로 변환
     */
    public String toDbValue() {
        return this.name();
    }
}
