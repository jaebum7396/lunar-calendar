package com.codism.service;

import com.codism.model.dto.response.SajuDetailResponse;
import com.codism.model.entity.SinsalMaster;
import com.codism.model.entity.SinsalRule;
import com.codism.model.enums.SinsalRuleType;
import com.codism.repository.SinsalMasterRepository;
import com.codism.repository.SinsalRuleRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * DB 기반 신살 계산 서비스
 * 하드코딩 대신 DB에서 규칙을 조회하여 신살 계산
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SinsalCalculatorDB {

    private final SinsalMasterRepository sinsalMasterRepository;
    private final SinsalRuleRepository sinsalRuleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 모든 신살 계산
     *
     * @param ilgan 일간
     * @param yearJiji 년지
     * @param monthJiji 월지
     * @param dayJiji 일지
     * @param hourJiji 시지
     * @param yearCheongan 년간
     * @param monthCheongan 월간
     * @param hourCheongan 시간
     * @return 신살 리스트
     */
    @Cacheable(value = "sinsal", key = "#ilgan + '_' + #yearJiji + '_' + #monthJiji + '_' + #dayJiji + '_' + #hourJiji + '_' + #yearCheongan + '_' + #monthCheongan + '_' + #hourCheongan")
    public List<SajuDetailResponse.SinsalInfo> calculateAllSinsal(
            String ilgan,
            String yearJiji, String monthJiji, String dayJiji, String hourJiji,
            String yearCheongan, String monthCheongan, String hourCheongan
    ) {
        log.debug("신살 계산 시작 - 일간: {}, 년지: {}, 월지: {}, 일지: {}, 시지: {}",
                ilgan, yearJiji, monthJiji, dayJiji, hourJiji);

        List<SajuDetailResponse.SinsalInfo> sinsalList = new ArrayList<>();

        // 활성화된 모든 신살 조회 (우선순위 순)
        List<SinsalMaster> activeSinsals = sinsalMasterRepository.findByIsActiveTrueOrderByPriorityDesc();

        for (SinsalMaster sinsal : activeSinsals) {
            // 각 신살의 규칙 조회
            List<SinsalRule> rules = sinsalRuleRepository.findBySinsalId(sinsal.getSinsalId());

            for (SinsalRule rule : rules) {
                // 규칙 체크
                if (checkSinsalRule(rule, ilgan, yearJiji, monthJiji, dayJiji, hourJiji,
                        yearCheongan, monthCheongan, hourCheongan)) {

                    // 신살 발견 (간략 정보만, 상세 정보는 별도 API로 조회)
                    sinsalList.add(new SajuDetailResponse.SinsalInfo(sinsal));

                    break; // 같은 신살 중복 방지
                }
            }
        }

        log.debug("신살 계산 완료 - 발견된 신살 수: {}", sinsalList.size());
        return sinsalList;
    }

    /**
     * 신살 규칙 체크
     *
     * @param rule 신살 규칙
     * @param ilgan 일간
     * @param yearJiji 년지
     * @param monthJiji 월지
     * @param dayJiji 일지
     * @param hourJiji 시지
     * @return 규칙 만족 여부
     */
    private boolean checkSinsalRule(
            SinsalRule rule,
            String ilgan,
            String yearJiji, String monthJiji, String dayJiji, String hourJiji,
            String yearCheongan, String monthCheongan, String hourCheongan
    ) {
        try {
            SinsalRuleType ruleType = rule.getRuleType();

            if (ruleType == null) {
                log.warn("규칙 타입이 null입니다 - rule: {}", rule.getRuleId());
                return false;
            }

            return switch (ruleType) {
                case ILGAN_JIJI -> checkIlganJijiRule(rule, ilgan, yearJiji, monthJiji, dayJiji, hourJiji);
                case YENJI_COMBINATION -> checkYenjiCombinationRule(rule, yearJiji, monthJiji, dayJiji, hourJiji);
                case JIJI_PATTERN -> checkJijiPatternRule(rule, yearJiji, monthJiji, dayJiji, hourJiji);
                case ILGAN_YENJI -> checkIlganYenjiRule(rule, ilgan, yearJiji);
                case CHEONGAN_JIJI -> checkCheonganJijiRule(rule, yearCheongan, monthCheongan, hourCheongan,
                        yearJiji, monthJiji, hourJiji);
                case YENJI_TO_ANY -> checkYenjiToAnyRule(rule, yearJiji, monthJiji, dayJiji, hourJiji);
                case ILJU_COMBINATION -> checkIljuCombinationRule(rule, ilgan, yearCheongan, monthCheongan, hourCheongan,
                        yearJiji, monthJiji, dayJiji, hourJiji);
                case JIJI_PAIR -> checkJijiPairRule(rule, yearJiji, monthJiji, dayJiji, hourJiji);
                case JIJI_TO_CHEONGAN -> checkJijiToCheonganRule(rule, ilgan, yearCheongan, monthCheongan, hourCheongan,
                        yearJiji, monthJiji, dayJiji, hourJiji);
                default -> {
                    log.warn("알 수 없는 규칙 타입: {}", ruleType);
                    yield false;
                }
            };
        } catch (Exception e) {
            log.error("신살 규칙 체크 실패 - rule: {}", rule.getRuleId(), e);
            return false;
        }
    }

    /**
     * 일간-지지 규칙 체크 (예: 갑일에 축지 보면 천을귀인)
     */
    private boolean checkIlganJijiRule(SinsalRule rule, String ilgan,
                                        String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            // JSON 배열 파싱
            List<String> ilganList = parseJsonArray(rule.getConditionIlgan());
            List<String> targetList = parseJsonArray(rule.getConditionTarget());

            if (ilganList == null || targetList == null) {
                return false;
            }

            // 일간 체크
            if (!ilganList.contains(ilgan)) {
                return false;
            }

            // match_position에 따라 확인할 지지 결정
            String matchPosition = rule.getMatchPosition();
            List<String> jijiToCheck;

            if ("year".equals(matchPosition)) {
                jijiToCheck = Arrays.asList(yearJiji);
            } else if ("month".equals(matchPosition)) {
                jijiToCheck = Arrays.asList(monthJiji);
            } else if ("day".equals(matchPosition)) {
                jijiToCheck = Arrays.asList(dayJiji);
            } else if ("hour".equals(matchPosition)) {
                jijiToCheck = Arrays.asList(hourJiji);
            } else {
                // match_position이 "any"이거나 null이면 모든 지지 확인
                jijiToCheck = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);
            }

            // 지지 체크
            for (String target : targetList) {
                if (jijiToCheck.contains(target)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            log.error("일간-지지 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 연지 조합 규칙 체크 (예: 년지가 인묘진 중 하나)
     */
    private boolean checkYenjiCombinationRule(SinsalRule rule, String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> yenjiList = parseJsonArray(rule.getConditionYenji());
            if (yenjiList == null) {
                return false;
            }

            String matchPosition = rule.getMatchPosition();

            // matchPosition이 'any'면 4기둥 전체에서 확인
            if ("any".equals(matchPosition)) {
                List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);
                for (String yenji : yenjiList) {
                    if (allJiji.contains(yenji)) {
                        return true;
                    }
                }
                return false;
            } else {
                // 기본: 년지만 확인
                return yenjiList.contains(yearJiji);
            }
        } catch (Exception e) {
            log.error("연지 조합 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 지지 패턴 규칙 체크 (예: 삼합, 육합)
     */
    private boolean checkJijiPatternRule(SinsalRule rule,
                                          String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> targetList = parseJsonArray(rule.getConditionTarget());
            if (targetList == null || targetList.size() < 2) {
                return false;
            }

            List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);

            if (rule.getRequireAll() != null && rule.getRequireAll()) {
                // 모든 지지가 있어야 함
                return allJiji.containsAll(targetList);
            } else {
                // 하나라도 있으면 됨
                for (String target : targetList) {
                    if (allJiji.contains(target)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            log.error("지지 패턴 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 일간-년지 규칙 체크
     */
    private boolean checkIlganYenjiRule(SinsalRule rule, String ilgan, String yearJiji) {
        try {
            List<String> ilganList = parseJsonArray(rule.getConditionIlgan());
            List<String> yenjiList = parseJsonArray(rule.getConditionYenji());

            if (ilganList == null || yenjiList == null) {
                return false;
            }

            return ilganList.contains(ilgan) && yenjiList.contains(yearJiji);
        } catch (Exception e) {
            log.error("일간-년지 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 천간-지지 조합 규칙 체크
     */
    private boolean checkCheonganJijiRule(SinsalRule rule,
                                           String yearCheongan, String monthCheongan, String hourCheongan,
                                           String yearJiji, String monthJiji, String hourJiji) {
        try {
            List<String> cheonganList = parseJsonArray(rule.getConditionIlgan());
            List<String> jijiList = parseJsonArray(rule.getConditionTarget());

            if (cheonganList == null || jijiList == null) {
                return false;
            }

            List<String> allCheongan = Arrays.asList(yearCheongan, monthCheongan, hourCheongan);
            List<String> allJiji = Arrays.asList(yearJiji, monthJiji, hourJiji);

            for (String cheongan : cheonganList) {
                if (!allCheongan.contains(cheongan)) {
                    continue;
                }
                for (String jiji : jijiList) {
                    if (allJiji.contains(jiji)) {
                        return true;
                    }
                }
            }

            return false;
        } catch (Exception e) {
            log.error("천간-지지 조합 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 연지를 기준으로 사주 전체에서 특정 지지 찾기
     * 예: 인오술 삼합에 묘가 있으면 도화살
     */
    private boolean checkYenjiToAnyRule(SinsalRule rule,
                                         String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> yenjiList = parseJsonArray(rule.getConditionYenji());
            List<String> targetList = parseJsonArray(rule.getConditionTarget());

            List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);

            // yenjiList가 비어있지 않으면 조건 체크
            if (!yenjiList.isEmpty()) {
                // target이 yenjiList에 포함되면: 년지만 체크 (화개살, 조객살 등)
                // target이 yenjiList에 없으면: 삼합 체크 (도화살, 역마살 등)
                boolean isTargetInYenji = false;
                if (targetList != null && !targetList.isEmpty()) {
                    for (String target : targetList) {
                        if (yenjiList.contains(target)) {
                            isTargetInYenji = true;
                            break;
                        }
                    }
                }

                if (isTargetInYenji) {
                    // 년지가 yenjiList에 포함되는지만 확인
                    if (!yenjiList.contains(yearJiji)) {
                        return false;
                    }
                } else {
                    // 사주 전체에서 yenjiList의 서로 다른 지지가 2개 이상 있는지 확인 (삼합 조건)
                    long matchCount = yenjiList.stream()
                            .filter(allJiji::contains)
                            .distinct()
                            .count();

                    if (matchCount < 2) {
                        return false;
                    }
                }
            }

            // 사주 전체에서 대상 지지가 있는지 확인
            for (String target : targetList) {
                if (allJiji.contains(target)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            log.error("연지-지지 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 일주 조합 규칙 체크 (일간 + 일지 조합)
     * 예: 갑진일주, 을사일주 등
     */
    private boolean checkIljuCombinationRule(SinsalRule rule, String ilgan,
                                              String yearCheongan, String monthCheongan, String hourCheongan,
                                              String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> ilganList = parseJsonArray(rule.getConditionIlgan());
            List<String> jijiList = parseJsonArray(rule.getConditionTarget());
            String matchPosition = rule.getMatchPosition();

            // matchPosition이 'any'면 4기둥 전체에서 천간-지지 조합 체크
            if ("any".equals(matchPosition)) {
                List<String> allCheongan = Arrays.asList(yearCheongan, monthCheongan, ilgan, hourCheongan);
                List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);

                // 4기둥 중 어디든 천간-지지 조합이 맞으면 true
                for (int i = 0; i < 4; i++) {
                    if (ilganList.contains(allCheongan.get(i)) && jijiList.contains(allJiji.get(i))) {
                        return true;
                    }
                }
                return false;
            } else {
                // matchPosition이 'day'면 일주만 체크 (기존 로직)
                return ilganList.contains(ilgan) && jijiList.contains(dayJiji);
            }
        } catch (Exception e) {
            log.error("일주 조합 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 지지 쌍 규칙 체크 (두 지지가 사주 내에 동시에 있는지)
     * 예: 귀문관살 - 진해, 자유, 인미, 축오, 묘신, 사술
     */
    private boolean checkJijiPairRule(SinsalRule rule,
                                       String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> yenji = parseJsonArray(rule.getConditionYenji());
            List<String> targetList = parseJsonArray(rule.getConditionTarget());

            if (yenji.isEmpty() || targetList.isEmpty()) {
                return false;
            }

            List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);

            // 첫 번째 지지가 사주에 있는지 확인
            boolean hasFirst = false;
            for (String first : yenji) {
                if (allJiji.contains(first)) {
                    hasFirst = true;
                    break;
                }
            }

            if (!hasFirst) {
                return false;
            }

            // 두 번째 지지가 사주에 있는지 확인
            for (String second : targetList) {
                if (allJiji.contains(second)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            log.error("지지 쌍 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * 지지-천간 규칙 체크 (지지를 보고 천간을 찾음)
     * 예: 천덕귀인 - 사년(지지)에 병(천간)이 있으면
     */
    private boolean checkJijiToCheonganRule(SinsalRule rule,
                                             String ilgan,
                                             String yearCheongan, String monthCheongan, String hourCheongan,
                                             String yearJiji, String monthJiji, String dayJiji, String hourJiji) {
        try {
            List<String> jijiList = parseJsonArray(rule.getConditionYenji());
            List<String> cheonganList = parseJsonArray(rule.getConditionTarget());

            log.debug("JIJI_TO_CHEONGAN 규칙 체크 - 지지조건: {}, 천간조건: {}", jijiList, cheonganList);
            log.debug("사주 - 일간: {}, 천간: [{}, {}, {}], 지지: [{}, {}, {}, {}]",
                    ilgan, yearCheongan, monthCheongan, hourCheongan, yearJiji, monthJiji, dayJiji, hourJiji);

            if (jijiList == null || cheonganList == null) {
                log.debug("조건이 null입니다");
                return false;
            }

            List<String> allJiji = Arrays.asList(yearJiji, monthJiji, dayJiji, hourJiji);
            List<String> allCheongan = Arrays.asList(ilgan, yearCheongan, monthCheongan, hourCheongan);

            // 지지가 조건에 맞는지 확인
            boolean hasJiji = false;
            for (String jiji : jijiList) {
                if (allJiji.contains(jiji)) {
                    hasJiji = true;
                    log.debug("지지 매칭: {}", jiji);
                    break;
                }
            }

            if (!hasJiji) {
                log.debug("지지가 조건에 맞지 않음");
                return false;
            }

            // 천간이 조건에 맞는지 확인
            for (String cheongan : cheonganList) {
                if (allCheongan.contains(cheongan)) {
                    log.debug("천간 매칭 성공: {} - 규칙 ID: {}", cheongan, rule.getRuleId());
                    return true;
                }
            }

            log.debug("천간이 조건에 맞지 않음");
            return false;
        } catch (Exception e) {
            log.error("지지-천간 규칙 체크 실패", e);
            return false;
        }
    }

    /**
     * JSON 배열 파싱 헬퍼
     */
    private List<String> parseJsonArray(String json) {
        if (json == null || json.trim().isEmpty() || json.equals("any") || json.equals("[]")) {
            return new ArrayList<>();  // 빈 리스트 반환
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("JSON 파싱 실패: {}", json, e);
            return new ArrayList<>();  // 빈 리스트 반환
        }
    }
}
