-- SNS 테이블 더미 데이터
INSERT INTO sns (sns_name) VALUES
('Google'), ('Naver'), ('KakaoTalk');

-- 유저 테이블 더미 데이터
-- 비밀번호 전부 1111 / 각 유저 마지막번호(6,8,10,12) 차단되어있음
-- 생성일/마지막 로그인 날짜 NOW() 통일
-- 관리자 [1]
INSERT INTO user (user_lastest_login, user_email, user_password, user_tel, user_nickname, user_autherlize, user_block) VALUES
(NOW(), 'admin@example.com', '1111', '010-1234-5678', '관리자', 0, 0);
-- 일반유저 [2~6]
INSERT INTO user (user_lastest_login, user_email, user_password, user_tel, user_nickname, user_autherlize, user_block) VALUES
(NOW(), 'user1@example.com', '1111', '010-1111-1111', '회원1', 1, 0),
(NOW(), 'user2@example.com', '1111', '010-1212-1212', '회원2', 1, 0),
(NOW(), 'user3@example.com', '1111', '010-1313-1313', '회원3', 1, 0),
(NOW(), 'user4@example.com', '1111', '010-1414-1414', '회원4', 1, 0),
(NOW(), 'user5@example.com', '1111', '010-1515-1515', '회원5', 1, 1);
-- 소셜유저 [7~12]
INSERT INTO user (user_lastest_login, user_email, user_password, user_tel, user_nickname, user_autherlize, user_block, sns_no) VALUES
(NOW(), 'google1@google.com', '1111', '010-1234-5678', '구글1', 1, 0, 1),
(NOW(), 'google2@google.com', '1111', '010-2345-6789', '구글2', 1, 1, 1),
(NOW(), 'naver1@naver.com', '1111', '010-1234-1234', '네이버1', 1, 0, 2),
(NOW(), 'naver2@naver.com', '1111', '010-2345-2345', '네이버2', 1, 1, 2),
(NOW(), 'kakao1@kakao.com', '1111', '010-3456-3456', '카카오1', 1, 0, 3),
(NOW(), 'kakao2@kakao.com', '1111', '010-4567-4567', '카카오2', 1, 1, 3);

-- 차단분류 테이블 더미 데이터
-- 추가가능
INSERT INTO bentype (bentype_name) VALUES
('광고'),
('불법 컨텐츠'),
('부적절한 언어'),
('욕설'),
('운영자에 의한 차단');

-- 차단관련 더미 테이블
-- 차단 테이블 더미 데이터
INSERT INTO ben (user_no, bentype_no, ben_desc) VALUES
(6, 1, '지속적인 광고성 게시글 작성'),
(8, 2, '불법 컨텐츠 게시글 작성'),
(10, 3, '성희롱 적인 댓글 작성'),
(12, 4, '타 유저를 향한 비난적인 욕설 지속적 작성');


-- 쪽지 테이블 더미 데이터
-- 1번 관리자 (읽음)
-- 2번유저 -> 전체유저에게 하나씩
-- 8~12번 유저 읽음 표시
INSERT INTO letter (send_user_no, receive_user_no, letter_title, letter_content, letter_check) VALUES
(1, 6, '관리자 안내', '지속적인 광고성 게시글 작성으로 차단되었습니다.', 1),
(2, 3, '여행 계획 문의', '제주도 여행 계획에 대해 궁금한 점이 있어요.', 0),
(2, 4, '동행 요청', '부산 맛집 투어에 함께 가고 싶습니다!', 0),
(2, 5, '서울 여행 팁', '서울 문화 투어 다녀오셨다고 해서요. 좋은 장소 추천해주세요!', 0),
(2, 6, '맛집 정보 공유', '부산에서 꼭 가봐야 할 맛집 리스트 공유해 드릴게요.', 0),
(2, 7, '여행 동행 제안', '다음 달에 계획 중인 여행에 함께 가실래요?', 0),
(2, 8, '제주도 숙소 문의', '제주도 여행 계획 중이신데, 좋은 숙소 있나요?', 1),
(2, 9, '여행 후기 감사', '공유해주신 여행 후기 정말 도움 많이 됐어요. 감사합니다!', 1),
(2, 10, '맛집 투어 일정 조율', '부산 맛집 투어 일정을 조율하고 싶어요. 언제가 좋으세요?', 1),
(2, 11, '서울 방문 계획', '다음 주에 서울에 가는데, 만나서 이야기 나눌 수 있을까요?', 1),
(2, 12, '여행 조언 요청', '처음으로 해외여행을 가려고 해요. 조언 부탁드려요!', 1);

-- 시도
DROP TABLE IF EXISTS city RESTRICT;

-- 시군구
DROP TABLE IF EXISTS state RESTRICT;

-- 시도
CREATE TABLE city (
  city_no   INTEGER     NOT NULL COMMENT '시도번호', -- 시도번호
  city_code VARCHAR(50) NOT NULL COMMENT '시도코드', -- 시도코드
  city_name VARCHAR(50) NOT NULL COMMENT '시도명' -- 시도명
)
COMMENT '시도';

-- 시도
ALTER TABLE city
  ADD CONSTRAINT PK_city -- 시도 기본키
  PRIMARY KEY (
  city_no -- 시도번호
  );

-- 시도 유니크 인덱스
CREATE UNIQUE INDEX UIX_city
  ON city ( -- 시도
    city_code ASC -- 시도코드
  );

ALTER TABLE city
  MODIFY COLUMN city_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '시도번호';

-- 시군구
CREATE TABLE state (
  state_no    INTEGER      NOT NULL COMMENT '시군구번호', -- 시군구번호
  city_no     INTEGER      NULL     COMMENT '시도번호', -- 시도번호
  state_code  VARCHAR(50)  NOT NULL COMMENT '시군구코드', -- 시군구코드
  state_name  VARCHAR(50)  NOT NULL COMMENT '시군구명', -- 시군구명
  state_photo VARCHAR(255) NULL     COMMENT '썸네일사진' -- 썸네일사진
)
COMMENT '시군구';

-- 시군구
ALTER TABLE state
  ADD CONSTRAINT PK_state -- 시군구 기본키
  PRIMARY KEY (
  state_no -- 시군구번호
  );

-- 시군구 유니크 인덱스
CREATE UNIQUE INDEX UIX_state
  ON state ( -- 시군구
    state_code ASC -- 시군구코드
  );

ALTER TABLE state
  MODIFY COLUMN state_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '시군구번호';

-- 시군구
ALTER TABLE state
  ADD CONSTRAINT FK_city_TO_state -- 시도 -> 시군구
  FOREIGN KEY (
  city_no -- 시도번호
  )
  REFERENCES city ( -- 시도
  city_no -- 시도번호
  );
  
  -- 시도(city) 테이블 더미 데이터
INSERT INTO state (state_code, state_name) VALUES
('11', '서울시'),
('26', '부산시'),
('27', '대구시'),
('28', '인천시'),
('29', '광주시'),
('30', '대전시'),
('31', '울산시'),
('41', '경기도'),
('42', '강원도'),
('43', '충북도'),
('44', '충남도'),
('45', '전북도'),
('46', '전남도'),
('47', '경북도'),
('48', '경남도'),
('50', '제주도');

-- 시군구(state) 테이블 더미 데이터
INSERT INTO city (state_code, city_code, city_name) VALUES
-- 서울시
(11, '11110', '종로구'),
(11, '11140', '중구'),
(11, '11170', '용산구'),
(11, '11200', '성동구'),
(11, '11215', '광진구'),
(11, '11230', '동대문구'),
(11, '11260', '중랑구'),
(11, '11290', '성북구'),
(11, '11305', '강북구'),
(11, '11320', '도봉구'),
(11, '11350', '노원구'),
(11, '11380', '은평구'),
(11, '11410', '서대문구'),
(11, '11440', '마포구'),
(11, '11470', '양천구'),
(11, '11500', '강서구'),
(11, '11530', '구로구'),
(11, '11545', '금천구'),
(11, '11560', '영등포구'),
(11, '11590', '동작구'),
(11, '11620', '관악구'),
(11, '11650', '서초구'),
(11, '11680', '강남구'),
(11, '11710', '송파구'),
(11, '11740', '강동구'),

-- 부산시
(26, '26110', '중구'),
(26, '26140', '서구'),
(26, '26170', '동구'),
(26, '26200', '영도구'),
(26, '26230', '부산진구'),
(26, '26260', '동래구'),
(26, '26290', '남구'),
(26, '26320', '북구'),
(26, '26350', '해운대구'),
(26, '26380', '사하구'),
(26, '26410', '금정구'),
(26, '26440', '강서구'),
(26, '26470', '연제구'),
(26, '26500', '수영구'),
(26, '26530', '사상구'),
(26, '26710', '기장군'),

-- 대구시
(27, '27110', '중구'),
(27, '27140', '동구'),
(27, '27170', '서구'),
(27, '27200', '남구'),
(27, '27230', '북구'),
(27, '27260', '수성구'),
(27, '27290', '달서구'),
(27, '27710', '달성군'),

-- 인천시
(28, '28110', '중구'),
(28, '28140', '동구'),
(28, '28170', '남구'),
(28, '28185', '연수구'),
(28, '28200', '남동구'),
(28, '28237', '부평구'),
(28, '28245', '계양구'),
(28, '28260', '서구'),
(28, '28710', '강화군'),
(28, '28720', '옹진군'),

-- 광주시
(29, '29110', '동구'),
(29, '29140', '서구'),
(29, '29155', '남구'),
(29, '29170', '북구'),
(29, '29200', '광산구'),

-- 대전시
(30, '30110', '동구'),
(30, '30140', '중구'),
(30, '30170', '서구'),
(30, '30200', '유성구'),
(30, '30230', '대덕구'),

-- 울산시
(31, '31110', '중구'),
(31, '31140', '남구'),
(31, '31170', '동구'),
(31, '31200', '북구'),
(31, '31710', '울주군'),

-- 경기도
(41, '41110', '수원시'),
(41, '41111', '수원시 장안구'),
(41, '41113', '수원시 권선구'),
(41, '41115', '수원시 팔달구'),
(41, '41117', '수원시 영통구'),
(41, '41130', '성남시'),
(41, '41131', '성남시 수정구'),
(41, '41133', '성남시 중원구'),
(41, '41135', '성남시 분당구'),
(41, '41150', '의정부시'),
(41, '41170', '안양시'),
(41, '41171', '안양시 만안구'),
(41, '41173', '안양시 동안구'),
(41, '41190', '부천시'),
(41, '41210', '광명시'),
(41, '41220', '평택시'),
(41, '41250', '동두천시'),
(41, '41270', '안산시'),
(41, '41271', '안산시 상록구'),
(41, '41273', '안산시 단원구'),
(41, '41280', '고양시'),
(41, '41281', '고양시 덕양구'),
(41, '41285', '고양시 일산동구'),
(41, '41287', '고양시 일산서구'),
(41, '41290', '과천시'),
(41, '41310', '구리시'),
(41, '41360', '남양주시'),
(41, '41370', '오산시'),
(41, '41390', '시흥시'),
(41, '41410', '군포시'),
(41, '41430', '의왕시'),
(41, '41450', '하남시'),
(41, '41460', '용인시'),
(41, '41461', '용인시 처인구'),
(41, '41463', '용인시 기흥구'),
(41, '41465', '용인시 수지구'),
(41, '41480', '파주시'),
(41, '41500', '이천시'),
(41, '41550', '안성시'),
(41, '41570', '김포시'),
(41, '41590', '화성시'),
(41, '41610', '광주시'),
(41, '41630', '양주시'),
(41, '41650', '포천시'),
(41, '41730', '여주군'),
(41, '41800', '연천군'),
(41, '41820', '가평군'),
(41, '41830', '양평군'),

-- 강원도
(42, '42110', '춘천시'),
(42, '42130', '원주시'),
(42, '42150', '강릉시'),
(42, '42170', '동해시'),
(42, '42190', '태백시'),
(42, '42210', '속초시'),
(42, '42230', '삼척시'),
(42, '42720', '홍천군'),
(42, '42730', '횡성군'),
(42, '42750', '영월군'),
(42, '42760', '평창군'),
(42, '42770', '정선군'),
(42, '42780', '철원군'),
(42, '42790', '화천군'),
(42, '42800', '양구군'),
(42, '42810', '인제군'),
(42, '42820', '고성군'),
(42, '42830', '양양군'),

-- 충북도
(43, '43110', '청주시'),
(43, '43111', '청주시 상당구'),
(43, '43113', '청주시 흥덕구'),
(43, '43130', '충주시'),
(43, '43150', '제천시'),
(43, '43710', '청원군'),
(43, '43720', '보은군'),
(43, '43730', '옥천군'),
(43, '43740', '영동군'),
(43, '43745', '증평군'),
(43, '43750', '진천군'),
(43, '43760', '괴산군'),
(43, '43770', '음성군'),
(43, '43800', '단양군'),

-- 충남도
(44, '44130', '천안시'),
(44, '44131', '천안시 동남구'),
(44, '44133', '천안시 서북구'),
(44, '44150', '공주시'),
(44, '44180', '보령시'),
(44, '44200', '아산시'),
(44, '44210', '서산시'),
(44, '44230', '논산시'),
(44, '44250', '계룡시'),
(44, '44710', '금산군'),
(44, '44730', '연기군'),
(44, '44760', '부여군'),
(44, '44770', '서천군'),
(44, '44790', '청양군'),
(44, '44800', '홍성군'),
(44, '44810', '예산군'),
(44, '44825', '태안군'),

-- 전북도
(45, '45110', '전주시'),
(45, '45111', '전주시 완산구'),
(45, '45113', '전주시 덕진구'),
(45, '45130', '군산시'),
(45, '45140', '익산시'),
(45, '45180', '정읍시'),
(45, '45190', '남원시'),
(45, '45210', '김제시'),
(45, '45710', '완주군'),
(45, '45720', '진안군'),
(45, '45730', '무주군'),
(45, '45740', '장수군'),
(45, '45750', '임실군'),
(45, '45770', '순창군'),
(45, '45790', '고창군'),
(45, '45800', '부안군'),

-- 전남도
(46, '46110', '목포시'),
(46, '46130', '여수시'),
(46, '46150', '순천시'),
(46, '46170', '나주시'),
(46, '46230', '광양시'),
(46, '46710', '담양군'),
(46, '46720', '곡성군'),
(46, '46730', '구례군'),
(46, '46770', '고흥군'),
(46, '46780', '보성군'),
(46, '46790', '화순군'),
(46, '46800', '장흥군'),
(46, '46810', '강진군'),
(46, '46820', '해남군'),
(46, '46830', '영암군'),
(46, '46840', '무안군'),
(46, '46860', '함평군'),
(46, '46870', '영광군'),
(46, '46880', '장성군'),
(46, '46890', '완도군'),
(46, '46900', '진도군'),
(46, '46910', '신안군'),

-- 경북도
(47, '47110', '포항시'),
(47, '47111', '포항시 남구'),
(47, '47113', '포항시 북구'),
(47, '47130', '경주시'),
(47, '47150', '김천시'),
(47, '47170', '안동시'),
(47, '47190', '구미시'),
(47, '47210', '영주시'),
(47, '47230', '영천시'),
(47, '47250', '상주시'),
(47, '47280', '문경시'),
(47, '47290', '경산시'),
(47, '47720', '군위군'),
(47, '47730', '의성군'),
(47, '47750', '청송군'),
(47, '47760', '영양군'),
(47, '47770', '영덕군'),
(47, '47820', '청도군'),
(47, '47830', '고령군'),
(47, '47840', '성주군'),
(47, '47850', '칠곡군'),
(47, '47900', '예천군'),
(47, '47920', '봉화군'),
(47, '47930', '울진군'),
(47, '47940', '울릉군'),

-- 경남도
(48, '48120', '창원시'),
(48, '48121', '창원시 의창구'),
(48, '48123', '창원시 성산구'),
(48, '48125', '창원시 마산합포구'),
(48, '48127', '창원시 마산회원구'),
(48, '48129', '창원시 진해구'),
(48, '48170', '진주시'),
(48, '48220', '통영시'),
(48, '48240', '사천시'),
(48, '48250', '김해시'),
(48, '48270', '밀양시'),
(48, '48310', '거제시'),
(48, '48330', '양산시'),
(48, '48720', '의령군'),
(48, '48730', '함안군'),
(48, '48740', '창녕군'),
(48, '48820', '고성군'),
(48, '48840', '남해군'),
(48, '48850', '하동군'),
(48, '48860', '산청군'),
(48, '48870', '함양군'),
(48, '48880', '거창군'),
(48, '48890', '합천군'),

-- 제주도
(50, '50110', '제주시'),
(50, '50130', '서귀포시');

-- 여행지유형대분류 더미 데이터
INSERT INTO firstclass (firstclass_code, firstclass_name) VALUES
('A01', '자연'),
('A02', '인문(문화/예술/역사)'),
('A03', '레포츠'),
('A04', '쇼핑'),
('A05', '음식'),
('B02', '숙박'),
('C01', '추천코스');

-- 여행지유형중분류 더미 데이터
INSERT INTO secondclass (firstclass_code, secondclass_code, secondclass_name) VALUES
('A01', 'A0101', '자연관광지'),
('A01', 'A0102', '관광자원'),
('A02', 'A0201', '역사관광지'),
('A02', 'A0202', '휴양관광지'),
('A02', 'A0203', '체험관광지'),
('A02', 'A0204', '산업관광지'),
('A02', 'A0205', '건축/조형물'),
('A02', 'A0206', '문화시설'),
('A02', 'A0207', '축제'),
('A02', 'A0208', '공연/행사'),
('A03', 'A0301', '레포츠소개'),
('A03', 'A0302', '육상 레포츠'),
('A03', 'A0303', '수상 레포츠'),
('A03', 'A0304', '항공 레포츠'),
('A03', 'A0305', '복합 레포츠'),
('A04', 'A0401', '쇼핑'),
('A05', 'A0502', '음식점'),
('B02', 'B0201', '숙박시설'),
('C01', 'C0112', '가족코스'),
('C01', 'C0113', '나홀로코스'),
('C01', 'C0114', '힐링코스'),
('C01', 'C0115', '도보코스'),
('C01', 'C0116', '캠핑코스'),
('C01', 'C0117', '맛코스');

-- 여행지유형소분류 더미 데이터
INSERT INTO thirdclass (secondclass_code, thirdclass_code, thirdclass_name) VALUES
( 'A0101', 'A01010100', '국립공원'),
( 'A0101', 'A01010200', '도립공원'),
( 'A0101', 'A01010300', '군립공원'),
( 'A0101', 'A01010400', '산'),
( 'A0101', 'A01010500', '자연생태관광지'),
( 'A0101', 'A01010600', '자연휴양림'),
( 'A0101', 'A01010700', '수목원'),
( 'A0101', 'A01010800', '폭포'),
( 'A0101', 'A01010900', '계곡'),
( 'A0101', 'A01011000', '약수터'),
( 'A0101', 'A01011100', '해안절경'),
( 'A0101', 'A01011200', '해수욕장'),
( 'A0101', 'A01011300', '섬'),
( 'A0101', 'A01011400', '항구/포구'),
( 'A0101', 'A01011600', '등대'),
( 'A0101', 'A01011700', '호수'),
( 'A0101', 'A01011800', '강'),
( 'A0101', 'A01011900', '동굴'),
( 'A0102', 'A01020100', '희귀동.식물'),
( 'A0102', 'A01020200', '기암괴석'),
( 'A0201', 'A02010100', '고궁'),
( 'A0201', 'A02010200', '성'),
( 'A0201', 'A02010300', '문'),
( 'A0201', 'A02010400', '고택'),
( 'A0201', 'A02010500', '생가'),
( 'A0201', 'A02010600', '민속마을'),
( 'A0201', 'A02010700', '유적지/사적지'),
( 'A0201', 'A02010800', '사찰'),
( 'A0201', 'A02010900', '종교성지'),
( 'A0201', 'A02011000', '안보관광'),
( 'A0202', 'A02020200', '관광단지'),
( 'A0202', 'A02020300', '온천/욕장/스파'),
( 'A0202', 'A02020400', '이색찜질방'),
( 'A0202', 'A02020500', '헬스투어'),
( 'A0202', 'A02020600', '테마공원'),
( 'A0202', 'A02020700', '공원'),
( 'A0202', 'A02020800', '유람선/잠수함관광'),
( 'A0203', 'A02030100', '농.산.어촌 체험'),
( 'A0203', 'A02030200', '전통체험'),
( 'A0203', 'A02030300', '산사체험'),
( 'A0203', 'A02030400', '이색체험'),
( 'A0203', 'A02030600', '이색거리'),
( 'A0204', 'A02040400', '발전소'),
( 'A0204', 'A02040600', '식음료'),
( 'A0204', 'A02040800', '기타'),
( 'A0204', 'A02040900', '전자-반도체'),
( 'A0204', 'A02041000', '자동차'),
( 'A0205', 'A02050100', '다리/대교'),
( 'A0205', 'A02050200', '기념탑/기념비/전망대'),
( 'A0205', 'A02050300', '분수'),
( 'A0205', 'A02050400', '동상'),
( 'A0205', 'A02050500', '터널'),
( 'A0205', 'A02050600', '유명건물'),
( 'A0206', 'A02060100', '박물관'),
( 'A0206', 'A02060200', '기념관'),
( 'A0206', 'A02060300', '전시관'),
( 'A0206', 'A02060400', '컨벤션센터'),
( 'A0206', 'A02060500', '미술관/화랑'),
( 'A0206', 'A02060600', '공연장'),
( 'A0206', 'A02060700', '문화원'),
( 'A0206', 'A02060800', '외국문화원'),
( 'A0206', 'A02060900', '도서관'),
( 'A0206', 'A02061000', '대형서점'),
( 'A0206', 'A02061100', '문화전수시설'),
( 'A0206', 'A02061200', '영화관'),
( 'A0206', 'A02061300', '어학당'),
( 'A0206', 'A02061400', '학교'),
( 'A0207', 'A02070100', '문화관광축제'),
( 'A0207', 'A02070200', '일반축제'),
( 'A0208', 'A02080100', '전통공연'),
( 'A0208', 'A02080200', '연극'),
( 'A0208', 'A02080300', '뮤지컬'),
( 'A0208', 'A02080400', '오페라'),
( 'A0208', 'A02080500', '전시회'),
( 'A0208', 'A02080600', '박람회'),
( 'A0208', 'A02080800', '무용'),
( 'A0208', 'A02080900', '클래식음악회'),
( 'A0208', 'A02081000', '대중콘서트'),
( 'A0208', 'A02081100', '영화'),
( 'A0208', 'A02081200', '스포츠경기'),
( 'A0208', 'A02081300', '기타행사'),
( 'A0301', 'A03010200', '수상레포츠'),
( 'A0301', 'A03010300', '항공레포츠'),
( 'A0302', 'A03020200', '수련시설'),
( 'A0302', 'A03020300', '경기장'),
( 'A0302', 'A03020400', '인라인(실내 인라인 포함)'),
( 'A0302', 'A03020500', '자전거하이킹'),
( 'A0302', 'A03020600', '카트'),
( 'A0302', 'A03020700', '골프'),
( 'A0302', 'A03020800', '경마'),
( 'A0302', 'A03020900', '경륜'),
( 'A0302', 'A03021000', '카지노'),
( 'A0302', 'A03021100', '승마'),
( 'A0302', 'A03021200', '스키/스노보드'),
( 'A0302', 'A03021300', '스케이트'),
( 'A0302', 'A03021400', '썰매장'),
( 'A0302', 'A03021500', '수렵장'),
( 'A0302', 'A03021600', '사격장'),
( 'A0302', 'A03021700', '야영장,오토캠핑장'),
( 'A0302', 'A03021800', '암벽등반'),
( 'A0302', 'A03022000', '서바이벌게임'),
( 'A0302', 'A03022100', 'ATV'),
( 'A0302', 'A03022200', 'MTB'),
( 'A0302', 'A03022300', '오프로드'),
( 'A0302', 'A03022400', '번지점프'),
( 'A0302', 'A03022600', '스키(보드 렌탈샵)'),
( 'A0302', 'A03022700', '트래킹'),
( 'A0303', 'A03030100', '윈드서핑/제트스키'),
( 'A0303', 'A03030200', '카약/카누'),
( 'A0303', 'A03030300', '요트'),
( 'A0303', 'A03030400', '스노쿨링/스킨스쿠버다이빙'),
( 'A0303', 'A03030500', '민물낚시'),
( 'A0303', 'A03030600', '바다낚시'),
( 'A0303', 'A03030700', '수영'),
( 'A0303', 'A03030800', '래프팅'),
( 'A0304', 'A03040100', '스카이다이빙'),
( 'A0304', 'A03040200', '초경량비행'),
( 'A0304', 'A03040300', '헹글라이딩/패러글라이딩'),
( 'A0304', 'A03040400', '열기구'),
( 'A0305', 'A03050100', '복합 레포츠'),
( 'A0401', 'A04010100', '5일장'),
( 'A0401', 'A04010200', '상설시장'),
( 'A0401', 'A04010300', '백화점'),
( 'A0401', 'A04010400', '면세점'),
( 'A0401', 'A04010500', '대형마트'),
( 'A0401', 'A04010600', '전문매장/상가'),
( 'A0401', 'A04010700', '공예/공방'),
( 'A0401', 'A04010900', '특산물판매점'),
( 'A0401', 'A04011000', '사후면세점'),
( 'A0502', 'A05020100', '한식'),
( 'A0502', 'A05020200', '서양식'),
( 'A0502', 'A05020300', '일식'),
( 'A0502', 'A05020400', '중식'),
( 'A0502', 'A05020700', '이색음식점'),
( 'A0502', 'A05020900', '카페/전통찻집'),
( 'A0502', 'A05021000', '클럽'),
( 'B0201', 'B02010100', '관광호텔'),
( 'B0201', 'B02010500', '콘도미니엄'),
( 'B0201', 'B02010600', '유스호스텔'),
( 'B0201', 'B02010700', '펜션'),
( 'B0201', 'B02010900', '모텔'),
( 'B0201', 'B02011000', '민박'),
( 'B0201', 'B02011100', '게스트하우스'),
( 'B0201', 'B02011200', '홈스테이'),
( 'B0201', 'B02011300', '서비스드레지던스'),
( 'B0201', 'B02011600', '한옥'),
( 'C0112', 'C01120001', '가족코스'),
( 'C0113', 'C01130001', '나홀로코스'),
( 'C0114', 'C01140001', '힐링코스'),
( 'C0115', 'C01150001', '도보코스'),
( 'C0116', 'C01160001', '캠핑코스'),
( 'C0117', 'C01170001', '맛코스');

-- 여행테마 테이블 더미 데이터
INSERT INTO thema (thema_name) VALUES 
('문화여행'), ('자연여행'), ('맛집탐방'), ('힐링'), ('모험');

-- 여행지 테이블 더미 데이터
insert into location(city_code,thirdclass_code,location_name,location_desc,location_photo,location_tel,location_addr,location_x,location_y, locationclass_no) values
('50110','A01010100',"여행지1","여행지설명1","여행지사진1위치","010-1111-2222","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010200',"여행지2","여행지설명2","여행지사진2위치","010-1111-2223","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010300',"여행지3","여행지설명3","여행지사진3위치","010-1111-2224","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010400',"여행지4","여행지설명4","여행지사진4위치","010-1111-2225","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010500',"여행지5","여행지설명5","여행지사진5위치","010-1111-2226","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010600',"여행지6","여행지설명6","여행지사진6위치","010-1111-2227","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010700',"여행지7","여행지설명7","여행지사진7위치","010-1111-2228","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010800',"여행지8","여행지설명8","여행지사진8위치","010-1111-2229","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01010900',"여행지9","여행지설명9","여행지사진9위치","010-1111-2230","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011000',"여행지10","여행지설명10","여행지사진10위치","010-1111-2231","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011100',"여행지11","여행지설명11","여행지사진11위치","010-1111-2232","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011200',"여행지12","여행지설명12","여행지사진12위치","010-1111-2233","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011300',"여행지13","여행지설명13","여행지사진13위치","010-1111-2234","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011400',"여행지14","여행지설명14","여행지사진14위치","010-1111-2235","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011600',"여행지15","여행지설명15","여행지사진15위치","010-1111-2236","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011700',"여행지16","여행지설명16","여행지사진16위치","010-1111-2237","제주도 제주시",33.4996213,126.5311884,1),
('50110','A01011800',"여행지17","여행지설명17","여행지사진17위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),

('50110','B02010100',"숙박지1","숙박지설명1","숙박지사진1위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),
('50110','B02010500',"숙박지2","숙박지설명2","숙박지사진2위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),
('50110','B02010600',"숙박지3","숙박지설명3","숙박지사진3위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),
('50110','B02010700',"숙박지4","숙박지설명4","숙박지사진4위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),
('50110','B02010900',"숙박지5","숙박지설명5","숙박지사진5위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),
('50110','B02011000',"숙박지6","숙박지설명6","숙박지사진6위치","010-1111-2238","제주도 제주시",33.4996213,126.5311884,1),

('50130','A01011900',"여행지18","여행지설명18","여행지사진18위치","010-1111-2239","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A01020100',"여행지19","여행지설명19","여행지사진19위치","010-1111-2240","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A01020200',"여행지20","여행지설명20","여행지사진20위치","010-1111-2241","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010100',"여행지21","여행지설명21","여행지사진21위치","010-1111-2242","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010200',"여행지22","여행지설명22","여행지사진22위치","010-1111-2243","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010300',"여행지23","여행지설명23","여행지사진23위치","010-1111-2244","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010400',"여행지24","여행지설명24","여행지사진24위치","010-1111-2245","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010500',"여행지25","여행지설명25","여행지사진25위치","010-1111-2246","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010600',"여행지26","여행지설명26","여행지사진26위치","010-1111-2247","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010700',"여행지27","여행지설명27","여행지사진27위치","010-1111-2248","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010800',"여행지28","여행지설명28","여행지사진28위치","010-1111-2249","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02010900',"여행지29","여행지설명29","여행지사진29위치","010-1111-2250","제주도 서귀포시",33.2541205,126.560076,1),
('50130','A02011000',"여행지30","여행지설명30","여행지사진30위치","010-1111-2251","제주도 서귀포시",33.2541205,126.560076,1),

('50130', 'B02011100',"숙박지7","숙박지설명7","숙박지사진7위치","010-1111-2238","제주도 서귀포시",33.4996213,126.5311884,1),
('50130', 'B02011200',"숙박지8","숙박지설명8","숙박지사진8위치","010-1111-2238","제주도 서귀포시",33.4996213,126.5311884,1),
('50130', 'B02011300',"숙박지9","숙박지설명9","숙박지사진9위치","010-1111-2238","제주도 서귀포시",33.4996213,126.5311884,1),
('50130', 'B02011600',"숙박지10","숙박지설명10","숙박지사진10위치","010-1111-2238","제주도 서귀포시",33.4996213,126.5311884,1);


INSERT INTO final_project.locationclass (locationclass_no, locationclass_name) VALUES
(1, 'API호출'),
(2, '사용자저장');


insert into trip(user_no,thema_no,trip_title,start_date,end_date,trip_created_date) values
(1,1,"제목1","2024-01-01","2024-02-01","2023-12-01"),
(2,2,"제목2","2024-01-02","2024-02-02","2023-12-02"),
(3,3,"제목3","2024-01-03","2024-02-03","2023-12-03");

insert into schedule(location_no,trip_no,schedule_day,schedule_route) values
(1,1,1,1),
(2,1,1,2),
(3,1,1,3),
(4,1,1,4),
(5,1,2,1),
(6,1,2,2),
(7,1,2,3),
(8,1,2,4),
(9,1,3,1),
(10,1,3,2),
(11,1,3,3),
(12,2,1,1),
(13,2,1,2),
(14,2,1,3),
(15,2,1,4),
(16,2,2,1),
(17,2,2,2),
(18,2,2,3),
(19,3,1,1),
(20,3,1,2),
(21,3,1,3),
(22,3,1,4),
(23,3,1,5),
(24,3,2,1),
(25,3,2,2),
(26,3,2,3),
(27,3,2,4),
(28,3,3,1),
(29,3,3,2),
(30,3,3,3);

-- 게시판 타입
insert into boardtype(boardtype_no, boardtype_name) values
  (1, '여행질문'),
  (2, '여행동행'),
  (3, '여행후기');

-- 여행질문 -- trip_no 다름
INSERT INTO board (boardtype_no, board_title, board_count, board_created_date, user_no, trip_no, board_content, board_tag)
VALUES
(1, '여름휴가 추천지 어디인가요?', 34, NOW(), 2, 1, '올여름 어디로 여행을 갈지 고민 중입니다. 추천 부탁드립니다!', '여름휴가'),
(1, '해외여행 보험 꼭 들어야 하나요?', 21, NOW(), 3, 2, '해외여행 갈 때 보험은 필수인가요? 경험 있으신 분들 의견 궁금합니다.', '보험'),
(1, '제주도에서 가장 맛있는 음식은?', 45, NOW(), 4, 1, '제주도 여행 예정입니다. 추천할 만한 음식점이 있을까요?', '제주도'),
(1, '동남아 여행 시 필수 준비물은?', 67, NOW(), 5, 1, '동남아시아 여행할 때 꼭 챙겨야 할 물건이 뭐가 있을까요?', '준비물'),
(1, '유럽여행 비자 발급 문의', 12, NOW(), 6, 2, '유럽 여행 시 비자는 어떻게 준비해야 하나요?', '비자'),
(1, '일본 온천 추천 부탁드려요', 40, NOW(), 7, 2, '일본 여행에서 온천을 가고 싶어요. 추천할 만한 곳 있을까요?', '온천'),
(1, '국내 힐링 여행지 추천해주세요', 55, NOW(), 8, 2, '일상에 지친 몸과 마음을 치유할 수 있는 국내 여행지 추천 부탁드려요.', '힐링'),
(1, '비행기 멀미 극복 방법 알려주세요!', 10, NOW(), 9, 2, '비행기에서 멀미가 심합니다. 좋은 방법 있을까요?', '멀미'),
(1, '겨울여행 준비물 리스트 공유 부탁드려요', 27, NOW(), 10, 3, '겨울 여행 시 필수 준비물이 무엇인지 궁금합니다.', '겨울여행'),
(1, '미국 여행 시 팁 문화 어떻게 해야 하나요?', 30, NOW(), 11, 3, '미국 여행에서 팁은 얼마나 줘야 할까요?', '팁');


-- 여행동행 더미데이터
INSERT INTO board (boardtype_no, board_title, board_count, board_created_date, user_no, trip_no, board_content, board_tag)
VALUES
(2, '스위스 등산 동행 구합니다.', 3, NOW(), 4, 2, '알프스 하이킹 함께 하실 분을 구해요! 일행과 일정 조정 가능해요.', '동행'),
(2, '도쿄 디즈니 동행 찾습니다.', 5, NOW(), 8, 1, '디즈니랜드 가실 분? 놀이기구 좋아하시는 분이면 더 좋겠네요.', '디즈니'),
(2, '제주도 렌터카 같이 쓰실 분?', 7, NOW(), 11, 3, '렌터카 함께 사용하실 분 연락 주세요! 숙소도 같이 알아볼까요?', '렌터카'),
(2, '파리 루브르 박물관 동행 구해요.', 2, NOW(), 5, 2, '미술관 투어 좋아하는 분들과 함께하면 좋을 것 같아요.', '미술관'),
(2, '스위스 겨울 여행 동행 구합니다.', 6, NOW(), 9, 2, '눈 구경과 스키를 즐길 동행 찾습니다. 초보자도 환영이에요.', '겨울여행'),
(2, '제주 오름 등반 같이 하실 분?', 10, NOW(), 3, 2, '오름 트레킹 좋아하시는 분들과 같이 가고 싶어요.', '오름'),
(2, '일본 온천 투어 동행 구해요.', 4, NOW(), 7, 1, '온천 즐기러 가실 분 찾습니다. 일정 유연하게 맞출 수 있어요.', '온천'),
(2, '제주도 해안도로 자전거 여행', 3, NOW(), 12, 3, '해안도로를 자전거로 달리고 싶어요! 같이 하실 분?', '자전거'),
(2, '파리 에펠탑 야경 투어 동행', 8, NOW(), 2, 2, '에펠탑 근처 야경을 함께 즐길 분 구해요. 간단한 식사도 어때요?', '야경'),
(2, '제주도 카페 투어 동행 구합니다.', 9, NOW(), 10, 3, '카페 투어 좋아하시는 분 함께해요! 핫플 가보고 싶어요.', '카페');


-- 여행후기 더미데이터 --맨앞 board_no지움
INSERT INTO board
(boardtype_no, board_title, board_count, board_created_date, user_no, trip_no, board_content, board_tag) 
VALUES
(3, '도쿄 디즈니랜드 정말 최고였어요!', 18, NOW(), 6, 1, '디즈니랜드에서 하루 종일 신나게 놀았어요. 꼭 가볼 만한 곳입니다!', '디즈니'),
(3, '스위스의 자연 경관은 감동이었어요.', 25, NOW(), 3, 2, '알프스의 풍경이 너무 아름다웠어요. 하이킹을 강력 추천합니다.', '알프스'),
(3, '제주도에서 최고의 오름을 만나다.', 12, NOW(), 8, 3, '백록담 등반은 힘들었지만 멋진 풍경에 보람을 느꼈어요.', '백록담'),
(3, '파리에서의 예술 여행 후기', 19, NOW(), 4, 2, '루브르와 오르세 미술관에서 하루 종일 예술에 빠졌어요.', '미술관'),
(3, '겨울 스위스 여행에서 스키를 즐기다.', 22, NOW(), 7, 2, '스키와 온천을 모두 즐길 수 있어서 완벽한 여행이었어요.', '스키'),
(3, '제주도 카페 투어 후기!', 16, NOW(), 2, 3, '아름다운 카페에서 여유로운 시간을 보냈어요. 협재 근처 추천합니다.', '카페'),
(3, '일본 온천 체험기', 10, NOW(), 5, 1, '처음 가본 온천이었는데 정말 좋았어요. 다음엔 더 오래 있고 싶네요.', '온천'),
(3, '파리 에펠탑 야경을 보며.', 14, NOW(), 9, 2, '에펠탑 근처에서 와인 한 잔과 야경을 즐겼어요. 정말 로맨틱했습니다.', '야경'),
(3, '제주도 해변에서의 하루', 13, NOW(), 12, 3, '해변에서 하루 종일 놀며 힐링했어요. 제주도는 언제 가도 좋네요.', '해변'),
(3, '도쿄 맛집 탐방 후기', 20, NOW(), 10, 1, '라멘과 초밥을 원 없이 먹고 왔어요. 미슐랭 가게도 추천합니다.', '맛집');


-- 게시판 좋아요 테이블 더미 데이터
-- 모든 회원 1번 게시글 좋아요
-- 1번회원 모든글 좋아요 (1~30)
INSERT INTO boardlike (user_no, board_no) VALUES
(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),
(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),
(2,11),(2,12),(2,13),(2,14),(2,15),(2,16),(2,17),(2,18),(2,19),(2,20),
(2,21),(2,22),(2,23),(2,24),(2,25),(2,26),(2,27),(2,28),(2,29),(2,30);


-- 즐겨찾기 테이블 더미 데이터
-- 모든 회원 1번 게시글 즐겨찾기
-- 1번회원 모든글 즐겨찾기 (1~30)
INSERT INTO favor (user_no, board_no) VALUES
(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),
(2,2),(2,3),(2,4),(2,5),(2,6),(2,7),(2,8),(2,9),(2,10),
(2,11),(2,12),(2,13),(2,14),(2,15),(2,16),(2,17),(2,18),(2,19),(2,20),
(2,21),(2,22),(2,23),(2,24),(2,25),(2,26),(2,27),(2,28),(2,29),(2,30);

-- 댓글 더미 데이터
-- 1번 질문
-- 2번 동행
-- 3번 후기 게시글이라고 생각하고 넣었음
-- 유저 2번은 모든 게시글에 댓글 포함되어있음
INSERT INTO comment (board_no, user_no, comment_content) VALUES
-- 1번 게시글 (여행 일정 질문)에 대한 댓글
(1, 2, '제주도 여행 계획 잘 보았습니다. 성산일출봉도 추천드려요!'),
(1, 3, '우도에 가보셨나요? 꼭 들러보세요.'),
(1, 4, '렌터카를 이용하시면 더 편하게 여행하실 수 있을 거예요.'),
(1, 5, '제주 흑돼지는 꼭 드셔보세요. 맛있어요!'),
(1, 6, '한라산 등반 계획이 있으신가요? 체력 관리 잘 하셔야 해요.'),
(1, 7, '제주 동문시장에서 현지 음식을 맛보는 것도 좋아요.'),
(1, 8, '카멜리아 힐은 봄에 가면 정말 예쁘더라구요.'),
(1, 9, '제주 오름 투어도 추천드립니다.'),
(1, 10, '해변 드라이브 코스도 알아보시면 좋을 것 같아요.'),
(1, 11, '제주 민속촌에 가보시는 건 어떨까요? 제주의 문화를 알 수 있어요.'),


-- 2번 게시글 (동행구하기)에 대한 댓글
(11, 2, '저도 부산 여행 계획 중이에요. 함께 가면 좋을 것 같아요!'),
(11, 4, '언제 가실 예정인가요? 일정이 맞으면 같이 가고 싶네요.'),
(11, 5, '부산 맛집 리스트 있으신가요? 저도 관심 있어요.'),
(11, 6, '해운대 비치 쪽에서 숙소 잡으실 건가요?'),
(11, 7, '부산 야경 투어에 관심 있으신가요? 같이 가면 좋을 것 같아요.'),
(11, 8, '동백섬 일출 보러 가실 생각 있으세요?'),
(11, 9, '부산 시장 투어도 재밌을 것 같아요. 관심 있으신가요?'),
(11, 10, '김밥 투어는 어떠세요? 부산 유명 김밥 맛집들을 돌아볼 수 있어요.'),
(11, 11, '부산 여행 경험 있으신 분 계신가요? 팁 좀 알려주세요.'),
(11, 12, '저도 동행 구해요! 함께 즐거운 여행 만들어봐요.'),

-- 3번 게시글 (여행후기)에 대한 댓글
(21, 2, '정말 좋은 여행 후기네요. 저도 꼭 가보고 싶어요!'),
(21, 5, '사진이 정말 예쁘네요. 어떤 카메라로 찍으셨어요?'),
(21, 6, '맛집 추천 감사합니다. 다음에 갈 때 꼭 가볼게요.'),
(21, 7, '숙소는 어디서 잡으셨나요? 위치가 좋아보여요.'),
(21, 8, '현지 교통은 어떻게 이용하셨어요? 팁 좀 알려주세요.'),
(21, 9, '여행 일정 정말 알차네요. 참고해서 저도 계획 세워봐야겠어요.'),
(21, 10, '혹시 현지에서 특별한 체험 같은 건 해보셨나요?'),
(21, 11, '글 읽으면서 마치 제가 여행 다녀온 것 같아요. 생생한 후기 감사합니다!'),
(21,  3, '여행 경비는 얼마 정도 들었나요? 계획 세우는 데 참고하고 싶어서요.'),
(21, 12, '다음엔 어디로 여행 가실 계획인가요? 또 이런 좋은 후기 기대할게요!');