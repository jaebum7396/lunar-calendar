package com.codism.service;

import com.codism.model.dto.SajuDetailResponse;
import com.codism.model.dto.SajuDetailResponse.SajuPillar;
import com.codism.model.entity.CheonganMaster;
import com.codism.model.entity.JijiMaster;
import com.codism.repository.CheonganMasterRepository;
import com.codism.repository.JijiMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

/**
 * 사주 상세 정보 서비스
 * 천간지지, 십성, 신살을 통합하여 전체 사주 정보 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SajuDetailService {

    private final StemBranchCalculator stemBranchCalculator;
    private final SipSungCalculatorDB sipSungCalculatorDB;
    private final SinsalCalculatorDB sinsalCalculatorDB;
    private final DaeunCalculator daeunCalculator;
    private final CheonganMasterRepository cheonganMasterRepository;
    private final JijiMasterRepository jijiMasterRepository;

    /**
     * 사주 상세 정보 조회
     *
     * @param birthDate 생년월일
     * @param birthTime 출생 시간 (예: "07:00")
     * @param isSolarCalendar 양력 여부
     * @return 사주 상세 정보
     */
    public SajuDetailResponse getSajuDetail(LocalDate birthDate, String birthTime, String gender, boolean isSolarCalendar) {
        log.info("사주 상세 조회 시작 - birthDate: {}, birthTime: {}, gender: {}, isSolar: {}",
                birthDate, birthTime, gender, isSolarCalendar);

        try {
            // 1. 천간지지 계산
            int year = birthDate.getYear();
            int month = birthDate.getMonthValue();
            int day = birthDate.getDayOfMonth();
            Integer hour = birthTime != null ? parseHour(birthTime) : null;

            // 음력 변환이 포함된 메서드 사용
            var stemBranchInfo = StemBranchCalculator.getAllStemBranch(year, month, day, hour, isSolarCalendar);

            String yearStemBranch = stemBranchInfo.getYearStemBranch();
            String monthStemBranch = stemBranchInfo.getMonthStemBranch();
            String dayStemBranch = stemBranchInfo.getDayStemBranch();
            String timeStemBranch = stemBranchInfo.getTimeStemBranch();

            // 천간/지지 분리
            String yearCheongan = yearStemBranch.substring(0, 1);
            String yearJiji = yearStemBranch.substring(1, 2);
            String monthCheongan = monthStemBranch.substring(0, 1);
            String monthJiji = monthStemBranch.substring(1, 2);
            String dayCheongan = dayStemBranch.substring(0, 1);
            String dayJiji = dayStemBranch.substring(1, 2);
            String hourCheongan = timeStemBranch.substring(0, 1);
            String hourJiji = timeStemBranch.substring(1, 2);

            // 일간
            String ilgan = dayCheongan;

            // 2. 십성 계산 (DB 기반)
            var sipSungMap = sipSungCalculatorDB.calculateAllSipSung(
                    ilgan, yearCheongan, monthCheongan, hourCheongan
            );

            // 3. 신살 계산 (DB 기반)
            var sinsalList = sinsalCalculatorDB.calculateAllSinsal(
                    ilgan,
                    yearJiji, monthJiji, dayJiji, hourJiji,
                    yearCheongan, monthCheongan, hourCheongan
            );

            // 4. 나이 계산
            int age = calculateAge(birthDate);

            // 5. 대운 계산
            String daeunStemBranch = daeunCalculator.calculateDaeun(
                    birthDate, gender, yearCheongan, monthCheongan, monthJiji
            );
            String daeunCheongan = daeunStemBranch.substring(0, 1);
            String daeunJiji = daeunStemBranch.substring(1, 2);
            String daeunSipsung = sipSungCalculatorDB.calculateSipSung(ilgan, daeunCheongan);

            // 6. 응답 DTO 생성
            return new SajuDetailResponse(
                    birthDate,
                    birthTime,
                    isSolarCalendar,
                    age,
                    createPillar(yearCheongan, yearJiji, sipSungMap.get("year")),
                    createPillar(monthCheongan, monthJiji, sipSungMap.get("month")),
                    createPillar(dayCheongan, dayJiji, sipSungMap.get("day")),
                    createPillar(hourCheongan, hourJiji, sipSungMap.get("hour")),
                    createPillar(daeunCheongan, daeunJiji, daeunSipsung),  // 대운 (고도화됨)
                    createSeun(LocalDate.now().getYear(), ilgan),  // 세운 (현재년도)
                    sinsalList
            );

        } catch (Exception e) {
            log.error("사주 상세 조회 실패", e);
            throw new RuntimeException("사주 계산 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사주 기둥 생성 (DB에서 한자, 색상 조회)
     */
    private SajuPillar createPillar(String cheongan, String jiji, String sipsung) {
        // DB에서 천간 정보 조회 (한자, 색상)
        CheonganMaster cheonganMaster = cheonganMasterRepository.findByCheonganKorean(cheongan)
                .orElse(null);

        // DB에서 지지 정보 조회 (한자, 색상)
        JijiMaster jijiMaster = jijiMasterRepository.findByJijiKorean(jiji)
                .orElse(null);

        return new SajuPillar(
                cheongan,
                cheonganMaster != null ? cheonganMaster.getCheonganHanja() : cheongan,
                cheonganMaster != null ? cheonganMaster.getColor() : null,
                cheonganMaster != null ? cheonganMaster.getColorHex() : null,
                jiji,
                jijiMaster != null ? jijiMaster.getJijiHanja() : jiji,
                jijiMaster != null ? jijiMaster.getColor() : null,
                jijiMaster != null ? jijiMaster.getColorHex() : null,
                sipsung
        );
    }

    /**
     * 세운 계산 (현재 년도의 천간지지)
     */
    private SajuPillar createSeun(int currentYear, String ilgan) {
        String yearStemBranch = StemBranchCalculator.getYearStemBranch(currentYear);
        String cheongan = yearStemBranch.substring(0, 1);
        String jiji = yearStemBranch.substring(1, 2);

        // 십성 계산
        String sipsung = sipSungCalculatorDB.calculateSipSung(ilgan, cheongan);

        // DB에서 정보 조회 (한자, 색상)
        CheonganMaster cheonganMaster = cheonganMasterRepository.findByCheonganKorean(cheongan)
                .orElse(null);

        JijiMaster jijiMaster = jijiMasterRepository.findByJijiKorean(jiji)
                .orElse(null);

        return new SajuPillar(
                cheongan,
                cheonganMaster != null ? cheonganMaster.getCheonganHanja() : cheongan,
                cheonganMaster != null ? cheonganMaster.getColor() : null,
                cheonganMaster != null ? cheonganMaster.getColorHex() : null,
                jiji,
                jijiMaster != null ? jijiMaster.getJijiHanja() : jiji,
                jijiMaster != null ? jijiMaster.getColor() : null,
                jijiMaster != null ? jijiMaster.getColorHex() : null,
                sipsung
        );
    }

    /**
     * 나이 계산
     */
    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    /**
     * 시간 문자열 파싱 (예: "07:00" -> 6, "07:01" -> 7)
     * 정시(XX:00)는 이전 시간대에 포함됨 (예: 07:00 = 묘시, 07:01 = 진시)
     */
    private Integer parseHour(String birthTime) {
        if (birthTime == null || birthTime.trim().isEmpty() || birthTime.equals("00:00")) {
            return null;
        }
        try {
            String[] parts = birthTime.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;

            // 정시(XX:00)는 이전 시간대로 처리
            // 예: 07:00 -> 6시로 처리하여 묘시(05:00~07:00)에 포함
            if (minute == 0 && hour > 0) {
                return hour - 1;
            }

            return hour;
        } catch (Exception e) {
            log.warn("시간 파싱 실패: {}", birthTime);
            return null;
        }
    }
}
