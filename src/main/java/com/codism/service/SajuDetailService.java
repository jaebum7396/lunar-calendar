package com.codism.service;

import com.codism.model.dto.SajuDetailResponse;
import com.codism.model.dto.SajuDetailResponse.SajuPillar;
import com.codism.model.dto.SajuDetailResponse.DaeunInfo;
import com.codism.model.dto.SajuDetailResponse.HapChungAnalysis;
import com.codism.model.entity.CheonganMaster;
import com.codism.model.entity.JijiMaster;
import com.codism.model.entity.SipsungMaster;
import com.codism.repository.CheonganMasterRepository;
import com.codism.repository.JijiMasterRepository;
import com.codism.repository.SipsungMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

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
    private final SipsungMasterRepository sipsungMasterRepository;

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

            // 2. 천간십성 계산 (DB 기반)
            var cheonganSipsungMap = sipSungCalculatorDB.calculateAllSipSung(
                    ilgan, yearCheongan, monthCheongan, hourCheongan
            );

            // 3. 지지십성 계산 (DB 기반)
            String yearJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, yearJiji);
            String monthJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, monthJiji);
            String dayJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, dayJiji);
            String hourJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, hourJiji);

            // 4. 신살 계산 (DB 기반)
            var sinsalList = sinsalCalculatorDB.calculateAllSinsal(
                    ilgan,
                    yearJiji, monthJiji, dayJiji, hourJiji,
                    yearCheongan, monthCheongan, hourCheongan
            );

            // 5. 나이 계산
            int age = calculateAge(birthDate);

            // 6. 대운 계산
            String daeunStemBranch = daeunCalculator.calculateDaeun(
                    birthDate, gender, yearCheongan, monthCheongan, monthJiji
            );
            String daeunCheongan = daeunStemBranch.substring(0, 1);
            String daeunJiji = daeunStemBranch.substring(1, 2);
            String daeunCheonganSipsung = sipSungCalculatorDB.calculateSipSung(ilgan, daeunCheongan);
            String daeunJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, daeunJiji);

            // 7. 대운 타임라인 계산 (전체 생애 대운표)
            List<DaeunInfo> daeunList = calculateDaeunTimeline(
                    birthDate, gender, yearCheongan, monthCheongan, monthJiji, ilgan, age
            );

            // 8. 월운 계산
            String wolunStemBranch = daeunCalculator.calculateWolun(LocalDate.now());
            String wolunCheongan = wolunStemBranch.substring(0, 1);
            String wolunJiji = wolunStemBranch.substring(1, 2);
            String wolunCheonganSipsung = sipSungCalculatorDB.calculateSipSung(ilgan, wolunCheongan);
            String wolunJijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, wolunJiji);

            // 9. 합충형파해 분석
            HapChungAnalysis hapChungAnalysis = analyzeHapChung(
                    yearCheongan, monthCheongan, dayCheongan, hourCheongan,
                    yearJiji, monthJiji, dayJiji, hourJiji,
                    daeunCheongan, daeunJiji,
                    createSeun(LocalDate.now().getYear(), ilgan),
                    createPillar(wolunCheongan, wolunJiji, wolunCheonganSipsung, wolunJijiSipsung)
            );

            // 10. 응답 DTO 생성
            return new SajuDetailResponse(
                    birthDate,
                    birthTime,
                    isSolarCalendar,
                    age,
                    createPillar(yearCheongan, yearJiji, cheonganSipsungMap.get("year"), yearJijiSipsung),
                    createPillar(monthCheongan, monthJiji, cheonganSipsungMap.get("month"), monthJijiSipsung),
                    createPillar(dayCheongan, dayJiji, cheonganSipsungMap.get("day"), dayJijiSipsung),
                    createPillar(hourCheongan, hourJiji, cheonganSipsungMap.get("hour"), hourJijiSipsung),
                    createPillar(daeunCheongan, daeunJiji, daeunCheonganSipsung, daeunJijiSipsung),  // 대운
                    createSeun(LocalDate.now().getYear(), ilgan),  // 세운 (현재년도)
                    createPillar(wolunCheongan, wolunJiji, wolunCheonganSipsung, wolunJijiSipsung),  // 월운
                    daeunList,  // 대운 타임라인
                    hapChungAnalysis,  // 합충형파해 분석
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
    private SajuPillar createPillar(String cheongan, String jiji, String cheonganSipsung, String jijiSipsung) {
        return createPillar(cheongan, jiji, cheonganSipsung, jijiSipsung, null);
    }

    /**
     * 사주 기둥 생성 (년도 포함)
     */
    private SajuPillar createPillar(String cheongan, String jiji, String cheonganSipsung, String jijiSipsung, Integer year) {
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
                cheonganSipsung,
                jijiSipsung,
                year
        );
    }

    /**
     * 세운 계산 (현재 년도의 천간지지)
     */
    private SajuPillar createSeun(int currentYear, String ilgan) {
        String yearStemBranch = StemBranchCalculator.getYearStemBranch(currentYear);
        String cheongan = yearStemBranch.substring(0, 1);
        String jiji = yearStemBranch.substring(1, 2);

        // 천간십성 계산
        String cheonganSipsung = sipSungCalculatorDB.calculateSipSung(ilgan, cheongan);

        // 지지십성 계산
        String jijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, jiji);

        // 년도 포함하여 Pillar 생성
        return createPillar(cheongan, jiji, cheonganSipsung, jijiSipsung, currentYear);
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

    /**
     * 대운 타임라인 계산 (전체 생애 대운표)
     *
     * @param birthDate 생년월일
     * @param gender 성별
     * @param yearCheongan 년주 천간
     * @param monthCheongan 월주 천간
     * @param monthJiji 월주 지지
     * @param ilgan 일간
     * @param currentAge 현재 나이
     * @return 대운 타임라인 리스트
     */
    private List<DaeunInfo> calculateDaeunTimeline(LocalDate birthDate, String gender,
                                                    String yearCheongan, String monthCheongan, String monthJiji,
                                                    String ilgan, int currentAge) {
        List<DaeunInfo> daeunList = new ArrayList<>();

        int daeunStartAge = daeunCalculator.getDaeunStartAge();  // 3세
        int daeunCycle = daeunCalculator.getDaeunCycle();  // 10년
        int maxAge = 100;  // 100세까지 계산

        for (int age = daeunStartAge; age < maxAge; age += daeunCycle) {
            int startAge = age;
            int endAge = age + daeunCycle - 1;

            // 대운 계산
            String daeunStemBranch = daeunCalculator.calculateDaeunForAge(
                    startAge, birthDate, gender, yearCheongan, monthCheongan, monthJiji
            );

            String cheongan = daeunStemBranch.substring(0, 1);
            String jiji = daeunStemBranch.substring(1, 2);

            // 십성 계산
            String cheonganSipsung = sipSungCalculatorDB.calculateSipSung(ilgan, cheongan);
            String jijiSipsung = sipSungCalculatorDB.calculateJijiSipsung(ilgan, jiji);

            // 현재 대운 여부
            boolean isCurrent = (currentAge >= startAge && currentAge <= endAge);

            // 현재 대운이면 남은 년수 계산
            Integer yearsRemaining = null;
            if (isCurrent) {
                yearsRemaining = endAge - currentAge;
            }

            // 기간 정보 생성
            String periodInfo = "전반 5년은 천간(" + cheongan + ") 영향, 후반 5년은 지지(" + jiji + ") 영향";

            // 간단 해석 생성
            String interpretation = generateDaeunInterpretation(cheonganSipsung, jijiSipsung);

            // 천간, 지지 한자 조회
            CheonganMaster cheonganMaster = cheonganMasterRepository.findByCheonganKorean(cheongan).orElse(null);
            JijiMaster jijiMaster = jijiMasterRepository.findByJijiKorean(jiji).orElse(null);

            daeunList.add(new DaeunInfo(
                    startAge,
                    endAge,
                    cheongan,
                    cheonganMaster != null ? cheonganMaster.getCheonganHanja() : cheongan,
                    jiji,
                    jijiMaster != null ? jijiMaster.getJijiHanja() : jiji,
                    cheonganSipsung,
                    jijiSipsung,
                    isCurrent,
                    interpretation,
                    yearsRemaining,
                    periodInfo
            ));
        }

        return daeunList;
    }

    /**
     * 대운 해석 생성 (DB에서 조회)
     */
    private String generateDaeunInterpretation(String cheonganSipsung, String jijiSipsung) {
        // 천간십성을 우선으로 해석 생성
        String mainSipsung = cheonganSipsung != null ? cheonganSipsung : jijiSipsung;

        if (mainSipsung == null) {
            return "안정적인 시기";
        }

        // DB에서 십성 정보 조회
        SipsungMaster sipsungMaster = sipsungMasterRepository.findBySipsungName(mainSipsung).orElse(null);

        if (sipsungMaster != null && sipsungMaster.getDaeunInterpretation() != null) {
            return sipsungMaster.getDaeunInterpretation();
        }

        // DB에 없을 경우 기본 메시지
        return mainSipsung + "운 - 안정적인 시기";
    }

    /**
     * 합충형파해 분석
     */
    private HapChungAnalysis analyzeHapChung(
            String yearCheongan, String monthCheongan, String dayCheongan, String hourCheongan,
            String yearJiji, String monthJiji, String dayJiji, String hourJiji,
            String daeunCheongan, String daeunJiji,
            SajuPillar seun, SajuPillar wolun) {

        List<String> hapList = new ArrayList<>();
        List<String> chungList = new ArrayList<>();
        List<String> hyeongList = new ArrayList<>();
        List<String> paList = new ArrayList<>();
        List<String> haeList = new ArrayList<>();
        List<String> sajuToDaeun = new ArrayList<>();
        List<String> sajuToSeun = new ArrayList<>();
        List<String> sajuToWolun = new ArrayList<>();

        // 사주 내부 합 (천간합)
        checkCheonganHap(hapList, "년간", yearCheongan, "월간", monthCheongan);
        checkCheonganHap(hapList, "년간", yearCheongan, "일간", dayCheongan);
        checkCheonganHap(hapList, "년간", yearCheongan, "시간", hourCheongan);
        checkCheonganHap(hapList, "월간", monthCheongan, "일간", dayCheongan);
        checkCheonganHap(hapList, "월간", monthCheongan, "시간", hourCheongan);
        checkCheonganHap(hapList, "일간", dayCheongan, "시간", hourCheongan);

        // 사주 내부 합 (지지합: 육합, 삼합)
        checkJijiHap(hapList, "년지", yearJiji, "월지", monthJiji);
        checkJijiHap(hapList, "년지", yearJiji, "일지", dayJiji);
        checkJijiHap(hapList, "년지", yearJiji, "시지", hourJiji);
        checkJijiHap(hapList, "월지", monthJiji, "일지", dayJiji);
        checkJijiHap(hapList, "월지", monthJiji, "시지", hourJiji);
        checkJijiHap(hapList, "일지", dayJiji, "시지", hourJiji);

        // 사주 내부 충 (지지 육충)
        checkJijiChung(chungList, "년지", yearJiji, "월지", monthJiji);
        checkJijiChung(chungList, "년지", yearJiji, "일지", dayJiji);
        checkJijiChung(chungList, "년지", yearJiji, "시지", hourJiji);
        checkJijiChung(chungList, "월지", monthJiji, "일지", dayJiji);
        checkJijiChung(chungList, "월지", monthJiji, "시지", hourJiji);
        checkJijiChung(chungList, "일지", dayJiji, "시지", hourJiji);

        // 사주 내부 형 (지지 삼형)
        checkJijiHyeong(hyeongList, yearJiji, monthJiji, dayJiji, hourJiji);

        // 사주 내부 파 (지지 육파)
        checkJijiPa(paList, "년지", yearJiji, "월지", monthJiji);
        checkJijiPa(paList, "년지", yearJiji, "일지", dayJiji);
        checkJijiPa(paList, "년지", yearJiji, "시지", hourJiji);
        checkJijiPa(paList, "월지", monthJiji, "일지", dayJiji);
        checkJijiPa(paList, "월지", monthJiji, "시지", hourJiji);
        checkJijiPa(paList, "일지", dayJiji, "시지", hourJiji);

        // 사주 내부 해 (지지 육해)
        checkJijiHae(haeList, "년지", yearJiji, "월지", monthJiji);
        checkJijiHae(haeList, "년지", yearJiji, "일지", dayJiji);
        checkJijiHae(haeList, "년지", yearJiji, "시지", hourJiji);
        checkJijiHae(haeList, "월지", monthJiji, "일지", dayJiji);
        checkJijiHae(haeList, "월지", monthJiji, "시지", hourJiji);
        checkJijiHae(haeList, "일지", dayJiji, "시지", hourJiji);

        // 사주-대운 작용
        checkCheonganHap(sajuToDaeun, "일간", dayCheongan, "대운천간", daeunCheongan);
        checkJijiHap(sajuToDaeun, "일지", dayJiji, "대운지지", daeunJiji);
        checkJijiChung(sajuToDaeun, "일지", dayJiji, "대운지지", daeunJiji);

        // 사주-세운 작용
        if (seun != null) {
            checkCheonganHap(sajuToSeun, "일간", dayCheongan, "세운천간", seun.getCheongan());
            checkJijiHap(sajuToSeun, "일지", dayJiji, "세운지지", seun.getJiji());
            checkJijiChung(sajuToSeun, "일지", dayJiji, "세운지지", seun.getJiji());
        }

        // 사주-월운 작용
        if (wolun != null) {
            checkCheonganHap(sajuToWolun, "일간", dayCheongan, "월운천간", wolun.getCheongan());
            checkJijiHap(sajuToWolun, "일지", dayJiji, "월운지지", wolun.getJiji());
            checkJijiChung(sajuToWolun, "일지", dayJiji, "월운지지", wolun.getJiji());
        }

        return new HapChungAnalysis(hapList, chungList, hyeongList, paList, haeList,
                sajuToDaeun, sajuToSeun, sajuToWolun);
    }

    // === 합충형파해 체크 메서드들 ===

    /**
     * 천간합 체크 (갑기합토, 을경합금, 병신합수, 정임합목, 무계합화)
     */
    private void checkCheonganHap(List<String> hapList, String pos1, String cheongan1, String pos2, String cheongan2) {
        String result = null;
        if ((cheongan1.equals("갑") && cheongan2.equals("기")) || (cheongan1.equals("기") && cheongan2.equals("갑"))) {
            result = pos1 + "-" + pos2 + " 갑기합토";
        } else if ((cheongan1.equals("을") && cheongan2.equals("경")) || (cheongan1.equals("경") && cheongan2.equals("을"))) {
            result = pos1 + "-" + pos2 + " 을경합금";
        } else if ((cheongan1.equals("병") && cheongan2.equals("신")) || (cheongan1.equals("신") && cheongan2.equals("병"))) {
            result = pos1 + "-" + pos2 + " 병신합수";
        } else if ((cheongan1.equals("정") && cheongan2.equals("임")) || (cheongan1.equals("임") && cheongan2.equals("정"))) {
            result = pos1 + "-" + pos2 + " 정임합목";
        } else if ((cheongan1.equals("무") && cheongan2.equals("계")) || (cheongan1.equals("계") && cheongan2.equals("무"))) {
            result = pos1 + "-" + pos2 + " 무계합화";
        }
        if (result != null) {
            hapList.add(result);
        }
    }

    /**
     * 지지합 체크 (육합, 삼합)
     */
    private void checkJijiHap(List<String> hapList, String pos1, String jiji1, String pos2, String jiji2) {
        // 육합: 자축(합토), 인해(합목), 묘술(합화), 진유(합금), 사신(합수), 오미(합화/합토)
        String result = null;
        if ((jiji1.equals("자") && jiji2.equals("축")) || (jiji1.equals("축") && jiji2.equals("자"))) {
            result = pos1 + "-" + pos2 + " 자축합토";
        } else if ((jiji1.equals("인") && jiji2.equals("해")) || (jiji1.equals("해") && jiji2.equals("인"))) {
            result = pos1 + "-" + pos2 + " 인해합목";
        } else if ((jiji1.equals("묘") && jiji2.equals("술")) || (jiji1.equals("술") && jiji2.equals("묘"))) {
            result = pos1 + "-" + pos2 + " 묘술합화";
        } else if ((jiji1.equals("진") && jiji2.equals("유")) || (jiji1.equals("유") && jiji2.equals("진"))) {
            result = pos1 + "-" + pos2 + " 진유합금";
        } else if ((jiji1.equals("사") && jiji2.equals("신")) || (jiji1.equals("신") && jiji2.equals("사"))) {
            result = pos1 + "-" + pos2 + " 사신합수";
        } else if ((jiji1.equals("오") && jiji2.equals("미")) || (jiji1.equals("미") && jiji2.equals("오"))) {
            result = pos1 + "-" + pos2 + " 오미합화";
        }
        if (result != null) {
            hapList.add(result);
        }
    }

    /**
     * 지지 육충 체크 (자오충, 축미충, 인신충, 묘유충, 진술충, 사해충)
     */
    private void checkJijiChung(List<String> chungList, String pos1, String jiji1, String pos2, String jiji2) {
        String result = null;
        if ((jiji1.equals("자") && jiji2.equals("오")) || (jiji1.equals("오") && jiji2.equals("자"))) {
            result = pos1 + "-" + pos2 + " 자오충";
        } else if ((jiji1.equals("축") && jiji2.equals("미")) || (jiji1.equals("미") && jiji2.equals("축"))) {
            result = pos1 + "-" + pos2 + " 축미충";
        } else if ((jiji1.equals("인") && jiji2.equals("신")) || (jiji1.equals("신") && jiji2.equals("인"))) {
            result = pos1 + "-" + pos2 + " 인신충";
        } else if ((jiji1.equals("묘") && jiji2.equals("유")) || (jiji1.equals("유") && jiji2.equals("묘"))) {
            result = pos1 + "-" + pos2 + " 묘유충";
        } else if ((jiji1.equals("진") && jiji2.equals("술")) || (jiji1.equals("술") && jiji2.equals("진"))) {
            result = pos1 + "-" + pos2 + " 진술충";
        } else if ((jiji1.equals("사") && jiji2.equals("해")) || (jiji1.equals("해") && jiji2.equals("사"))) {
            result = pos1 + "-" + pos2 + " 사해충";
        }
        if (result != null) {
            chungList.add(result);
        }
    }

    /**
     * 지지 삼형 체크
     */
    private void checkJijiHyeong(List<String> hyeongList, String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        List<String> allJiji = List.of(yearJiji, monthJiji, dayJiji, hourJiji);

        // 무은형(삼형): 인사신
        long insasinCount = allJiji.stream().filter(j -> j.equals("인") || j.equals("사") || j.equals("신")).count();
        if (insasinCount >= 2) {
            hyeongList.add("인사신 무은형");
        }

        // 무례형(삼형): 축술미
        long chuksulmiCount = allJiji.stream().filter(j -> j.equals("축") || j.equals("술") || j.equals("미")).count();
        if (chuksulmiCount >= 2) {
            hyeongList.add("축술미 무례형");
        }

        // 무자형(삼형): 자묘
        if (allJiji.contains("자") && allJiji.contains("묘")) {
            hyeongList.add("자묘 무자형");
        }
    }

    /**
     * 지지 육파 체크 (자유파, 오묘파, 진축파, 술미파, 해신파, 인사파)
     */
    private void checkJijiPa(List<String> paList, String pos1, String jiji1, String pos2, String jiji2) {
        String result = null;
        if ((jiji1.equals("자") && jiji2.equals("유")) || (jiji1.equals("유") && jiji2.equals("자"))) {
            result = pos1 + "-" + pos2 + " 자유파";
        } else if ((jiji1.equals("오") && jiji2.equals("묘")) || (jiji1.equals("묘") && jiji2.equals("오"))) {
            result = pos1 + "-" + pos2 + " 오묘파";
        } else if ((jiji1.equals("진") && jiji2.equals("축")) || (jiji1.equals("축") && jiji2.equals("진"))) {
            result = pos1 + "-" + pos2 + " 진축파";
        } else if ((jiji1.equals("술") && jiji2.equals("미")) || (jiji1.equals("미") && jiji2.equals("술"))) {
            result = pos1 + "-" + pos2 + " 술미파";
        } else if ((jiji1.equals("해") && jiji2.equals("신")) || (jiji1.equals("신") && jiji2.equals("해"))) {
            result = pos1 + "-" + pos2 + " 해신파";
        } else if ((jiji1.equals("인") && jiji2.equals("사")) || (jiji1.equals("사") && jiji2.equals("인"))) {
            result = pos1 + "-" + pos2 + " 인사파";
        }
        if (result != null) {
            paList.add(result);
        }
    }

    /**
     * 지지 육해 체크 (자미해, 축오해, 인사해, 묘진해, 신해해, 유술해)
     */
    private void checkJijiHae(List<String> haeList, String pos1, String jiji1, String pos2, String jiji2) {
        String result = null;
        if ((jiji1.equals("자") && jiji2.equals("미")) || (jiji1.equals("미") && jiji2.equals("자"))) {
            result = pos1 + "-" + pos2 + " 자미해";
        } else if ((jiji1.equals("축") && jiji2.equals("오")) || (jiji1.equals("오") && jiji2.equals("축"))) {
            result = pos1 + "-" + pos2 + " 축오해";
        } else if ((jiji1.equals("인") && jiji2.equals("사")) || (jiji1.equals("사") && jiji2.equals("인"))) {
            result = pos1 + "-" + pos2 + " 인사해";
        } else if ((jiji1.equals("묘") && jiji2.equals("진")) || (jiji1.equals("진") && jiji2.equals("묘"))) {
            result = pos1 + "-" + pos2 + " 묘진해";
        } else if ((jiji1.equals("신") && jiji2.equals("해")) || (jiji1.equals("해") && jiji2.equals("신"))) {
            result = pos1 + "-" + pos2 + " 신해해";
        } else if ((jiji1.equals("유") && jiji2.equals("술")) || (jiji1.equals("술") && jiji2.equals("유"))) {
            result = pos1 + "-" + pos2 + " 유술해";
        }
        if (result != null) {
            haeList.add(result);
        }
    }
}
