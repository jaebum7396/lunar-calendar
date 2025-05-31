# Lunar Calendar API (천간지지 계산 서비스)

명리학 기반 천간지지(干支) 계산 서비스입니다. 순수한 계산 로직만 제공하는 stateless 서비스로 설계되었습니다.

## 📋 개요

생년월일과 출생시간을 바탕으로 전통 명리학의 천간지지를 계산하여 사주팔자를 제공하는 RESTful API 서비스입니다. 양력과 음력 모두 지원하며, 시간이 제공되면 완전한 사주팔자(년월일시)를, 시간이 없으면 년월일만 계산합니다.

## 🛠️ 기술 스택

- **Java 17**
- **Spring Boot 3.2.3**
- **Spring Web** (RESTful API)
- **Swagger/OpenAPI 3** (API 문서화)
- **Gradle** (빌드 도구)

## 🚀 주요 기능

### 1. 천간지지 계산
- **년주(年柱)**: 출생년도의 천간지지
- **월주(月柱)**: 출생월의 천간지지  
- **일주(日柱)**: 출생일의 천간지지
- **시주(時柱)**: 출생시간의 천간지지 (선택사항)

### 2. 양력/음력 지원
- 양력 기준 계산
- 음력 기준 계산 (음력-양력 자동 변환)

### 3. 유연한 입력 형식
- 날짜: `YYYY-MM-DD` 형식
- 시간: `HH:mm` 또는 `HH` 형식 (선택사항)

## 📡 API 엔드포인트

### 천간지지 계산

```http
GET /api/v1
```

#### 파라미터

| 파라미터 | 타입 | 필수 | 설명 | 예시 |
|---------|------|------|------|------|
| `birthDate` | String | ✅ | 생년월일 (YYYY-MM-DD) | `1990-05-15` |
| `isSolarCalendar` | Boolean | ✅ | 양력/음력 구분 (true: 양력, false: 음력) | `true` |
| `birthTime` | String | ❌ | 출생시간 (HH:mm 또는 HH) | `14:30` |

#### 응답 예시

**시간 포함 (완전한 사주팔자)**
```json
{
  "yearStemBranch": "경오",
  "monthStemBranch": "신사", 
  "dayStemBranch": "을유",
  "timeStemBranch": "계미"
}
```

**시간 미포함 (년월일만)**
```json
{
  "yearStemBranch": "경오",
  "monthStemBranch": "신사",
  "dayStemBranch": "을유", 
  "timeStemBranch": null
}
```

## 💡 사용 예시

### 1. 완전한 사주팔자 계산
```bash
curl "http://localhost:8080/api/v1?birthDate=1990-05-15&birthTime=14:30&isSolarCalendar=true"
```

### 2. 년월일만 계산
```bash
curl "http://localhost:8080/api/v1?birthDate=1990-05-15&isSolarCalendar=true"
```

### 3. 음력 기준 계산
```bash
curl "http://localhost:8080/api/v1?birthDate=1990-04-21&isSolarCalendar=false"
```

## 🔧 설정

### application.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: lunar-calendar
  mvc:
    format:
      date-time: "yyyy-MM-dd HH:mm:ss"

logging:
  level:
    com:
      codism: info
    root: error
```

## 🏃‍♂️ 실행 방법

### 1. 개발 환경 실행
```bash
# 의존성 설치 및 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 또는 JAR 파일 실행
java -jar build/libs/lunar-calendar-0.0.1-SNAPSHOT.jar
```

### 2. Docker 실행
```bash
# Docker 이미지 빌드
docker build -t lunar-calendar .

# 컨테이너 실행
docker run -p 8080:8080 lunar-calendar
```

### 3. 간단한 테스트
```bash
# API 테스트 (년월일만)
curl "http://localhost:8080/api/v1?birthDate=1990-05-15&isSolarCalendar=true"

# API 테스트 (시간 포함)
curl "http://localhost:8080/api/v1?birthDate=1990-05-15&birthTime=14:30&isSolarCalendar=true"

# 음력 테스트
curl "http://localhost:8080/api/v1?birthDate=1990-04-21&isSolarCalendar=false"
```

## 📚 API 문서

서비스 실행 후 Swagger UI를 통해 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui/index.html
```

## 🌐 서비스 URL

- **로컬 개발**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

## 🧮 천간지지 기본 지식

### 천간 (天干)
갑(甲), 을(乙), 병(丙), 정(丁), 무(戊), 기(己), 경(庚), 신(辛), 임(壬), 계(癸)

### 지지 (地支)  
자(子), 축(丑), 인(寅), 묘(卯), 진(辰), 사(巳), 오(午), 미(未), 신(申), 유(酉), 술(戌), 해(亥)

### 시간별 지지
- 자시(子時): 23:00~01:00
- 축시(丑時): 01:00~03:00
- 인시(寅時): 03:00~05:00
- 묘시(卯時): 05:00~07:00
- 진시(辰時): 07:00~09:00
- 사시(巳時): 09:00~11:00
- 오시(午時): 11:00~13:00
- 미시(未時): 13:00~15:00
- 신시(申時): 15:00~17:00
- 유시(酉時): 17:00~19:00
- 술시(戌時): 19:00~21:00
- 해시(亥時): 21:00~23:00

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 라이선스

이 프로젝트는 MIT 라이선스 하에 있습니다. 자세한 내용은 [LICENSE](LICENSE) 파일을 참조하세요.

## 📞 연락처

프로젝트 링크: [https://github.com/jaebum7396/lunar-calendar](https://github.com/jaebum7396/lunar-calendar)
