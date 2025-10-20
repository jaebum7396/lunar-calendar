package com.codism.config;

import com.codism.repository.CheonganMasterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 애플리케이션 시작 시 마스터 데이터 초기화
 * 테이블이 비어있을 때만 data.sql을 실행합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Order(Integer.MAX_VALUE)
public class DataInitializer implements CommandLineRunner {

    private final CheonganMasterRepository cheonganMasterRepository;
    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        // Hibernate가 테이블을 생성할 시간을 줌
        Thread.sleep(1000);

        // 천간 마스터 테이블이 비어있는지 확인
        long count = cheonganMasterRepository.count();

        if (count == 0) {
            log.info("=== 초기 데이터가 없습니다. data.sql을 실행합니다. ===");
            executeDataSql();
        }
    }

    private void executeDataSql() {
        try {
            log.info("=== data.sql 실행 시작 ===");
            ClassPathResource resource = new ClassPathResource("data.sql");

            // ScriptUtils를 사용하여 SQL 스크립트 실행 (주석과 여러 줄 문장을 올바르게 처리)
            ScriptUtils.executeSqlScript(dataSource.getConnection(), resource);

            log.info("=== 초기 데이터 삽입 완료 ===");
        } catch (Exception e) {
            log.error("초기 데이터 삽입 중 오류 발생", e);
        }
    }
}
