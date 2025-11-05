package com.codism.service;

import com.codism.model.dto.response.SipsungDetailResponse;
import com.codism.model.entity.SipsungMaster;
import com.codism.repository.SipsungMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 십성 상세 정보 조회 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SipsungDetailService {

    private final SipsungMasterRepository sipsungMasterRepository;

    /**
     * 십성 이름으로 상세 정보 조회
     *
     * @param sipsungName 십성 이름 (예: "비견", "식신", "편재" 등)
     * @return 십성 상세 정보
     */
    public SipsungDetailResponse getSipsungDetail(String sipsungName) {
        log.info("십성 상세 조회 - sipsungName: {}", sipsungName);

        SipsungMaster sipsung = sipsungMasterRepository.findBySipsungName(sipsungName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 십성입니다: " + sipsungName));

        return new SipsungDetailResponse(sipsung);
    }

    /**
     * 모든 십성 목록 조회
     *
     * @return 십성 목록
     */
    public java.util.List<SipsungDetailResponse> getAllSipsung() {
        log.info("전체 십성 목록 조회");

        return sipsungMasterRepository.findAll().stream()
                .map(SipsungDetailResponse::new)
                .toList();
    }
}
