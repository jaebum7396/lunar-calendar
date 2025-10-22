package com.codism.repository;

import com.codism.model.entity.CheonganMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 천간 마스터 Repository
 */
@Repository
public interface CheonganMasterRepository extends JpaRepository<CheonganMaster, Long> {

    /**
     * 천간 한글명으로 조회
     */
    Optional<CheonganMaster> findByCheonganKorean(String cheonganKorean);

    /**
     * 천간 한자명으로 조회
     */
    Optional<CheonganMaster> findByCheonganHanja(String cheonganHanja);

    /**
     * 순서로 조회
     */
    Optional<CheonganMaster> findBySequenceOrder(Integer sequenceOrder);
}
