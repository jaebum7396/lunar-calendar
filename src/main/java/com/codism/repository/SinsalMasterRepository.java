package com.codism.repository;

import com.codism.model.entity.SinsalMaster;
import com.codism.model.enums.SinsalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 신살 마스터 Repository
 */
@Repository
public interface SinsalMasterRepository extends JpaRepository<SinsalMaster, Long> {

    /**
     * 신살 이름으로 조회
     */
    Optional<SinsalMaster> findBySinsalName(String sinsalName);

    /**
     * 신살 유형으로 조회 (길신/흉신)
     */
    List<SinsalMaster> findBySinsalType(SinsalType sinsalType);

    /**
     * 활성화된 신살만 조회
     */
    List<SinsalMaster> findByIsActive(Boolean isActive);

    /**
     * 활성화된 신살을 우선순위로 정렬하여 조회
     */
    List<SinsalMaster> findByIsActiveTrueOrderByPriorityDesc();
}
