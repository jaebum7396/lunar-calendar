package com.codism.repository;

import com.codism.model.entity.SinsalRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 신살 규칙 Repository
 */
@Repository
public interface SinsalRuleRepository extends JpaRepository<SinsalRule, Long> {

    /**
     * 신살 ID로 모든 규칙 조회
     */
    List<SinsalRule> findBySinsalId(Long sinsalId);

    /**
     * 규칙 타입으로 조회
     */
    List<SinsalRule> findByRuleType(String ruleType);

    /**
     * 신살 ID와 규칙 타입으로 조회
     */
    List<SinsalRule> findBySinsalIdAndRuleType(Long sinsalId, String ruleType);
}
