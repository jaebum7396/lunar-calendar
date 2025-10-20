package com.codism.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 신살 카테고리 (귀인/살성)
 */
@Getter
@RequiredArgsConstructor
public enum SinsalCategory {

    GWIIN("귀인", "貴人"),      // 귀인 (길한 기운)
    SALSUNG("살성", "殺星");    // 살성 (흉한 기운)

    private final String korean;   // 한글명
    private final String hanja;    // 한자명

    /**
     * 한글명으로 Enum 찾기
     */
    public static SinsalCategory fromKorean(String korean) {
        for (SinsalCategory category : values()) {
            if (category.korean.equals(korean)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid sinsal category: " + korean);
    }

    /**
     * DB 문자열로 변환 (귀인/살성)
     */
    public String toDbValue() {
        return korean;
    }
}
