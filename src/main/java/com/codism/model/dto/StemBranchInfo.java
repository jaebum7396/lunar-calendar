package com.codism.model.dto;

import lombok.Getter;

/**
 * 간지(천간지지) 정보를 담는 클래스
 * 년월일시 사주팔자 정보 포함
 */
@Getter
public class StemBranchInfo {
    private String yearStemBranch;
    private String monthStemBranch;
    private String dayStemBranch;
    private String timeStemBranch;

    public StemBranchInfo(String yearStemBranch, String monthStemBranch, String dayStemBranch, String timeStemBranch) {
        this.yearStemBranch = yearStemBranch;
        this.monthStemBranch = monthStemBranch;
        this.dayStemBranch = dayStemBranch;
        this.timeStemBranch = timeStemBranch;
    }

    // 기존 생성자 (하위 호환성)
    public StemBranchInfo(String yearStemBranch, String monthStemBranch, String dayStemBranch) {
        this(yearStemBranch, monthStemBranch, dayStemBranch, null);
    }

    @Override
    public String toString() {
        if (timeStemBranch != null) {
            return "년: " + yearStemBranch + ", 월: " + monthStemBranch + ", 일: " + dayStemBranch + ", 시: " + timeStemBranch;
        } else {
            return "년: " + yearStemBranch + ", 월: " + monthStemBranch + ", 일: " + dayStemBranch;
        }
    }

    /**
     * 사주팔자 형태로 문자열 반환
     */
    public String toSajuString() {
        if (timeStemBranch != null) {
            return yearStemBranch + " " + monthStemBranch + " " + dayStemBranch + " " + timeStemBranch;
        } else {
            return yearStemBranch + " " + monthStemBranch + " " + dayStemBranch;
        }
    }
}