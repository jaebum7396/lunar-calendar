package com.codism.repository;

import com.codism.model.entity.JijiMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 지지 마스터 Repository
 */
@Repository
public interface JijiMasterRepository extends JpaRepository<JijiMaster, Long> {

    /**
     * 지지 한글명으로 조회
     */
    Optional<JijiMaster> findByJijiKorean(String jijiKorean);

    /**
     * 지지 한자명으로 조회
     */
    Optional<JijiMaster> findByJijiHanja(String jijiHanja);

    /**
     * 순서로 조회
     */
    Optional<JijiMaster> findBySequenceOrder(Integer sequenceOrder);
}
