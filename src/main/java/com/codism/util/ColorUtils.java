package com.codism.util;

/**
 * 색상 관련 유틸리티 클래스
 */
public class ColorUtils {

    /**
     * 오행 색상을 형용사로 변환
     * @param color 색상명 (청색, 적색, 황색, 백색, 흑색 등)
     * @return 형용사 형태 (푸른, 붉은, 누런, 흰, 검은)
     */
    public static String convertColorToAdjective(String color) {
        if (color == null) {
            return "";
        }

        return switch (color) {
            case "청색", "녹색" -> "푸른";
            case "적색", "빨강" -> "붉은";
            case "황색", "노랑" -> "누런";
            case "금색", "황금", "황금색" -> "황금빛";
            case "백색", "흰색" -> "흰";
            case "흑색", "검정" -> "검은";
            default -> color; // 매칭되지 않으면 원본 반환
        };
    }

    /**
     * 오행별 기본 색상 반환
     * @param ohang 오행 (목, 화, 토, 금, 수)
     * @return 색상 형용사
     */
    public static String getAdjectiveByOhang(String ohang) {
        if (ohang == null) {
            return "";
        }

        return switch (ohang) {
            case "목" -> "푸른";
            case "화" -> "붉은";
            case "토" -> "누런";
            case "금" -> "흰";
            case "수" -> "검은";
            default -> "";
        };
    }
}
