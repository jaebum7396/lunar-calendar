package com.codism.service;

import com.codism.model.entity.CheonganMaster;
import com.codism.model.entity.JijiMaster;
import com.codism.model.entity.YukshipGanjiMaster;
import com.codism.repository.CheonganMasterRepository;
import com.codism.repository.JijiMasterRepository;
import com.codism.repository.YukshipGanjiMasterRepository;
import com.codism.util.ColorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 간지 특징 조합 서비스
 * 육십갑자의 특징 + 천간 색상 + 지지 동물 조합
 */
@Service
@RequiredArgsConstructor
public class GanjiCharacteristicService {

    private final YukshipGanjiMasterRepository yukshipGanjiMasterRepository;
    private final CheonganMasterRepository cheonganMasterRepository;
    private final JijiMasterRepository jijiMasterRepository;

    /**
     * 간지 조합의 전체 특징 문구 생성
     * 예: "센스 있는 검은 원숭이"
     *
     * @param cheongan 천간 (갑, 을, 병...)
     * @param jiji 지지 (자, 축, 인...)
     * @return 특징 + 색상형용사 + 동물 조합 문구
     */
    public String getFullCharacteristic(String cheongan, String jiji) {
        // 1. 육십갑자에서 특징 조회
        String characteristic = yukshipGanjiMasterRepository
                .findByCheonganAndJiji(cheongan, jiji)
                .map(YukshipGanjiMaster::getCharacteristic)
                .orElse("");

        // 2. 천간에서 색상 조회 및 형용사 변환
        String colorAdjective = cheonganMasterRepository
                .findByCheonganKorean(cheongan)
                .map(CheonganMaster::getColor)
                .map(ColorUtils::convertColorToAdjective)
                .orElse("");

        // 3. 지지에서 동물 조회
        String animal = jijiMasterRepository
                .findByJijiKorean(jiji)
                .map(JijiMaster::getAnimal)
                .orElse("");

        // 4. 조합: "특징 + 색상형용사 + 동물"
        return String.format("%s %s %s", characteristic, colorAdjective, animal).trim();
    }

    /**
     * 천간의 색상 형용사만 반환
     * @param cheongan 천간
     * @return 색상 형용사 (푸른, 붉은, 누런, 흰, 검은)
     */
    public String getColorAdjective(String cheongan) {
        return cheonganMasterRepository
                .findByCheonganKorean(cheongan)
                .map(CheonganMaster::getColor)
                .map(ColorUtils::convertColorToAdjective)
                .orElse("");
    }

    /**
     * 지지의 동물명만 반환
     * @param jiji 지지
     * @return 동물명 (쥐, 소, 호랑이...)
     */
    public String getAnimal(String jiji) {
        return jijiMasterRepository
                .findByJijiKorean(jiji)
                .map(JijiMaster::getAnimal)
                .orElse("");
    }

    /**
     * 육십갑자의 특징만 반환
     * @param cheongan 천간
     * @param jiji 지지
     * @return 특징 (센스 있는, 영리한, 용감한...)
     */
    public String getCharacteristic(String cheongan, String jiji) {
        return yukshipGanjiMasterRepository
                .findByCheonganAndJiji(cheongan, jiji)
                .map(YukshipGanjiMaster::getCharacteristic)
                .orElse("");
    }

    /**
     * 색상 + 동물 조합 (특징 제외)
     * 예: "검은 원숭이"
     *
     * @param cheongan 천간
     * @param jiji 지지
     * @return 색상형용사 + 동물
     */
    public String getColorAnimal(String cheongan, String jiji) {
        String colorAdjective = getColorAdjective(cheongan);
        String animal = getAnimal(jiji);
        return String.format("%s %s", colorAdjective, animal).trim();
    }
}
