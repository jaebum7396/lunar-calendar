package com.codism.repository;

import com.codism.model.entity.OhangRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 오행 관계 Repository
 */
@Repository
public interface OhangRelationRepository extends JpaRepository<OhangRelation, Long> {

    /**
     * 오행 관계 조회 (source -> target)
     */
    Optional<OhangRelation> findBySourceOhangAndTargetOhang(String sourceOhang, String targetOhang);

    /**
     * 특정 오행에서 시작하는 모든 관계 조회
     */
    List<OhangRelation> findBySourceOhang(String sourceOhang);

    /**
     * 특정 오행으로 끝나는 모든 관계 조회
     */
    List<OhangRelation> findByTargetOhang(String targetOhang);

    /**
     * 관계 유형으로 조회 (상생/상극)
     */
    List<OhangRelation> findByRelationType(String relationType);
}
