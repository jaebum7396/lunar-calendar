package com.codism.repository;

import com.codism.model.entity.YukshipGanjiMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 육십갑자 마스터 리포지토리
 */
@Repository
public interface  YukshipGanjiMasterRepository extends JpaRepository<YukshipGanjiMaster, Long> {

    /**
     * 천간과 지지로 육십갑자 조회
     * @param cheongan 천간 (갑, 을, 병...)
     * @param jiji 지지 (자, 축, 인...)
     * @return 육십갑자 정보
     */
    Optional<YukshipGanjiMaster> findByCheonganAndJiji(String cheongan, String jiji);

    /**
     * 간지명으로 조회
     * @param ganjiName 간지명 (갑자, 을축...)
     * @return 육십갑자 정보
     */
    Optional<YukshipGanjiMaster> findByGanjiName(String ganjiName);
}
