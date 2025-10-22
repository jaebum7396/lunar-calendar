package com.codism.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 신살 유형 (길신/흉신)
 */
@Getter
@RequiredArgsConstructor
public enum SinsalType {

    GILSIN("길신", "吉神"),
    HYUNGSIN("흉신", "凶神");

    private final String korean;   // 한글명
    private final String hanja;    // 한자명

    /**
     * 한글명으로 Enum 찾기
     */
    public static SinsalType fromKorean(String korean) {
        for (SinsalType type : values()) {
            if (type.korean.equals(korean)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid sinsal type: " + korean);
    }

    /**
     * DB 문자열로 변환 (길신/흉신)
     */
    public String toDbValue() {
        return korean;
    }
}
