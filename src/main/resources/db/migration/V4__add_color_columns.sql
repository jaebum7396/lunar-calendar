-- ============================================
-- 오행 색상 정보 추가
-- ============================================

-- 천간 마스터에 색상 추가
ALTER TABLE cheongan_master ADD COLUMN IF NOT EXISTS color VARCHAR(20);
ALTER TABLE cheongan_master ADD COLUMN IF NOT EXISTS color_hex VARCHAR(10);

-- 지지 마스터에 색상 추가
ALTER TABLE jiji_master ADD COLUMN IF NOT EXISTS color VARCHAR(20);
ALTER TABLE jiji_master ADD COLUMN IF NOT EXISTS color_hex VARCHAR(10);

-- 천간 색상 업데이트 (오행 기준)
UPDATE cheongan_master SET color = '초록', color_hex = '#7FB77E' WHERE ohang = '목' AND eumyang = '양';  -- 갑
UPDATE cheongan_master SET color = '연두', color_hex = '#A8D5A8' WHERE ohang = '목' AND eumyang = '음';  -- 을

UPDATE cheongan_master SET color = '주황', color_hex = '#F5A97F' WHERE ohang = '화' AND eumyang = '양';  -- 병
UPDATE cheongan_master SET color = '살구', color_hex = '#F5C69D' WHERE ohang = '화' AND eumyang = '음';  -- 정

UPDATE cheongan_master SET color = '노랑', color_hex = '#F4D58D' WHERE ohang = '토' AND eumyang = '양';  -- 무
UPDATE cheongan_master SET color = '베이지', color_hex = '#F4E4CD' WHERE ohang = '토' AND eumyang = '음';  -- 기

UPDATE cheongan_master SET color = '회색', color_hex = '#9E9E9E' WHERE ohang = '금' AND eumyang = '양';  -- 경
UPDATE cheongan_master SET color = '은색', color_hex = '#C0C0C0' WHERE ohang = '금' AND eumyang = '음';  -- 신

UPDATE cheongan_master SET color = '파랑', color_hex = '#7EBCE6' WHERE ohang = '수' AND eumyang = '양';  -- 임
UPDATE cheongan_master SET color = '하늘색', color_hex = '#A8D8EA' WHERE ohang = '수' AND eumyang = '음';  -- 계

-- 지지 색상 업데이트 (오행 기준)
UPDATE jiji_master SET color = '초록', color_hex = '#7FB77E' WHERE ohang = '목' AND jiji_korean = '인';  -- 인목
UPDATE jiji_master SET color = '연두', color_hex = '#A8D5A8' WHERE ohang = '목' AND jiji_korean = '묘';  -- 묘목

UPDATE jiji_master SET color = '주황', color_hex = '#F5A97F' WHERE ohang = '화' AND jiji_korean = '오';  -- 오화
UPDATE jiji_master SET color = '살구', color_hex = '#F5C69D' WHERE ohang = '화' AND jiji_korean = '사';  -- 사화

UPDATE jiji_master SET color = '노랑', color_hex = '#F4D58D' WHERE ohang = '토' AND jiji_korean = '진';  -- 진토
UPDATE jiji_master SET color = '베이지', color_hex = '#F4E4CD' WHERE ohang = '토' AND jiji_korean = '술';  -- 술토
UPDATE jiji_master SET color = '베이지', color_hex = '#F4E4CD' WHERE ohang = '토' AND jiji_korean = '축';  -- 축토
UPDATE jiji_master SET color = '노랑', color_hex = '#F4D58D' WHERE ohang = '토' AND jiji_korean = '미';  -- 미토

UPDATE jiji_master SET color = '회색', color_hex = '#9E9E9E' WHERE ohang = '금' AND jiji_korean = '신';  -- 신금
UPDATE jiji_master SET color = '은색', color_hex = '#C0C0C0' WHERE ohang = '금' AND jiji_korean = '유';  -- 유금

UPDATE jiji_master SET color = '파랑', color_hex = '#7EBCE6' WHERE ohang = '수' AND jiji_korean = '자';  -- 자수
UPDATE jiji_master SET color = '하늘색', color_hex = '#A8D8EA' WHERE ohang = '수' AND jiji_korean = '해';  -- 해수

COMMENT ON COLUMN cheongan_master.color IS '오행 색상 (한글)';
COMMENT ON COLUMN cheongan_master.color_hex IS '오행 색상 (HEX 코드)';
COMMENT ON COLUMN jiji_master.color IS '오행 색상 (한글)';
COMMENT ON COLUMN jiji_master.color_hex IS '오행 색상 (HEX 코드)';
