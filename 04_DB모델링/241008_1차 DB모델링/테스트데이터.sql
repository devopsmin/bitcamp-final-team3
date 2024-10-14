-- SNS 테이블 더미 데이터
INSERT INTO sns (sns_name) VALUES 
('Google'), ('Naver'), ('KakaoTalk');

-- 유저 테이블 더미 데이터
INSERT INTO user (user_photo, user_lastest_login, user_email, user_password, user_tel, user_nickname, user_autherlize, user_block, sns_no) VALUES 
('profile1.jpg', NOW(), 'user1@example.com', 'password1', '010-1234-5678', '여행자1', 1, 0, 1),
('profile2.jpg', NOW(), 'user2@example.com', 'password2', '010-2345-6789', '모험가2', 1, 0, 2),
('profile3.jpg', NOW(), 'user3@example.com', 'password3', '010-3456-7890', '배낭족3', 1, 0, 3);

-- 여행테마 테이블 더미 데이터
INSERT INTO thema (thema_name) VALUES 
('문화여행'), ('자연여행'), ('맛집탐방'), ('힐링'), ('모험');

-- 여행 테이블 더미 데이터
INSERT INTO trip (user_no, thema_no, trip_title, COL, COL2) VALUES 
(1, 1, '서울 문화 투어', '2024-05-01', '2024-05-05'),
(2, 2, '제주도 자연 여행', '2024-06-15', '2024-06-20'),
(3, 3, '부산 맛집 탐방', '2024-07-10', '2024-07-15');

-- 시도 테이블 더미 데이터
INSERT INTO city (city_name) VALUES 
('서울특별시'), ('부산광역시'), ('제주특별자치도');

-- 시군구 테이블 더미 데이터
INSERT INTO state (city_no, state_name, state_photo) VALUES 
(1, '종로구', 'jongno.jpg'),
(2, '해운대구', 'haeundae.jpg'),
(3, '제주시', 'jeju.jpg');

-- 여행지유형대분류 테이블 더미 데이터
INSERT INTO firstclass (firstclass_name) VALUES 
('관광지'), ('문화시설'), ('축제공연행사'), ('여행코스'), ('레포츠');

-- 여행지유형중분류 테이블 더미 데이터
INSERT INTO secondclass (firstclass_no, secondclass_name) VALUES 
(1, '자연관광지'), (1, '관광자원'), (2, '박물관'), (2, '미술관'), (3, '문화관광축제');

-- 여행지유형소분류 테이블 더미 데이터
INSERT INTO thirdclass (secondclass_no, thirdclass_name) VALUES 
(1, '자연휴양림'), (1, '해변'), (2, '역사유적지'), (3, '대형박물관'), (4, '미술전시관');

-- 여행지정보 테이블 더미 데이터
INSERT INTO location (state_no, thirdclass_no, location_name, location_desc, location_photo, location_tel, location_addr, location_x, location_y) VALUES 
(1, 3, '경복궁', '조선시대 대표 궁궐', 'gyeongbokgung.jpg', '02-3700-3900', '서울특별시 종로구 사직로 161', 37.579617, 126.977041),
(2, 2, '해운대해수욕장', '부산의 대표 해수욕장', 'haeundae_beach.jpg', '051-749-7601', '부산광역시 해운대구 우동', 35.158888, 129.160278),
(3, 1, '한라산국립공원', '제주도의 상징', 'hallasan.jpg', '064-713-9950', '제주특별자치도 제주시 1100로 2070-61', 33.361666, 126.529166);

-- 게시글분류 테이블 더미 데이터
INSERT INTO boardtype (boardtype_name) VALUES 
('여행후기'), ('여행계획'), ('동행구하기'), ('질문과답변');

-- 게시판 테이블 더미 데이터
INSERT INTO board (boardtype_no, board_title, user_no, trip_no, board_content, board_tag) VALUES 
(1, '서울 문화 투어 후기', 1, 1, '서울의 문화를 체험한 5일간의 여행 후기입니다.', '#서울 #문화여행'),
(2, '제주도 자연 여행 계획', 2, 2, '제주도의 아름다운 자연을 만끽할 계획입니다.', '#제주도 #자연여행'),
(3, '부산 맛집 투어 동행 구해요', 3, 3, '부산의 맛집을 함께 돌아볼 동행을 구합니다.', '#부산 #맛집투어');

-- 댓글 테이블 더미 데이터
INSERT INTO comment (board_no, user_no, comment_content) VALUES 
(1, 2, '정말 좋은 여행 후기네요. 저도 꼭 가보고 싶어요!'),
(2, 3, '제주도 여행 계획 잘 보았습니다. 추천 장소가 있다면 알려주세요.'),
(3, 1, '부산 맛집 투어 정말 재밌을 것 같아요. 제가 함께 갈 수 있을까요?');

-- 좋아요 테이블 더미 데이터
INSERT INTO boardlike (board_no, user_no) VALUES 
(1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2);

-- 즐겨찾기 테이블 더미 데이터
INSERT INTO favor (board_no, user_no) VALUES 
(1, 3), (2, 1), (3, 2);

-- 여행일정 테이블 더미 데이터
INSERT INTO schedule (trip_no, schedule_day, schedule_route, location_no) VALUES 
(1, 1, 1, 1), (2, 1, 1, 3), (3, 1, 1, 2);

-- 여행이미지 테이블 더미 데이터
INSERT INTO tripimg (tripimg_photo) VALUES 
('seoul_trip1.jpg'), ('jeju_trip1.jpg'), ('busan_trip1.jpg');

-- 이미지첨부 테이블 더미 데이터
INSERT INTO attachedimage (board_no, tripimg_no) VALUES 
(1, 1), (2, 2), (3, 3);

-- 동행 테이블 더미 데이터
INSERT INTO companion (board_no, companion_max) VALUES 
(3, 3);

-- 차단분류 테이블 더미 데이터
INSERT INTO bentype (bentype_name) VALUES 
('스팸'), ('불법 컨텐츠'), ('부적절한 언어');

-- 차단 테이블 더미 데이터
INSERT INTO ben (user_no, bentype_no, ben_desc) VALUES 
(3, 1, '지속적인 광고성 게시글 작성');

-- 쪽지 테이블 더미 데이터
INSERT INTO letter (send_user_no, receive_user_no, letter_title, letter_content, letter_check) VALUES 
(1, 2, '여행 계획 문의', '제주도 여행 계획에 대해 궁금한 점이 있어요.', 0),
(2, 3, '동행 요청', '부산 맛집 투어에 함께 가고 싶습니다!', 0);