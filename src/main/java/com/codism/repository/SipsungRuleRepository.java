package com.codism.repository;

import com.codism.model.entity.SipsungRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 십성 규칙 Repository
 */
@Repository
public interface SipsungRuleRepository extends JpaRepository<SipsungRule, Long> {

    /**
     * 십성 ID로 모든 규칙 조회
     */
    List<SipsungRule> findBySipsungId(Long sipsungId);

    /**
     * 일간 오행과 대상 오행, 음양으로 십성 규칙 조회
     */
    @Query("SELECT sr FROM SipsungRule sr WHERE " +
            "sr.ilganOhang = :ilganOhang AND " +
            "sr.targetOhang = :targetOhang AND " +
            "sr.ilganEumyang = :ilganEumyang AND " +
            "sr.targetEumyang = :targetEumyang")
    Optional<SipsungRule> findByOhangAndEumyang(
            @Param("ilganOhang") String ilganOhang,
            @Param("targetOhang") String targetOhang,
            @Param("ilganEumyang") String ilganEumyang,
            @Param("targetEumyang") String targetEumyang
    );
}
