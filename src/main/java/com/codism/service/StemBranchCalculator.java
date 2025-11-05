package com.codism.service;

import com.codism.model.dto.response.StemBranchInfo;
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
     * 절입일 기준 사주 월 계산 (양력 날짜 기준)
     *
     * @param solarYear 양력 연도
     * @param solarMonth 양력 월
     * @param solarDay 양력 일
     * @return 사주 월 인덱스 (1=인월, 2=묘월, ..., 12=축월)
     */
    public static int getSajuMonthIndex(int solarYear, int solarMonth, int solarDay) {
        // 절입일 기준 (절기는 매년 조금씩 다르므로 대략적인 날짜 사용)
        // 각 절기 이후에 해당 사주 월이 시작됨
        // 입춘(2/4) 이후 = 1월(인월), 경칩(3/6) 이후 = 2월(묘월) 등

        // 12월 (축월): 소한(1/6) ~ 입춘(2/4) 이전
        if (solarMonth == 1 && solarDay >= 6) {
            return 12;  // 소한 이후 = 12월(축월)
        } else if (solarMonth == 2 && solarDay < 4) {
            return 12;  // 입춘 이전 = 12월(축월)
        }
        // 1월 (인월): 입춘(2/4) ~ 경칩(3/6) 이전
        else if (solarMonth == 2 && solarDay >= 4) {
            return 1;   // 입춘 이후 = 1월(인월)
        } else if (solarMonth == 3 && solarDay < 6) {
            return 1;   // 경칩 이전 = 1월(인월)
        }
        // 2월 (묘월): 경칩(3/6) ~ 청명(4/5) 이전
        else if (solarMonth == 3 && solarDay >= 6) {
            return 2;   // 경칩 이후 = 2월(묘월)
        } else if (solarMonth == 4 && solarDay < 5) {
            return 2;   // 청명 이전 = 2월(묘월)
        }
        // 3월 (진월): 청명(4/5) ~ 입하(5/6) 이전
        else if (solarMonth == 4 && solarDay >= 5) {
            return 3;   // 청명 이후 = 3월(진월)
        } else if (solarMonth == 5 && solarDay < 6) {
            return 3;   // 입하 이전 = 3월(진월)
        }
        // 4월 (사월): 입하(5/6) ~ 망종(6/6) 이전
        else if (solarMonth == 5 && solarDay >= 6) {
            return 4;   // 입하 이후 = 4월(사월)
        } else if (solarMonth == 6 && solarDay < 6) {
            return 4;   // 망종 이전 = 4월(사월)
        }
        // 5월 (오월): 망종(6/6) ~ 소서(7/7) 이전
        else if (solarMonth == 6 && solarDay >= 6) {
            return 5;   // 망종 이후 = 5월(오월)
        } else if (solarMonth == 7 && solarDay < 7) {
            return 5;   // 소서 이전 = 5월(오월)
        }
        // 6월 (미월): 소서(7/7) ~ 입추(8/8) 이전
        else if (solarMonth == 7 && solarDay >= 7) {
            return 6;   // 소서 이후 = 6월(미월)
        } else if (solarMonth == 8 && solarDay < 8) {
            return 6;   // 입추 이전 = 6월(미월)
        }
        // 7월 (신월): 입추(8/8) ~ 백로(9/8) 이전
        else if (solarMonth == 8 && solarDay >= 8) {
            return 7;   // 입추 이후 = 7월(신월)
        } else if (solarMonth == 9 && solarDay < 8) {
            return 7;   // 백로 이전 = 7월(신월)
        }
        // 8월 (유월): 백로(9/8) ~ 한로(10/8) 이전
        else if (solarMonth == 9 && solarDay >= 8) {
            return 8;   // 백로 이후 = 8월(유월)
        } else if (solarMonth == 10 && solarDay < 8) {
            return 8;   // 한로 이전 = 8월(유월)
        }
        // 9월 (술월): 한로(10/8) ~ 입동(11/7) 이전
        else if (solarMonth == 10 && solarDay >= 8) {
            return 9;   // 한로 이후 = 9월(술월)
        } else if (solarMonth == 11 && solarDay < 7) {
            return 9;   // 입동 이전 = 9월(술월)
        }
        // 10월 (해월): 입동(11/7) ~ 대설(12/7) 이전
        else if (solarMonth == 11 && solarDay >= 7) {
            return 10;  // 입동 이후 = 10월(해월)
        } else if (solarMonth == 12 && solarDay < 7) {
            return 10;  // 대설 이전 = 10월(해월)
        }
        // 11월 (자월): 대설(12/7) ~ 소한(1/6) 이전
        else if (solarMonth == 12 && solarDay >= 7) {
            return 11;  // 대설 이후 = 11월(자월)
        } else {
            return 11;  // 1월 소한 이전 = 11월(자월)
        }
    }

    /**
     * 절입일 기준 월간지(干支) 계산
     *
     * @param solarYear  양력 연도
     * @param solarMonth 양력 월
     * @param solarDay   양력 일
     * @return 해당 월의 간지 문자열 (예: "기해월")
     */
    public static String getMonthStemBranchBySolarTerm(int solarYear, int solarMonth, int solarDay) {
        // 절입일 기준 사주 월 계산
        int sajuMonth = getSajuMonthIndex(solarYear, solarMonth, solarDay);

        // 년도 결정: 입춘 이전이면 전년도
        int sajuYear = solarYear;
        if (sajuMonth == 12) {
            // 12월(축월)이면 전년도 기준
            sajuYear = solarYear - 1;
        }

        // 년도의 천간(stem)에 따라 월의 천간 결정
        int yearStem = (sajuYear - 4) % 10;
        if (yearStem < 0) yearStem += 10;

        // 월의 천간 계산 - 오주갑자표(五虎遁甲) 규칙 적용
        // 갑/기년: 정월(인월)은 병인(병=2), 을/경년: 무인(무=4), 병/신년: 경인(경=6)
        // 정/임년: 임인(임=8), 무/계년: 갑인(갑=0)
        // 정월(인월) 천간 = (yearStem * 2 + 2) % 10
        // 각 월의 천간 = (정월 천간 + (sajuMonth - 1)) % 10
        int monthStem = (yearStem * 2 + 2 + (sajuMonth - 1)) % 10;

        // 월의 지지 계산 (사주 1월=인(2), 2월=묘(3), ..., 10월=해(11), 11월=자(0), 12월=축(1))
        int monthBranch = (sajuMonth + 1) % 12;

        return CELESTIAL_STEMS[monthStem] + TERRESTRIAL_BRANCHES[monthBranch];
    }

    /**
     * 음력 월의 간지(干支) 계산 (레거시 - 절입일 기준으로 변경 권장)
     *
     * @param lunarYear  음력 연도
     * @param lunarMonth 음력 월 (1-12)
     * @return 해당 월의 간지 문자열 (예: "경자월")
     * @deprecated 사주 계산에는 getMonthStemBranchBySolarTerm 사용 권장
     */
    @Deprecated
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
     * 시간의 간지(干支) 계산 (레거시 메서드)
     *
     * @param hour 시간 (0-23)
     * @param dayStemBranch 해당 일의 간지
     * @return 해당 시간의 간지 문자열 (예: "갑자시")
     */
    public static String getTimeStemBranch(int hour, String dayStemBranch) {
        return getTimeStemBranch(hour, 0, dayStemBranch);
    }

    /**
     * 시간의 간지(干支) 계산
     *
     * @param hour 시간 (0-23)
     * @param minute 분 (0-59)
     * @param dayStemBranch 해당 일의 간지
     * @return 해당 시간의 간지 문자열 (예: "갑자시")
     */
    public static String getTimeStemBranch(int hour, int minute, String dayStemBranch) {
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
        int timeBranchIndex = getTimeBranchIndex(hour, minute);

        // 시간의 천간 계산 (일간에 따라 자시의 천간이 결정됨)
        // 자시부터 시작해서 시간마다 천간이 1씩 증가
        int timeStemIndex = (DAY_TO_TIME_STEM_OFFSET[dayStemIndex] + timeBranchIndex) % 10;

        return CELESTIAL_STEMS[timeStemIndex] + TIME_BRANCHES[timeBranchIndex];
    }

    /**
     * 시간을 지지 인덱스로 변환 (레거시 메서드)
     *
     * @param hour 시간 (0-23)
     * @return 지지 인덱스 (0-11)
     */
    private static int getTimeBranchIndex(int hour) {
        return getTimeBranchIndex(hour, 0);
    }

    /**
     * 시간을 지지 인덱스로 변환 (분까지 고려)
     *
     * @param hour 시간 (0-23)
     * @param minute 분 (0-59)
     * @return 지지 인덱스 (0-11)
     */
    private static int getTimeBranchIndex(int hour, int minute) {
        // 전통 사주에서 각 시(時)는 2시간씩 차지하며, 홀수 시각은 이전 시에 포함됨
        // 자시(23~01), 축시(01~03), 인시(03~05), 묘시(05~07), 진시(07~09), 사시(09~11)
        // 오시(11~13), 미시(13~15), 신시(15~17), 유시(17~19), 술시(19~21), 해시(21~23)

        if (hour == 23 || hour == 0) return 0;  // 자시 (23:00~00:59)
        else if (hour == 1 || hour == 2) return 1;   // 축시 (01:00~02:59)
        else if (hour == 3 || hour == 4) return 2;   // 인시 (03:00~04:59)
        else if (hour == 5 || hour == 6 || hour == 7) return 3;   // 묘시 (05:00~07:59)
        else if (hour == 8 || hour == 9) return 4;   // 진시 (08:00~09:59)
        else if (hour == 10 || hour == 11) return 5;  // 사시 (10:00~11:59)
        else if (hour == 12 || hour == 13) return 6; // 오시 (12:00~13:59)
        else if (hour == 14 || hour == 15) return 7; // 미시 (14:00~15:59)
        else if (hour == 16 || hour == 17) return 8; // 신시 (16:00~17:59)
        else if (hour == 18 || hour == 19) return 9; // 유시 (18:00~19:59)
        else if (hour == 20 || hour == 21) return 10; // 술시 (20:00~21:59)
        else return 11; // 해시 (22:00~22:59)
    }

    /**
     * 시간명 반환 (레거시 메서드)
     *
     * @param hour 시간 (0-23)
     * @return 시간명 (예: "자시", "축시")
     */
    public static String getTimeNameKorean(int hour) {
        return getTimeNameKorean(hour, 0);
    }

    /**
     * 시간명 반환
     *
     * @param hour 시간 (0-23)
     * @param minute 분 (0-59)
     * @return 시간명 (예: "자시", "축시")
     */
    public static String getTimeNameKorean(int hour, int minute) {
        int branchIndex = getTimeBranchIndex(hour, minute);
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
        Integer minute = null;
        if (birthTime != null && !birthTime.trim().isEmpty()) {
            try {
                // HH:mm 형식 또는 HH 형식 모두 지원
                String[] timeParts = birthTime.trim().split(":");
                hour = Integer.parseInt(timeParts[0]);
                if (timeParts.length > 1) {
                    minute = Integer.parseInt(timeParts[1]);
                } else {
                    minute = 0; // 분이 없으면 0으로 처리
                }

                // 시간 범위 검증
                if (hour < 0 || hour > 23) {
                    throw new IllegalArgumentException("시간은 0~23 범위여야 합니다: " + hour);
                }
                if (minute < 0 || minute > 59) {
                    throw new IllegalArgumentException("분은 0~59 범위여야 합니다: " + minute);
                }

                log.info("출생시간 파싱 완료 - hour: {}시 {}분 ({})", hour, minute, getTimeNameKorean(hour, minute));
            } catch (NumberFormatException e) {
                log.warn("잘못된 시간 형식입니다. 시간 계산을 건너뛰겠습니다: {}", birthTime);
                hour = null;
                minute = null;
            }
        }

        return getAllStemBranch(year, month, day, hour, minute, isSolarCalendar);
    }

    /**
     * 양력 날짜에 대한 모든 간지(년월일) 정보 반환 (기존 메서드 유지)
     */
    public static StemBranchInfo getAllStemBranch(int solarYear, int solarMonth, int solarDay, boolean isSolarCalendar) {
        return getAllStemBranch(solarYear, solarMonth, solarDay, null, null, isSolarCalendar);
    }

    /**
     * 양력 날짜와 시간에 대한 모든 간지(년월일시) 정보 반환 (레거시 메서드)
     */
    public static StemBranchInfo getAllStemBranch(int solarYear, int solarMonth, int solarDay, Integer hour, boolean isSolarCalendar) {
        return getAllStemBranch(solarYear, solarMonth, solarDay, hour, 0, isSolarCalendar);
    }

    /**
     * 양력 날짜와 시간에 대한 모든 간지(년월일시) 정보 반환
     *
     * @param solarYear 양력 연도
     * @param solarMonth 양력 월 (1-12)
     * @param solarDay 양력 일
     * @param hour 시간 (0-23), null이면 시간 간지는 계산하지 않음
     * @param minute 분 (0-59), null이면 0으로 처리
     * @param isSolarCalendar 양력 기준 여부 (true: 양력, false: 음력)
     * @return 년월일시 간지 정보가 담긴 StemBranchInfo 객체
     */
    public static StemBranchInfo getAllStemBranch(int solarYear, int solarMonth, int solarDay, Integer hour, Integer minute, boolean isSolarCalendar) {
        // 양력 날짜를 음력으로 변환 (사주는 항상 음력 기준)
        LunarCalendarService.LunarDate lunarDate = LunarCalendarService.solarToLunar(solarYear, solarMonth, solarDay);

        int lunarYear = lunarDate.getYear();
        int lunarMonth = lunarDate.getMonth();

        // 음력 변환 결과 로깅
        log.info("양력 {}/{}/{} → 음력 {}/{}/{}", solarYear, solarMonth, solarDay, lunarYear, lunarMonth, lunarDate.getDay());

        // 년월일 간지 계산
        // 년: 입춘 기준 (입춘 이전은 전년도)
        // 월: 절입일 기준 (24절기 중 입춘, 경칩 등 12개 절기 기준)
        // 일: 양력 기준
        String monthStemBranch = getMonthStemBranchBySolarTerm(solarYear, solarMonth, solarDay);
        int sajuMonth = getSajuMonthIndex(solarYear, solarMonth, solarDay);

        // 사주 년도 결정: 입춘 이전(12월 축월)이면 양력 전년도
        int sajuYear = solarYear;
        if (sajuMonth == 12) {
            sajuYear = solarYear - 1;
        }

        String yearStemBranch = getYearStemBranch(sajuYear);
        String dayStemBranch = getDayStemBranch(solarYear, solarMonth, solarDay);

        log.info("절입일 기준 - 사주년: {}, 사주월: {}월({})", sajuYear, sajuMonth, monthStemBranch);

        // 시간 간지 계산 (시간이 제공된 경우)
        String timeStemBranch = null;
        if (hour != null) {
            int actualMinute = (minute != null) ? minute : 0;
            timeStemBranch = getTimeStemBranch(hour, actualMinute, dayStemBranch);
        }

        return new StemBranchInfo(yearStemBranch, monthStemBranch, dayStemBranch, timeStemBranch);
    }
}