package com.codism.service;

import com.codism.model.entity.CheonganMaster;
import com.codism.model.entity.SipsungMaster;
import com.codism.model.entity.SipsungRule;
import com.codism.repository.CheonganMasterRepository;
import com.codism.repository.SipsungMasterRepository;
import com.codism.repository.SipsungRuleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DB 기반 십성 계산 서비스
 * 하드코딩 대신 DB에서 규칙을 조회하여 십성 계산
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SipSungCalculatorDB {

    private final CheonganMasterRepository cheonganMasterRepository;
    private final SipsungMasterRepository sipsungMasterRepository;
    private final SipsungRuleRepository sipsungRuleRepository;

    /**
     * 모든 십성 계산 (년주, 월주, 시주)
     *
     * @param ilgan 일간
     * @param yearCheongan 년간
     * @param monthCheongan 월간
     * @param hourCheongan 시간
     * @return 각 기둥의 십성 Map
     */
    public Map<String, String> calculateAllSipSung(
            String ilgan,
            String yearCheongan,
            String monthCheongan,
            String hourCheongan
    ) {
        log.debug("십성 계산 시작 - 일간: {}, 년간: {}, 월간: {}, 시간: {}",
                ilgan, yearCheongan, monthCheongan, hourCheongan);

        Map<String, String> result = new HashMap<>();

        // 일간은 항상 비견
        result.put("day", "비견");

        // 년간, 월간, 시간의 십성 계산
        result.put("year", calculateSipSung(ilgan, yearCheongan));
        result.put("month", calculateSipSung(ilgan, monthCheongan));
        result.put("hour", calculateSipSung(ilgan, hourCheongan));

        log.debug("십성 계산 결과: {}", result);
        return result;
    }

    /**
     * 십성 계산 (일간 vs 대상 천간)
     *
     * @param ilgan 일간
     * @param targetCheongan 대상 천간
     * @return 십성명
     */
    @Cacheable(value = "sipsung", key = "#ilgan + '_' + #targetCheongan")
    public String calculateSipSung(String ilgan, String targetCheongan) {
        try {
            // 일간과 대상이 같으면 비견
            if (ilgan.equals(targetCheongan)) {
                return "비견";
            }

            // 1. 일간의 오행과 음양 조회
            CheonganMaster ilganMaster = cheonganMasterRepository.findByCheonganKorean(ilgan)
                    .orElseThrow(() -> new IllegalArgumentException("일간 데이터 없음: " + ilgan));

            // 2. 대상 천간의 오행과 음양 조회
            CheonganMaster targetMaster = cheonganMasterRepository.findByCheonganKorean(targetCheongan)
                    .orElseThrow(() -> new IllegalArgumentException("대상 천간 데이터 없음: " + targetCheongan));

            // 3. 십성 규칙 조회
            Optional<SipsungRule> ruleOpt = sipsungRuleRepository.findByOhangAndEumyang(
                    ilganMaster.getOhang(),
                    targetMaster.getOhang(),
                    ilganMaster.getEumyang(),
                    targetMaster.getEumyang()
            );

            if (ruleOpt.isEmpty()) {
                log.warn("십성 규칙 없음 - 일간: {} ({}{}), 대상: {} ({}{})",
                        ilgan, ilganMaster.getOhang(), ilganMaster.getEumyang(),
                        targetCheongan, targetMaster.getOhang(), targetMaster.getEumyang());
                return "알 수 없음";
            }

            // 4. 십성 마스터에서 십성명 조회
            SipsungRule rule = ruleOpt.get();
            SipsungMaster sipsung = sipsungMasterRepository.findById(rule.getSipsungId())
                    .orElseThrow(() -> new IllegalArgumentException("십성 마스터 데이터 없음: " + rule.getSipsungId()));

            return sipsung.getSipsungName();

        } catch (Exception e) {
            log.error("십성 계산 실패 - ilgan: {}, target: {}", ilgan, targetCheongan, e);
            return "알 수 없음";
        }
    }

    /**
     * 십성 상세 정보 조회
     *
     * @param sipsungName 십성명
     * @return 십성 상세 정보
     */
    @Cacheable(value = "sipsungDetail", key = "#sipsungName")
    public Optional<SipsungMaster> getSipsungDetail(String sipsungName) {
        return sipsungMasterRepository.findBySipsungName(sipsungName);
    }
}
