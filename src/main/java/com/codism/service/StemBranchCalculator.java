package com.codism.service;

import com.codism.model.dto.StemBranchInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * 천간지지(간지) 계산을 위한 유틸리티 클래스
 * 년, 월, 일, 시에 대한 간지 계산 기능 제공 (사주팔자)
 */
@Slf4j
@Service
public class StemBranchCalculator {

    // 천간(天干)
    private static final String[] CELESTIAL_STEMS = {
            "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
    };

    // 지지(地支)
    private static final String[] TERRESTRIAL_BRANCHES = {
            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
    };

    // 시간별 지지 매핑 (자시: 23~01시, 축시: 01~03시, ...)
    private static final String[] TIME_BRANCHES = {
            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
    };

    // 일간별 자시 천간 대응표 (갑일->갑자시, 을일->병자시, ...)
    private static final int[] DAY_TO_TIME_STEM_OFFSET = {
            0, 2, 4, 6, 8, 0, 2, 4, 6, 8  // 갑, 을, 병, 정, 무, 기, 경, 신, 임, 계
    };

    /**
     * 음력 년도의 간지(干支) 계산
     *
     * @param lunarYear 음력 연도
     * @return 해당 연도의 간지 문자열 (예: "경자년")
     */
    public static String getYearStemBranch(int lunarYear) {
        int celestialStem = (lunarYear - 4) % 10;
        int terrestrialBranch = (lunarYear - 4) % 12;

        if (celestialStem < 0) celestialStem += 10;
        if (terrestrialBranch < 0) terrestrialBranch += 12;

        return CELESTIAL_STEMS[celestialStem] + TERRESTRIAL_BRANCHES[terrestrialBranch];
    }

    /**
     * 음력 월의 간지(干支) 계산
     *
     * @param lunarYear  음력 연도
     * @param lunarMonth 음력 월 (1-12)
     * @return 해당 월의 간지 문자열 (예: "경자월")
     */
    public static String getMonthStemBranch(int lunarYear, int lunarMonth) {
        // 년도의 천간(stem)에 따라 월의 천간 결정
        int yearStem = (lunarYear - 4) % 10;
        if (yearStem < 0) yearStem += 10;

        // 월의 천간 계산 - 년의 천간에 따라 시작하는 월의 천간이 결정됨
        int monthStem = (yearStem * 2 + lunarMonth) % 10;
        if (monthStem == 0) monthStem = 10;
        monthStem = (monthStem - 1) % 10;

        // 월의 지지 계산 (음력 1월=인, 2월=묘, ..., 10월=해, 11월=자, 12월=축)
        int monthBranch = (lunarMonth + 1) % 12;

        return CELESTIAL_STEMS[monthStem] + TERRESTRIAL_BRANCHES[monthBranch];
    }

    /**
     * 음력 일의 간지(干支) 계산
     *
     * @param solarYear  양력 연도
     * @param solarMonth 양력 월 (1-12)
     * @param solarDay   양력 일
     * @return 해당 일의 간지 문자열 (예: "경자일")
     */
    public static String getDayStemBranch(int solarYear, int solarMonth, int solarDay) {
        // 기준일자: 1900년 1월 1일은 "을해일" (乙亥)
        Calendar baseDate = Calendar.getInstance();
        baseDate.set(1900, 0, 1); // 1900년 1월 1일

        // 기준일자의 간지 인덱스
        int baseStem = 1; // "을"의 인덱스
        int baseBranch = 11; // "해"의 인덱스

        // 입력일자
        Calendar targetDate = Calendar.getInstance();
        targetDate.set(solarYear, solarMonth - 1, solarDay);

        // 두 날짜 사이의 일수 계산
        long diffDays = (targetDate.getTimeInMillis() - baseDate.getTimeInMillis()) / (24 * 60 * 60 * 1000);

        // 간지 계산
        int stemIndex = (int) ((baseStem + diffDays) % 10);
        int branchIndex = (int) ((baseBranch + diffDays) % 12);

        if (stemIndex < 0) stemIndex += 10;
        if (branchIndex < 0) branchIndex += 12;

        return CELESTIAL_STEMS[stemIndex] + TERRESTRIAL_BRANCHES[branchIndex];
    }

    /**
     * 시간의 간지(干支) 계산
     *
     * @param hour 시간 (0-23)
     * @param dayStemBranch 해당 일의 간지
     * @return 해당 시간의 간지 문자열 (예: "갑자시")
     */
    public static String getTimeStemBranch(int hour, String dayStemBranch) {
        // 일간(일의 천간) 추출
        String dayStemStr = dayStemBranch.substring(0, 1);
        int dayStemIndex = -1;
        for (int i = 0; i < CELESTIAL_STEMS.length; i++) {
            if (CELESTIAL_STEMS[i].equals(dayStemStr)) {
                dayStemIndex = i;
                break;
            }
        }

        if (dayStemIndex == -1) {
            throw new IllegalArgumentException("유효하지 않은 일간입니다: " + dayStemStr);
        }

        // 시지(시간의 지지) 계산
        int timeBranchIndex = getTimeBranchIndex(hour);

        // 시간의 천간 계산 (일간에 따라 자시의 천간이 결정됨)
        // 자시부터 시작해서 시간마다 천간이 1씩 증가
        int timeStemIndex = (DAY_TO_TIME_STEM_OFFSET[dayStemIndex] + timeBranchIndex) % 10;

        return CELESTIAL_STEMS[timeStemIndex] + TIME_BRANCHES[timeBranchIndex];
    }

    /**
     * 시간을 지지 인덱스로 변환
     *
     * @param hour 시간 (0-23)
     * @return 지지 인덱스 (0-11)
     */
    private static int getTimeBranchIndex(int hour) {
        // 전통 사주에서 정시(X:00)는 이전 시에 포함됨
        // 예: 05:00~06:59 = 묘시, 07:00~08:59 = 진시
        // 하지만 일부 유파에서는 07:00을 묘시에 포함시키므로 < 7로 처리
        if (hour == 23 || hour == 0) return 0;  // 자시 (23:00~00:59)
        else if (hour >= 1 && hour < 3) return 1;   // 축시 (01:00~02:59)
        else if (hour >= 3 && hour < 5) return 2;   // 인시 (03:00~04:59)
        else if (hour >= 5 && hour < 7) return 3;   // 묘시 (05:00~06:59)
        else if (hour >= 7 && hour < 9) return 4;   // 진시 (07:00~08:59)
        else if (hour >= 9 && hour < 11) return 5;  // 사시 (09:00~10:59)
        else if (hour >= 11 && hour < 13) return 6; // 오시 (11:00~12:59)
        else if (hour >= 13 && hour < 15) return 7; // 미시 (13:00~14:59)
        else if (hour >= 15 && hour < 17) return 8; // 신시 (15:00~16:59)
        else if (hour >= 17 && hour < 19) return 9; // 유시 (17:00~18:59)
        else if (hour >= 19 && hour < 21) return 10; // 술시 (19:00~20:59)
        else return 11; // 해시 (21:00~22:59)
    }

    /**
     * 시간명 반환
     *
     * @param hour 시간 (0-23)
     * @return 시간명 (예: "자시", "축시")
     */
    public static String getTimeNameKorean(int hour) {
        int branchIndex = getTimeBranchIndex(hour);
        return TIME_BRANCHES[branchIndex] + "시";
    }

    /**
     * 문자열 날짜와 시간으로 모든 간지 정보 계산
     */
    public StemBranchInfo getAllStemBranch(String birthDate, boolean isSolarCalendar) {
        return getAllStemBranch(birthDate, null, isSolarCalendar);
    }

    /**
     * 문자열 날짜와 시간으로 모든 간지 정보 계산 (시간 포함)
     */
    public StemBranchInfo getAllStemBranch(String birthDate, String birthTime, boolean isSolarCalendar) {
        log.info("getAllStemBranch - birthDate: {}, birthTime: {}", birthDate, birthTime);

        String[] dateParts = birthDate.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        Integer hour = null;
        if (birthTime != null && !birthTime.trim().isEmpty()) {
            try {
                // HH:mm 형식 또는 HH 형식 모두 지원
                String[] timeParts = birthTime.trim().split(":");
                hour = Integer.parseInt(timeParts[0]);

                // 시간 범위 검증
                if (hour < 0 || hour > 23) {
                    throw new IllegalArgumentException("시간은 0~23 범위여야 합니다: " + hour);
                }

                log.info("출생시간 파싱 완료 - hour: {}시 ({})", hour, getTimeNameKorean(hour));
            } catch (NumberFormatException e) {
                log.warn("잘못된 시간 형식입니다. 시간 계산을 건너뛰겠습니다: {}", birthTime);
                hour = null;
            }
        }

        return getAllStemBranch(year, month, day, hour, isSolarCalendar);
    }

    /**
     * 양력 날짜에 대한 모든 간지(년월일) 정보 반환 (기존 메서드 유지)
     */
    public static StemBranchInfo getAllStemBranch(int solarYear, int solarMonth, int solarDay, boolean isSolarCalendar) {
        return getAllStemBranch(solarYear, solarMonth, solarDay, null, isSolarCalendar);
    }

    /**
     * 양력 날짜와 시간에 대한 모든 간지(년월일시) 정보 반환
     *
     * @param solarYear 양력 연도
     * @param solarMonth 양력 월 (1-12)
     * @param solarDay 양력 일
     * @param hour 시간 (0-23), null이면 시간 간지는 계산하지 않음
     * @param isSolarCalendar 양력 기준 여부 (true: 양력, false: 음력)
     * @return 년월일시 간지 정보가 담긴 StemBranchInfo 객체
     */
    public static StemBranchInfo getAllStemBranch(int solarYear, int solarMonth, int solarDay, Integer hour, boolean isSolarCalendar) {
        // 양력 날짜를 음력으로 변환 (사주는 항상 음력 기준)
        LunarCalendarService.LunarDate lunarDate = LunarCalendarService.solarToLunar(solarYear, solarMonth, solarDay);

        int lunarYear = lunarDate.getYear();
        int lunarMonth = lunarDate.getMonth();

        // 음력 변환 결과 로깅
        log.info("양력 {}/{}/{} → 음력 {}/{}/{}", solarYear, solarMonth, solarDay, lunarYear, lunarMonth, lunarDate.getDay());

        // 년월일 간지 계산 (년월은 음력 기준, 일은 양력 기준)
        String yearStemBranch = getYearStemBranch(lunarYear);
        String monthStemBranch = getMonthStemBranch(lunarYear, lunarMonth);
        String dayStemBranch = getDayStemBranch(solarYear, solarMonth, solarDay);

        // 시간 간지 계산 (시간이 제공된 경우)
        String timeStemBranch = null;
        if (hour != null) {
            timeStemBranch = getTimeStemBranch(hour, dayStemBranch);
        }

        return new StemBranchInfo(yearStemBranch, monthStemBranch, dayStemBranch, timeStemBranch);
    }
}