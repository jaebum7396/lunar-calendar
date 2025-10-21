# 사주 API - DB 기반 구현 완료

## 개요

완전한 **DB 기반 사주 API**가 구현되었습니다. 하드코딩 없이 PostgreSQL 데이터베이스에서 모든 마스터 데이터를 관리합니다.

## 주요 특징

✅ **하드코딩 제거**: 모든 데이터를 DB에서 관리
✅ **50개 신살 완료**: 길신 20개 + 흉신 30개, 상세 설명 포함
✅ **십성 상세 설명**: 성격, 운세, 직업 등 완전한 정보 (나무위키 기반)
✅ **오행 색상 지원**: 각 천간/지지에 오행 기반 색상 정보 (color, colorHex)
✅ **클릭 가능한 용어 API**: 십성/신살 용어 클릭 시 상세 정보 조회
✅ **한자 포함**: 모든 천간, 지지, 십성, 신살에 한자 표기
✅ **유연한 규칙 시스템**: rule 테이블로 계산 로직 관리
✅ **캐싱 적용**: 계산 결과를 메모리에 캐싱하여 성능 최적화

## 데이터베이스 구조

### 테이블 목록

1. **cheongan_master** (천간 마스터) - 10개
   - 갑(甲), 을(乙), 병(丙), 정(丁), 무(戊), 기(己), 경(庚), 신(辛), 임(壬), 계(癸)
   - 오행, 음양, 순서, **색상(color, colorHex)** 포함

2. **jiji_master** (지지 마스터) - 12개
   - 자(子), 축(丑), 인(寅), 묘(卯), 진(辰), 사(巳), 오(午), 미(未), 신(申), 유(酉), 술(戌), 해(亥)
   - 십이지 동물, 오행, 방위, 계절, 시간대, **색상(color, colorHex)** 포함

3. **sipsung_master** (십성 마스터) - 10개
   - 비견, 겁재, 식신, 상관, 편재, 정재, 편관, 정관, 편인, 정인
   - **상세 성격, 운세, 직업 설명** 포함 (나무위키 기반)

4. **sipsung_rule** (십성 계산 규칙) - 100개
   - 일간과 대상의 오행+음양에 따른 십성 매핑

5. **sinsal_master** (신살 마스터) - **50개** ✨
   - **길신 20개**: 천을귀인☀️, 천덕귀인🌟, 월덕귀인🌙, 삼기귀인🔱, 문창귀인📚, 학당귀인🎓, 금여록💰, 천의성🏥, 복성귀인🍀, 천관귀인👑, 덕합귀인🤝, 장성살⚔️, 태극귀인☯️, 진신귀인🐉, 연합귀인💕, 천사귀인🎭, 천후귀인👸, 천주귀인🏛️, 천령귀인🌌, 천록귀인💎
   - **흉신 30개**: 망신살⚠️, 박아살🔥, 도화살🌸, 역마살🐎, 백호대살🐯, 상문살🚪, 조객살👤, 양인살⚡, 현침살📌, 고란살🌊, 괴강살💀, 원진살🌀, 육해살🐍, 공망살🕳️, 천라지망🕸️, 과숙살👰, 과신살🤵, 화개살🎨, 천액살💧, 지살살🗡️, 연살살⚔️, 월살살🌙, 일살살☀️, 시살살⏰, 천중살⚖️, 육파살💥, 삼형살⛓️, 육충살💢, 귀문관살👻, 절지살🔚
   - **상세 설명, 긍정/부정 효과** 포함

6. **sinsal_rule** (신살 계산 규칙) - 가변
   - ILGAN_JIJI, YENJI_COMBINATION, JIJI_PATTERN 등 다양한 규칙 타입

7. **ohang_relation** (오행 상생상극) - 10개
   - 목→화→토→금→수→목 (상생)
   - 목극토, 화극금, 토극수, 금극목, 수극화 (상극)

## API 엔드포인트

### 1. 사주 상세 조회
```
GET /api/v1/saju/detail
```

**파라미터:**
- `birthDate` (필수): 생년월일 (YYYY-MM-DD)
- `birthTime` (선택): 출생 시간 (HH:mm, 기본값: 00:00)
- `isSolarCalendar` (선택): 양력 여부 (기본값: true)

**응답 예시:**
```json
{
  "birthDate": "1996-12-01",
  "birthTime": "07:00",
  "isSolarCalendar": true,
  "age": 28,
  "year": {
    "cheongan": "병",
    "cheonganHanja": "丙",
    "cheonganColor": "주황",
    "cheonganColorHex": "#F5A97F",
    "jiji": "자",
    "jijiHanja": "子",
    "jijiColor": "파랑",
    "jijiColorHex": "#7EBCE6",
    "sipsung": "편재"
  },
  "month": { ... },
  "day": { ... },
  "hour": { ... },
  "daeun": { ... },
  "seun": { ... },
  "sinsalList": [
    {
      "sinsalName": "천을귀인",
      "sinsalType": "길신",
      "icon": "☀️"
    },
    {
      "sinsalName": "도화살",
      "sinsalType": "흉신",
      "icon": "🌸"
    }
  ]
}
```

### 2. 간단 사주 조회 (시간 없이)
```
GET /api/v1/saju/simple?birthDate=1996-12-01
```

### 3. 십성 상세 정보 조회 ⭐
```
GET /api/v1/sipsung/{sipsungName}
```

**예시:**
```
GET /api/v1/sipsung/비견
GET /api/v1/sipsung/식신
GET /api/v1/sipsung/편재
```

**응답:**
```json
{
  "sipsungName": "비견",
  "sipsungHanja": "比肩",
  "sipsungCategory": "비겁",
  "relationshipType": "형제",
  "eumyangCondition": "same",
  "description": "나와 같은 오행. 형제, 동료, 경쟁자",
  "personality": "• 독립적이고 자립심이 강함\n• 경쟁심이 있고 승부욕이 강함\n• 자존심이 높고 고집이 셈...",
  "fortune": "• 형제, 친구와의 관계\n• 협력과 경쟁\n• 재물을 나누는 운..."
}
```

**사용처:** 프론트에서 십성 용어를 클릭했을 때 팝업/모달로 상세 설명 표시

### 4. 전체 십성 목록 조회
```
GET /api/v1/sipsung
```

**응답:** 10개 십성 전체 배열 (비견, 겁재, 식신, 상관, 편재, 정재, 편관, 정관, 편인, 정인)

### 5. 신살 상세 정보 조회 ⭐NEW
```
GET /api/v1/saju-info/sinsal/{name}
```

**예시:**
```
GET /api/v1/saju-info/sinsal/천을귀인
```

### 6. 모든 신살 목록 조회
```
GET /api/v1/saju-info/sinsal?type=길신
```

**파라미터:**
- `type` (선택): 신살 유형 필터 (길신/흉신)

## 파일 구조

```
C:\project\lunar-calendar\
├── src/main/java/com/codism/
│   ├── config/
│   │   └── CacheConfig.java                    # 캐시 설정
│   ├── controller/
│   │   ├── SajuDetailController.java           # 사주 계산 API
│   │   ├── SajuInfoController.java             # 신살 상세 조회 API
│   │   └── SipsungController.java              # 십성 상세 조회 API ⭐NEW
│   ├── model/
│   │   ├── entity/
│   │   │   ├── CheonganMaster.java            # 천간 엔티티 (색상 포함)
│   │   │   ├── JijiMaster.java                # 지지 엔티티 (색상 포함)
│   │   │   ├── SipsungMaster.java             # 십성 엔티티 (상세 설명 포함)
│   │   │   ├── SipsungRule.java               # 십성 규칙
│   │   │   ├── SinsalMaster.java              # 신살 엔티티 (상세 설명 포함)
│   │   │   ├── SinsalRule.java                # 신살 규칙
│   │   │   └── OhangRelation.java             # 오행 관계
│   │   └── dto/
│   │       ├── SajuDetailResponse.java         # 사주 응답 DTO (색상 포함)
│   │       └── SipsungDetailResponse.java      # 십성 상세 응답 DTO ⭐NEW
│   ├── repository/
│   │   ├── CheonganMasterRepository.java
│   │   ├── JijiMasterRepository.java
│   │   ├── SipsungMasterRepository.java
│   │   ├── SipsungRuleRepository.java
│   │   ├── SinsalMasterRepository.java
│   │   ├── SinsalRuleRepository.java
│   │   └── OhangRelationRepository.java
│   └── service/
│       ├── StemBranchCalculator.java          # 천간지지 계산 (수학적)
│       ├── SipSungCalculatorDB.java           # 십성 계산 (DB 기반)
│       ├── SinsalCalculatorDB.java            # 신살 계산 (DB 기반)
│       ├── SajuDetailService.java             # 사주 통합 서비스
│       └── SipsungDetailService.java          # 십성 상세 조회 서비스 ⭐NEW
└── src/main/resources/
    ├── db/migration/
    │   ├── V1__create_saju_tables.sql         # 테이블 생성
    │   ├── V2__insert_master_data.sql         # 천간/지지/십성 데이터 (상세 설명 포함)
    │   ├── V3__insert_sinsal_data.sql         # 신살 50개 데이터 (상세 설명 포함)
    │   └── V4__add_color_columns.sql          # 색상 컬럼 추가 및 데이터
    └── application-local.yml                   # DB 설정
```

## 삭제된 하드코딩 파일

❌ **SipSungCalculator.java** - 하드코딩된 십성 계산 (삭제됨)
❌ **SinsalCalculator.java** - 하드코딩된 신살 계산 (삭제됨)
❌ **V5__update_sipsung_descriptions.sql** - 십성 설명이 V2에 통합됨 (삭제됨)
❌ **V6__update_sinsal_descriptions.sql** - 신살 설명이 V3에 포함됨 (불필요)

## 데이터베이스 설정

### PostgreSQL 설치 및 데이터베이스 생성

```sql
CREATE DATABASE lunar_calendar;
```

### application-local.yml 설정

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/lunar_calendar
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
  flyway:
    enabled: true
    baseline-on-migrate: true
```

## 마이그레이션 실행

애플리케이션 실행 시 Flyway가 자동으로 마이그레이션을 실행합니다:

1. **V1**: 테이블 생성 (7개 테이블)
2. **V2**: 천간/지지/십성 마스터 데이터 삽입 (상세 설명 포함)
3. **V3**: 신살 50개 마스터 데이터 삽입 (상세 설명 포함)
4. **V4**: 색상 컬럼 추가 및 오행별 색상 데이터 업데이트

## 오행 색상 매핑

| 오행 | 음양 | 색상 | HEX 코드 |
|------|------|------|----------|
| 목(木) | 양 | 초록 | #7FB77E |
| 목(木) | 음 | 연두 | #A8D5A8 |
| 화(火) | 양 | 주황 | #F5A97F |
| 화(火) | 음 | 살구 | #F5C69D |
| 토(土) | 양 | 노랑 | #F4D58D |
| 토(土) | 음 | 베이지 | #F4E4CD |
| 금(金) | 양 | 회색 | #9E9E9E |
| 금(金) | 음 | 은색 | #C0C0C0 |
| 수(水) | 양 | 파랑 | #7EBCE6 |
| 수(水) | 음 | 하늘색 | #A8D8EA |

## 캐싱 전략

- **sipsung**: 십성 계산 결과 캐시 (일간 + 대상 천간)
- **sipsungDetail**: 십성 상세 정보 캐시
- **sinsal**: 신살 계산 결과 캐시 (사주 전체)
- **cheongan**: 천간 마스터 데이터 캐시
- **jiji**: 지지 마스터 데이터 캐시

## 확장 방법

### 신살 추가

1. `sinsal_master` 테이블에 신살 추가:
```sql
INSERT INTO sinsal_master (sinsal_name, sinsal_hanja, sinsal_type, icon, category, priority, description, detail_description, effect_positive, effect_negative, is_active)
VALUES ('새로운신살', '新神殺', '길신', '✨', '귀인', 50, '간단 설명', '상세 설명...', '긍정 효과', NULL, true);
```

2. `sinsal_rule` 테이블에 계산 규칙 추가:
```sql
INSERT INTO sinsal_rule (sinsal_id, rule_type, condition_ilgan, condition_target)
VALUES (51, 'ILGAN_JIJI', '["갑"]', '["자"]');
```

### 십성 설명 수정

십성 설명은 V2 migration 파일에서 직접 수정하거나, 운영 중에는 SQL로 수정:

```sql
UPDATE sipsung_master
SET description = '새로운 설명',
    personality = '새로운 성격 설명',
    fortune = '새로운 운세 설명'
WHERE sipsung_name = '비견';
```

## 성능 최적화

1. **인덱스**: 자주 조회되는 컬럼에 인덱스 적용
   - `cheongan_korean`, `jiji_korean`, `sipsung_name`, `sinsal_name` 등

2. **캐싱**: `@Cacheable` 어노테이션으로 반복 계산 방지

3. **Lazy Loading**: Entity 관계에서 필요시에만 로딩

## 테스트 방법

```bash
# Swagger UI 접속
http://localhost:8080/swagger-ui.html

# 사주 상세 조회
curl "http://localhost:8080/api/v1/saju/detail?birthDate=1996-12-01&birthTime=07:00&isSolarCalendar=true"

# 십성 상세 조회 ⭐NEW
curl "http://localhost:8080/api/v1/sipsung/비견"
curl "http://localhost:8080/api/v1/sipsung/식신"
curl "http://localhost:8080/api/v1/sipsung/편재"

# 전체 십성 목록 조회
curl "http://localhost:8080/api/v1/sipsung"

# 신살 상세 조회
curl "http://localhost:8080/api/v1/saju-info/sinsal/천을귀인"

# 모든 신살 목록 (길신만)
curl "http://localhost:8080/api/v1/saju-info/sinsal?type=길신"
```

## 주요 개선 사항

### Before (하드코딩)
```java
private static final Map<String, String> CHEONGAN_HANJA = new HashMap<>() {{
    put("갑", "甲"); put("을", "乙"); // ...
}};
```

### After (DB 기반)
```java
String cheonganHanja = cheonganMasterRepository.findByCheonganKorean(cheongan)
    .map(CheonganMaster::getCheonganHanja)
    .orElse(cheongan);
```

### 색상 정보 추가
```java
SajuPillar pillar = SajuPillar.builder()
    .cheongan(cheongan)
    .cheonganColor(cheonganMaster.getColor())        // "주황"
    .cheonganColorHex(cheonganMaster.getColorHex())  // "#F5A97F"
    .build();
```

## 데이터 완성도

✅ **천간 10개** - 한자, 오행, 음양, 색상 완료
✅ **지지 12개** - 한자, 동물, 오행, 색상 완료
✅ **십성 10개** - 한자, 상세 성격/운세 설명 완료 (나무위키 기반)
✅ **신살 50개** - 길신 20개 + 흉신 30개, 상세 설명 완료 (나무위키 기반)
✅ **오행 관계** - 상생 5개 + 상극 5개 완료
✅ **색상 매핑** - 오행별 양/음 색상 20개 완료

## 다음 단계 (선택사항)

- [ ] **24절기 기반 월주 계산** (현재는 음력 기준, 포스텔러는 절기 기준) ⚠️
- [ ] 지장간(地藏干) 구현
- [ ] 형충파해(刑沖破害) 구현
- [ ] 삼합/육합 구현
- [ ] 십이운성 구현
- [ ] 대운 계산 고도화 (현재는 간단 구현)
- [ ] 세운 계산 고도화 (월운, 일운 추가)
- [ ] 사주 궁합 API 구현
- [ ] 관리자 페이지 (신살/십성 데이터 관리)
- [ ] 더 많은 신살 추가 (현재 50개)

### ⚠️ 알려진 제한사항

**월주 계산 방식 차이:**
- 현재 구현: 음력 월 기준으로 월주 계산
- 포스텔러 만세력: 24절기(입춘, 경칩, 청명 등) 기준으로 월주 계산
- 예시: 1989년 6월 5일 출생
  - 음력: 1989년 5월 → 월지 오(午)
  - 절기: 망종(6월 6일) 이전 → 월지 사(巳)
- 절기 기반 구현 시도했으나 데이터 관리의 복잡도로 인해 보류
- 향후 한국천문연구원 API 또는 계산 라이브러리 활용 예정

## 결론

✅ **완전한 DB 기반 사주 API 구현 완료!**
✅ **하드코딩 제거 완료!**
✅ **50개 신살 데이터 완료!**
✅ **십성 상세 설명 완료!**
✅ **오행 색상 지원 완료!**
✅ **클릭 가능한 용어 API 완료!**
✅ **유지보수성 극대화!**

이제 코드 재배포 없이 DB에서 데이터만 수정하면 즉시 적용됩니다! 🎉

프론트엔드에서는:
1. 사주 화면에서 천간/지지를 `colorHex`로 표시
2. **십성 용어 클릭** → `/api/v1/sipsung/{sipsungName}` API로 상세 정보 팝업/모달 표시
3. **신살 클릭** → `/api/v1/saju-info/sinsal/{sinsalName}` API로 상세 정보 표시
4. 신살은 `icon`, `name`, `type`만 응답에 포함 (경량화 ✨)
5. 모든 정보가 DB에서 관리되므로 확장 용이

## 프론트엔드 연동 예시

### 십성 클릭 이벤트
```javascript
// 십성 용어를 클릭했을 때
const handleSipsungClick = async (sipsungName) => {
  const response = await fetch(`/api/v1/sipsung/${sipsungName}`);
  const data = await response.json();

  // 모달로 표시
  showModal({
    title: `${data.sipsungName} (${data.sipsungHanja})`,
    category: data.sipsungCategory,
    description: data.description,
    personality: data.personality,
    fortune: data.fortune
  });
};
```

### 신살 클릭 이벤트
```javascript
// 신살 용어를 클릭했을 때
const handleSinsalClick = async (sinsalName) => {
  const response = await fetch(`/api/v1/saju-info/sinsal/${sinsalName}`);
  const data = await response.json();

  // 모달로 표시
  showModal({
    title: `${data.sinsalName} (${data.sinsalHanja}) ${data.icon}`,
    type: data.sinsalType,
    description: data.detailDescription,
    positive: data.effectPositive,
    negative: data.effectNegative
  });
};
```
