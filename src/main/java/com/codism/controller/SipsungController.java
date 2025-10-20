package com.codism.controller;

import com.codism.model.dto.SipsungDetailResponse;
import com.codism.service.SipsungDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 십성 정보 조회 API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/sipsung")
@RequiredArgsConstructor
public class SipsungController {

    private final SipsungDetailService sipsungDetailService;

    /**
     * 십성 상세 정보 조회
     *
     * @param sipsungName 십성 이름 (예: "비견", "식신", "편재")
     * @return 십성 상세 정보
     */
    @GetMapping("/{sipsungName}")
    public ResponseEntity<SipsungDetailResponse> getSipsungDetail(
            @PathVariable String sipsungName) {

        log.info("십성 상세 조회 요청 - sipsungName: {}", sipsungName);

        SipsungDetailResponse response = sipsungDetailService.getSipsungDetail(sipsungName);
        return ResponseEntity.ok(response);
    }

    /**
     * 전체 십성 목록 조회
     *
     * @return 십성 목록
     */
    @GetMapping
    public ResponseEntity<List<SipsungDetailResponse>> getAllSipsung() {
        log.info("전체 십성 목록 조회 요청");

        List<SipsungDetailResponse> response = sipsungDetailService.getAllSipsung();
        return ResponseEntity.ok(response);
    }
}
