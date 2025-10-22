package com.codism.service;

import com.github.usingsky.calendar.KoreanLunarCalendar;
import java.util.Calendar;

/**
 * 만세력(음력) 계산을 위한 Java API
 * 음력-양력 변환 및 다양한 달력 관련 기능을 제공합니다.
 * KoreanLunarCalendar 라이브러리 사용 (한국천문연구원 데이터 기반)
 *
 * @version 2.0
 */
public class LunarCalendarService {

    // 윤달 정보와 음력 데이터를 저장하는 배열
    // 1900년부터 2050년까지의 데이터 포함
    private static final long[] LUNAR_INFO = {
            // 1900년대 (1900-1909)
            0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
            // 1910년대 (1910-1919)
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
            // 1920년대 (1920-1929)
            0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            // 1930년대 (1930-1939)
            0x06566, 0x0d4a0, 0x0ea50, 0x16a95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
            // 1940년대 (1940-1949)
            0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
            // 1950년대 (1950-1959)
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5b0, 0x14573, 0x052b0, 0x0a9a8, 0x0e950, 0x06aa0,
            // 1960년대 (1960-1969)
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
            // 1970년대 (1970-1979)
            0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b6a0, 0x195a6,
            // 1980년대 (1980-1989)
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
            // 1990년대 (1990-1999)
            0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x05ac0, 0x0ab60, 0x096d5, 0x092e0,
            // 2000년대 (2000-2009)
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
            // 2010년대 (2010-2019)
            0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
            // 2020년대 (2020-2029)
            0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
            // 2030년대 (2030-2039)
            0x05aa0, 0x076a3, 0x096d0, 0x04afb, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
            // 2040년대 (2040-2049)
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0,
            // 2050년
            0x14b63
    };

    // 음력의 1~12월의 날짜 수 (평달)
    private static final int[] LUNAR_MONTH_DAYS = {29, 30, 29, 30, 29, 30, 29, 30, 29, 30, 29, 30};

    // 천간(天干)
    private static final String[] CELESTIAL_STEMS = {
            "갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"
    };

    // 지지(地支)
    private static final String[] TERRESTRIAL_BRANCHES = {
            "자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"
    };

    // 십이지(十二支)
    private static final String[] ZODIAC_ANIMALS = {
            "쥐", "소", "호랑이", "토끼", "용", "뱀", "말", "양", "원숭이", "닭", "개", "돼지"
    };

    /**
     * 양력 날짜를 음력으로 변환 (KoreanLunarCalendar 라이브러리 사용)
     *
     * @param year  양력 연도
     * @param month 양력 월 (1-12)
     * @param day   양력 일
     * @return 음력 날짜를 나타내는 LunarDate 객체
     */
    public static LunarDate solarToLunar(int year, int month, int day) {
        try {
            KoreanLunarCalendar calendar = KoreanLunarCalendar.getInstance();
            calendar.setSolarDate(year, month, day);

            // 음력 정보 추출 - getLunarIsoFormat()은 "YYYY-MM-DD" 형식 반환
            String lunarIso = calendar.getLunarIsoFormat();
            String[] parts = lunarIso.split("-");

            int lunarYear = Integer.parseInt(parts[0]);
            int lunarMonth = Integer.parseInt(parts[1]);
            int lunarDay = Integer.parseInt(parts[2]);

            // 윤달 여부는 ISO 형식에서 확인 (intercalation 필드)
            boolean isLeapMonth = calendar.isIntercalation();

            return new LunarDate(lunarYear, lunarMonth, lunarDay, isLeapMonth);
        } catch (Exception e) {
            throw new IllegalArgumentException("음력 변환 실패: " + year + "/" + month + "/" + day, e);
        }
    }

    /**
     * 음력 날짜를 양력으로 변환
     *
     * @param lunarYear   음력 연도
     * @param lunarMonth  음력 월 (1-12)
     * @param lunarDay    음력 일
     * @param isLeapMonth 윤달 여부
     * @return 양력 날짜를 나타내는 Calendar 객체
     */
    public static Calendar lunarToSolar(int lunarYear, int lunarMonth, int lunarDay, boolean isLeapMonth) {
        // 지원 가능한 연도 범위 확인
        if (lunarYear < 1900 || lunarYear > 2050) {
            throw new IllegalArgumentException("지원하는 연도 범위는 1900년에서 2050년까지입니다: " + lunarYear);
        }

        // 기준일자 (1900년 1월 31일은 음력 1900년 1월 1일)
        Calendar baseDate = Calendar.getInstance();
        baseDate.set(1900, 0, 31);

        // 1900년 1월 1일부터 입력된 음력 날짜까지의 총 일수 계산
        long days = 0;

        // 연도별 일수 더하기
        for (int year = 1900; year < lunarYear; year++) {
            days += getLunarYearDays(year);
        }

        // 해당 연도의 월별 일수
        int[] monthDays = getLunarMonthDays(lunarYear);
        int leapMonth = getLeapMonth(lunarYear);

        // 월별 일수 더하기
        for (int month = 0; month < lunarMonth - 1; month++) {
            days += monthDays[month];
        }

        // 윤달인 경우 추가 계산
        if (isLeapMonth && leapMonth == lunarMonth) {
            days += monthDays[lunarMonth - 1];
        }

        // 일자 더하기
        days += lunarDay - 1;

        // 기준 날짜에 일수를 더해 양력 날짜 계산
        Calendar result = Calendar.getInstance();
        result.setTimeInMillis(baseDate.getTimeInMillis() + days * 24 * 60 * 60 * 1000);

        return result;
    }

    /**
     * 음력 연도의 총 일수 계산
     *
     * @param year 음력 연도
     * @return 해당 연도의 총 일수
     */
    private static int getLunarYearDays(int year) {
        int offset = year - 1900;
        if (offset < 0 || offset >= LUNAR_INFO.length) {
            throw new IllegalArgumentException("지원되지 않는 연도입니다: " + year);
        }

        int days = 0;
        int leapMonth = getLeapMonth(year);

        // 1~12월의 일수 합산
        for (int i = 0; i < 12; i++) {
            int daysInMonth = (LUNAR_INFO[offset] & (0x10000 >> i)) == 0 ? 29 : 30;
            days += daysInMonth;
        }

        // 윤달이 있는 경우 윤달의 일수 추가
        if (leapMonth > 0) {
            days += (LUNAR_INFO[offset] & (0x10000 >> (leapMonth - 1))) == 0 ? 29 : 30;
        }

        return days;
    }

    /**
     * 음력 연도의 윤달 월 반환 (없으면 0)
     *
     * @param year 음력 연도
     * @return 윤달의 월 (1-12), 윤달이 없으면 0
     */
    private static int getLeapMonth(int year) {
        int offset = year - 1900;
        if (offset < 0 || offset >= LUNAR_INFO.length) {
            return 0;
        }
        return (int) (LUNAR_INFO[offset] & 0xf);
    }

    /**
     * 음력 연도의 각 월별 일수 계산
     *
     * @param year 음력 연도
     * @return 각 월의 일수 배열 (윤달 포함)
     */
    private static int[] getLunarMonthDays(int year) {
        int offset = year - 1900;
        if (offset < 0 || offset >= LUNAR_INFO.length) {
            throw new IllegalArgumentException("지원되지 않는 연도입니다: " + year);
        }

        int leapMonth = getLeapMonth(year);
        int monthCount = leapMonth > 0 ? 13 : 12;
        int[] monthDays = new int[monthCount];

        for (int i = 0; i < 12; i++) {
            monthDays[i] = (LUNAR_INFO[offset] & (0x10000 >> i)) == 0 ? 29 : 30;
        }

        if (leapMonth > 0) {
            // 윤달의 일수 계산
            monthDays[12] = (LUNAR_INFO[offset] & (0x10000 >> (leapMonth - 1))) == 0 ? 29 : 30;

            // 윤달 위치에 삽입
            for (int i = 12; i > leapMonth; i--) {
                monthDays[i] = monthDays[i - 1];
            }
        }

        return monthDays;
    }

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

        return CELESTIAL_STEMS[celestialStem] + TERRESTRIAL_BRANCHES[terrestrialBranch] + "년";
    }

    /**
     * 음력 년도의 십이지 동물 계산
     *
     * @param lunarYear 음력 연도
     * @return 해당 연도의 십이지 동물 문자열 (예: "쥐")
     */
    public static String getYearZodiacAnimal(int lunarYear) {
        int index = (lunarYear - 4) % 12;
        if (index < 0) index += 12;
        return ZODIAC_ANIMALS[index];
    }

    /**
     * 특정 날짜의 절기(24절기) 계산
     *
     * @param year  양력 연도
     * @param month 양력 월 (1-12)
     * @param day   양력 일
     * @return 절기 이름, 해당 날짜가 절기가 아니면 null
     */
    public static String getSolarTerm(int year, int month, int day) {
        // 절기 계산은 복잡한 천문학적 계산이 필요하므로
        // 이 예제에서는 간단한 구현만 제공합니다.
        // 실제 구현에서는 절기 테이블을 사용하거나 천문학적 계산이 필요합니다.

        // 24절기 이름
        final String[] SOLAR_TERMS = {
                "소한", "대한", "입춘", "우수", "경칩", "춘분",
                "청명", "곡우", "입하", "소만", "망종", "하지",
                "소서", "대서", "입추", "처서", "백로", "추분",
                "한로", "상강", "입동", "소설", "대설", "동지"
        };

        // 단순화된 구현 - 실제로는 더 정확한 계산이 필요합니다
        int termIndex = -1;

        // 각 월의 특정 날짜에 해당하는 절기 (매우 간략화된 예시)
        if (day == 6 || day == 21) {
            termIndex = (month - 1) * 2;
            if (day == 21) {
                termIndex += 1;
            }
        }

        return (termIndex >= 0 && termIndex < SOLAR_TERMS.length) ? SOLAR_TERMS[termIndex] : null;
    }

    /**
     * 음력 날짜 클래스
     */
    public static class LunarDate {
        private int year;
        private int month;
        private int day;
        private boolean isLeapMonth;

        public LunarDate(int year, int month, int day, boolean isLeapMonth) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.isLeapMonth = isLeapMonth;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public boolean isLeapMonth() {
            return isLeapMonth;
        }

        @Override
        public String toString() {
            String leapMonthStr = isLeapMonth ? "윤" : "";
            return year + "년 " + leapMonthStr + month + "월 " + day + "일";
        }

        /**
         * 간지(干支) 표기 포함한 문자열 반환
         */
        public String toFullString() {
            String stemBranch = LunarCalendarService.getYearStemBranch(year);
            String zodiac = LunarCalendarService.getYearZodiacAnimal(year);
            String leapMonthStr = isLeapMonth ? "윤" : "";

            return year + "년(" + stemBranch + ", " + zodiac + "해) " +
                    leapMonthStr + month + "월 " + day + "일";
        }
    }
}