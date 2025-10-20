package com.codism.controller;

import com.codism.model.entity.SinsalMaster;
import com.codism.model.entity.SipsungMaster;
import com.codism.model.enums.SinsalType;
import com.codism.repository.SinsalMasterRepository;
import com.codism.repository.SipsungMasterRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사주 정보(십성, 신살) 상세 조회 API
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/saju-info")
@RequiredArgsConstructor
@Tag(name = "사주 정보 API", description = "십성, 신살 상세 정보 조회")
public class SajuInfoController {

    private final SipsungMasterRepository sipsungMasterRepository;
    private final SinsalMasterRepository sinsalMasterRepository;

    /**
     * 십성 상세 정보 조회
     */
    @GetMapping("/sipsung/{name}")
    @Operation(
            summary = "십성 상세 정보 조회",
            description = "십성 이름으로 상세 정보(성격, 운세, 직업 등)를 조회합니다."
    )
    public ResponseEntity<SipsungMaster> getSipsungDetail(
            @Parameter(description = "십성 이름", example = "비견")
            @PathVariable String name
    ) {
        log.info("십성 상세 조회 - name: {}", name);

        return sipsungMasterRepository.findBySipsungName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 모든 십성 목록 조회
     */
    @GetMapping("/sipsung")
    @Operation(
            summary = "모든 십성 목록 조회",
            description = "10가지 십성의 전체 목록과 설명을 조회합니다."
    )
    public ResponseEntity<List<SipsungMaster>> getAllSipsung() {
        log.info("전체 십성 목록 조회");
        return ResponseEntity.ok(sipsungMasterRepository.findAll());
    }

    /**
     * 신살 상세 정보 조회
     */
    @GetMapping("/sinsal/{name}")
    @Operation(
            summary = "신살 상세 정보 조회",
            description = "신살 이름으로 상세 정보(설명, 효과 등)를 조회합니다."
    )
    public ResponseEntity<SinsalMaster> getSinsalDetail(
            @Parameter(description = "신살 이름", example = "천을귀인")
            @PathVariable String name
    ) {
        log.info("신살 상세 조회 - name: {}", name);

        return sinsalMasterRepository.findBySinsalName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 모든 신살 목록 조회
     */
    @GetMapping("/sinsal")
    @Operation(
            summary = "모든 신살 목록 조회",
            description = "활성화된 모든 신살의 목록과 설명을 조회합니다."
    )
    public ResponseEntity<List<SinsalMaster>> getAllSinsal(
            @Parameter(description = "신살 유형 필터 (길신/흉신)", example = "길신")
            @RequestParam(required = false) String type
    ) {
        log.info("전체 신살 목록 조회 - type: {}", type);

        List<SinsalMaster> sinsalList;
        if (type != null && !type.isEmpty()) {
            try {
                SinsalType sinsalType = SinsalType.fromKorean(type);
                sinsalList = sinsalMasterRepository.findBySinsalType(sinsalType);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid sinsal type: {}", type);
                return ResponseEntity.badRequest().build();
            }
        } else {
            sinsalList = sinsalMasterRepository.findByIsActiveTrueOrderByPriorityDesc();
        }

        return ResponseEntity.ok(sinsalList);
    }
}
