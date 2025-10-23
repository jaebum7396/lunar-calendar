package com.codism.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

/**
 * 대운(大運) 계산 서비스
 *
 * 대운은 10년 주기로 바뀌는 운세를 의미함
 * - 양남/음녀: 순행 (월주에서 다음 간지로 진행)
 * - 음남/양녀: 역행 (월주에서 이전 간지로 진행)
 */
@Slf4j
@Component
public class DaeunCalculator {

    // 천간 순서 (순환)
    private static final String[] CHEONGAN = {"갑", "을", "병", "정", "무", "기", "경", "신", "임", "계"};

    // 지지 순서 (순환)
    private static final String[] JIJI = {"자", "축", "인", "묘", "진", "사", "오", "미", "신", "유", "술", "해"};

    /**
     * 현재 나이에 해당하는 대운 계산
     *
     * @param birthDate 생년월일
     * @param gender 성별 ("M" or "F")
     * @param yearCheongan 년주 천간
     * @param monthCheongan 월주 천간
     * @param monthJiji 월주 지지
     * @return 대운 천간지지 (예: "갑자")
     */
    public String calculateDaeun(LocalDate birthDate, String gender,
                                  String yearCheongan, String monthCheongan, String monthJiji) {

        // 1. 현재 나이 계산
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        // 2. 대운 시작 나이 계산 (간단히 3세부터 시작으로 가정)
        int daeunStartAge = 3;

        // 3. 현재 몇 번째 대운인지 계산 (10년 주기)
        int daeunIndex = (age - daeunStartAge) / 10;
        if (daeunIndex < 0) daeunIndex = 0; // 대운 시작 전이면 첫 대운

        // 4. 순행/역행 결정
        boolean isForward = isForwardDaeun(gender, yearCheongan);

        // 5. 월주 기준으로 대운 계산
        String daeunCheongan = calculateDaeunCheongan(monthCheongan, daeunIndex, isForward);
        String daeunJiji = calculateDaeunJiji(monthJiji, daeunIndex, isForward);

        log.debug("대운 계산 - 나이: {}, 대운차수: {}, 순행여부: {}, 대운: {}{}",
                age, daeunIndex, isForward, daeunCheongan, daeunJiji);

        return daeunCheongan + daeunJiji;
    }

    /**
     * 대운 순행/역행 판단
     * - 양남(陽男): 년주 천간이 양(갑병무경임) + 남성 = 순행
     * - 음녀(陰女): 년주 천간이 음(을정기신계) + 여성 = 순행
     * - 음남(陰男): 역행
     * - 양녀(陽女): 역행
     */
    private boolean isForwardDaeun(String gender, String yearCheongan) {
        boolean isYangCheongan = isYang(yearCheongan);
        boolean isMale = "M".equalsIgnoreCase(gender);

        // 양남 or 음녀 = 순행
        // 음남 or 양녀 = 역행
        return (isYangCheongan && isMale) || (!isYangCheongan && !isMale);
    }

    /**
     * 천간이 양인지 판단
     */
    private boolean isYang(String cheongan) {
        return "갑".equals(cheongan) || "병".equals(cheongan) ||
               "무".equals(cheongan) || "경".equals(cheongan) || "임".equals(cheongan);
    }

    /**
     * 대운 천간 계산
     */
    private String calculateDaeunCheongan(String monthCheongan, int daeunIndex, boolean isForward) {
        int currentIndex = findIndex(CHEONGAN, monthCheongan);
        if (currentIndex == -1) return monthCheongan; // 찾을 수 없으면 원본 반환

        int offset = isForward ? daeunIndex + 1 : -daeunIndex - 1;
        int newIndex = (currentIndex + offset) % CHEONGAN.length;
        if (newIndex < 0) newIndex += CHEONGAN.length;

        return CHEONGAN[newIndex];
    }

    /**
     * 대운 지지 계산
     */
    private String calculateDaeunJiji(String monthJiji, int daeunIndex, boolean isForward) {
        int currentIndex = findIndex(JIJI, monthJiji);
        if (currentIndex == -1) return monthJiji; // 찾을 수 없으면 원본 반환

        int offset = isForward ? daeunIndex + 1 : -daeunIndex - 1;
        int newIndex = (currentIndex + offset) % JIJI.length;
        if (newIndex < 0) newIndex += JIJI.length;

        return JIJI[newIndex];
    }

    /**
     * 배열에서 요소의 인덱스 찾기
     */
    private int findIndex(String[] array, String element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 대운 시작 나이 계산 (정확한 계산 - 선택사항)
     *
     * 실제로는 생일부터 다음 절기까지의 일수를 계산하여
     * 3일 = 1년으로 환산하는 복잡한 계산이 필요
     * 여기서는 간단히 3세 고정으로 반환
     */
    @SuppressWarnings("unused")
    private int calculateDaeunStartAge(LocalDate birthDate, boolean isForward) {
        // TODO: 정확한 계산이 필요하면 절기 계산 추가
        // 현재는 간단히 3세부터 시작
        return 3;
    }

    /**
     * 특정 나이에 해당하는 대운의 천간지지 계산
     *
     * @param targetAge 계산할 나이
     * @param birthDate 생년월일
     * @param gender 성별
     * @param yearCheongan 년주 천간
     * @param monthCheongan 월주 천간
     * @param monthJiji 월주 지지
     * @return 대운 천간지지 (예: "갑자")
     */
    public String calculateDaeunForAge(int targetAge, LocalDate birthDate, String gender,
                                       String yearCheongan, String monthCheongan, String monthJiji) {
        // 대운 시작 나이
        int daeunStartAge = 3;

        // 몇 번째 대운인지 계산 (10년 주기)
        int daeunIndex = (targetAge - daeunStartAge) / 10;
        if (daeunIndex < 0) daeunIndex = 0; // 대운 시작 전이면 첫 대운

        // 순행/역행 결정
        boolean isForward = isForwardDaeun(gender, yearCheongan);

        // 월주 기준으로 대운 계산
        String daeunCheongan = calculateDaeunCheongan(monthCheongan, daeunIndex, isForward);
        String daeunJiji = calculateDaeunJiji(monthJiji, daeunIndex, isForward);

        return daeunCheongan + daeunJiji;
    }

    /**
     * 대운 시작 나이 반환
     * (현재는 간단히 3세로 고정)
     */
    public int getDaeunStartAge() {
        return 3;
    }

    /**
     * 대운 주기 반환 (10년)
     */
    public int getDaeunCycle() {
        return 10;
    }

    /**
     * 월운(月運) 계산
     * 현재 월의 천간지지를 반환
     *
     * @param currentDate 현재 날짜
     * @return 월운 천간지지
     */
    public String calculateWolun(LocalDate currentDate) {
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();

        // 월주 계산 (절입일 기준)
        String monthStemBranch = StemBranchCalculator.getMonthStemBranchBySolarTerm(year, month, day);

        return monthStemBranch;
    }
}
