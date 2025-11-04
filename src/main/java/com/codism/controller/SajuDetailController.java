package com.codism.controller;

import com.codism.model.dto.IljuAnimalResponse;
import com.codism.model.dto.SajuDetailResponse;
import com.codism.service.SajuDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * 사주 상세 정보 조회 API 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/saju")
@RequiredArgsConstructor
@Tag(name = "사주 상세 API", description = "사주 팔자, 십성, 신살 정보 조회")
public class SajuDetailController {

    private final SajuDetailService sajuDetailService;

    /**
     * 사주 상세 정보 조회
     *
     * @param birthDate 생년월일 (YYYY-MM-DD)
     * @param birthTime 출생 시간 (HH:mm, 선택 사항)
     * @param gender 성별 (M: 남성, F: 여성)
     * @param isSolarCalendar 양력 여부 (기본값: true)
     * @return 사주 상세 정보
     */
    @GetMapping("/detail")
    @Operation(
            summary = "사주 상세 정보 조회",
            description = "생년월일시를 기반으로 사주 팔자, 십성, 신살 정보를 조회합니다. 대운 계산을 위해 성별이 필요합니다."
    )
    public ResponseEntity<SajuDetailResponse> getSajuDetail(
            @Parameter(description = "생년월일 (YYYY-MM-DD)", example = "1996-12-01", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthDate,

            @Parameter(description = "출생 시간 (HH:mm)", example = "07:00")
            @RequestParam(required = false, defaultValue = "00:00")
            String birthTime,

            @Parameter(description = "성별 (M: 남성, F: 여성)", example = "M")
            @RequestParam(required = false, defaultValue = "M")
            String gender,

            @Parameter(description = "양력 여부", example = "true")
            @RequestParam(required = false, defaultValue = "true")
            boolean isSolarCalendar
    ) {
        log.info("사주 상세 조회 요청 - birthDate: {}, birthTime: {}, gender: {}, isSolar: {}",
                birthDate, birthTime, gender, isSolarCalendar);

        SajuDetailResponse response = sajuDetailService.getSajuDetail(
                birthDate, birthTime, gender, isSolarCalendar
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 간단한 사주 팔자 조회 (시간 없이)
     *
     * @param birthDate 생년월일 (YYYY-MM-DD)
     * @param gender 성별 (M: 남성, F: 여성)
     * @return 사주 상세 정보
     */
    @GetMapping("/simple")
    @Operation(
            summary = "간단 사주 조회",
            description = "생년월일만으로 사주 정보를 조회합니다 (출생 시간 없이)."
    )
    public ResponseEntity<SajuDetailResponse> getSimpleSaju(
            @Parameter(description = "생년월일 (YYYY-MM-DD)", example = "1996-12-01", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthDate,

            @Parameter(description = "성별 (M: 남성, F: 여성)", example = "M")
            @RequestParam(required = false, defaultValue = "M")
            String gender
    ) {
        log.info("간단 사주 조회 요청 - birthDate: {}, gender: {}", birthDate, gender);

        SajuDetailResponse response = sajuDetailService.getSajuDetail(
                birthDate, "00:00", gender, true
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 일주 동물 조회
     *
     * @param birthDate 생년월일 (YYYY-MM-DD)
     * @param birthTime 출생 시간 (HH:mm, 선택 사항)
     * @param isSolarCalendar 양력 여부 (기본값: true)
     * @return 일주 동물 정보
     */
    @GetMapping("/ilju-animal")
    @Operation(
            summary = "일주 동물 조회",
            description = "생년월일시를 기반으로 일주(日柱)의 동물 정보를 조회합니다. 예: 센스 있는 검은 원숭이"
    )
    public ResponseEntity<IljuAnimalResponse> getIljuAnimal(
            @Parameter(description = "생년월일 (YYYY-MM-DD)", example = "1996-12-01", required = true)
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthDate,

            @Parameter(description = "출생 시간 (HH:mm)", example = "07:00")
            @RequestParam(required = false, defaultValue = "00:00")
            String birthTime,

            @Parameter(description = "양력 여부", example = "true")
            @RequestParam(required = false, defaultValue = "true")
            boolean isSolarCalendar
    ) {
        log.info("일주 동물 조회 요청 - birthDate: {}, birthTime: {}, isSolar: {}",
                birthDate, birthTime, isSolarCalendar);

        IljuAnimalResponse response = sajuDetailService.getIljuAnimal(
                birthDate, birthTime, isSolarCalendar
        );

        return ResponseEntity.ok(response);
    }

    /**
     * 사주 궁합 조회 (추후 구현)
     *
     * @param birthDate1 첫 번째 생년월일
     * @param birthDate2 두 번째 생년월일
     * @return 궁합 정보
     */
    @GetMapping("/compatibility")
    @Operation(
            summary = "사주 궁합 조회 (추후 구현)",
            description = "두 사람의 사주 궁합을 조회합니다."
    )
    public ResponseEntity<String> getSajuCompatibility(
            @Parameter(description = "첫 번째 생년월일", example = "1996-12-01")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthDate1,

            @Parameter(description = "두 번째 생년월일", example = "1997-05-15")
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate birthDate2
    ) {
        log.info("궁합 조회 요청 - birthDate1: {}, birthDate2: {}", birthDate1, birthDate2);

        // TODO: 궁합 계산 로직 구현
        return ResponseEntity.ok("궁합 조회 기능은 추후 구현 예정입니다.");
    }
}
