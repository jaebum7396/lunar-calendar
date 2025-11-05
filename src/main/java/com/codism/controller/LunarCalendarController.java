package com.codism.controller;

import com.codism.model.dto.response.StemBranchInfo;
import com.codism.service.StemBranchCalculator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class LunarCalendarController {

    private final StemBranchCalculator stemBranchCalculator;

    @Operation(
            summary = "천간지지 계산",
            description = "생년월일을 기준으로 천간지지를 계산합니다. " +
                    "시간이 제공되면 완전한 사주팔자(년월일시)를, " +
                    "시간이 없으면 년월일만 계산합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 파라미터"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping
    public ResponseEntity<StemBranchInfo> getStemBranch(
            @Parameter(description = "생년월일 (YYYY-MM-DD 형식)", example = "1990-05-15", required = true)
            @RequestParam String birthDate,

            @Parameter(description = "양력/음력 구분 (true: 양력, false: 음력)", example = "true", required = true)
            @RequestParam boolean isSolarCalendar,

            @Parameter(description = "출생시간 (HH:mm 또는 HH 형식, 선택사항. 제공하면 사주팔자 완성)", example = "14:30")
            @RequestParam(required = false) String birthTime
    ) throws Exception {

        log.info("천간지지 계산 요청 - birthDate: {}, birthTime: {}, isSolarCalendar: {}",
                birthDate, birthTime, isSolarCalendar);

        StemBranchInfo result;

        // 시간이 제공되었는지 확인
        if (birthTime != null && !birthTime.trim().isEmpty()) {
            log.info("시간 포함 사주팔자 계산");
            result = stemBranchCalculator.getAllStemBranch(birthDate, birthTime, isSolarCalendar);
        } else {
            log.info("년월일만 계산");
            result = stemBranchCalculator.getAllStemBranch(birthDate, isSolarCalendar);
        }

        log.info("천간지지 계산 결과: {}", result.toString());

        return ResponseEntity.ok(result);
    }
}