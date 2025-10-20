package com.codism.repository;

import com.codism.model.entity.SipsungMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 십성 마스터 Repository
 */
@Repository
public interface SipsungMasterRepository extends JpaRepository<SipsungMaster, Long> {

    /**
     * 십성 이름으로 조회
     */
    Optional<SipsungMaster> findBySipsungName(String sipsungName);

    /**
     * 십성 한자명으로 조회
     */
    Optional<SipsungMaster> findBySipsungHanja(String sipsungHanja);
}
