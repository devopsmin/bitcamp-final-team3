-- 여행
DROP TABLE IF EXISTS trip RESTRICT;

-- 유저
DROP TABLE IF EXISTS user RESTRICT;

-- 쪽지
DROP TABLE IF EXISTS letter RESTRICT;

-- 댓글
DROP TABLE IF EXISTS comment RESTRICT;

-- 여행지유형대분류
DROP TABLE IF EXISTS firstclass RESTRICT;

-- 여행지정보
DROP TABLE IF EXISTS location RESTRICT;

-- SNS
DROP TABLE IF EXISTS sns RESTRICT;

-- 즐겨찾기
DROP TABLE IF EXISTS favor RESTRICT;

-- 좋아요
DROP TABLE IF EXISTS boardlike RESTRICT;

-- 여행이미지
DROP TABLE IF EXISTS tripimg RESTRICT;

-- 게시판
DROP TABLE IF EXISTS board RESTRICT;

-- 차단
DROP TABLE IF EXISTS ben RESTRICT;

-- 시도
DROP TABLE IF EXISTS city RESTRICT;

-- 시군구
DROP TABLE IF EXISTS state RESTRICT;

-- 여행테마
DROP TABLE IF EXISTS thema RESTRICT;

-- 여행일정
DROP TABLE IF EXISTS schedule RESTRICT;

-- 여행지유형중분류
DROP TABLE IF EXISTS secondclass RESTRICT;

-- 여행지유형소분류
DROP TABLE IF EXISTS thirdclass RESTRICT;

-- 차단분류
DROP TABLE IF EXISTS bentype RESTRICT;

-- 이미지첨부
DROP TABLE IF EXISTS attachedimage RESTRICT;

-- 게시글분류
DROP TABLE IF EXISTS boardtype RESTRICT;

-- 동행
DROP TABLE IF EXISTS companion RESTRICT;

-- 여행
CREATE TABLE trip (
	trip_no           INTEGER      NOT NULL, -- 여행번호
	user_no           INTEGER      NULL,     -- 유저번호
	thema_no          INTEGER      NULL,     -- 여행테마번호
	trip_title        VARCHAR(150) NOT NULL, -- 여행제목
	COL               DATE         NOT NULL, -- 여행시작일
	COL2              DATE         NOT NULL, -- 여행종료일
	trip_created_date DATETIME     NOT NULL DEFAULT now() -- 생성날짜
);

-- 여행
ALTER TABLE trip
	ADD CONSTRAINT PK_trip -- 여행 기본키
	PRIMARY KEY (
	trip_no -- 여행번호
	);

ALTER TABLE trip
	MODIFY COLUMN trip_no INTEGER NOT NULL AUTO_INCREMENT;

-- 유저
CREATE TABLE user (
	user_no            INTEGER      NOT NULL, -- 유저번호
	user_photo         VARCHAR(255) NULL,     -- 프로필사진
	user_lastest_login DATETIME     NOT NULL, -- 마지막로그인날짜
	user_email         VARCHAR(40)  NOT NULL, -- 이메일
	user_password      VARCHAR(32)  NOT NULL, -- 비밀번호
	user_tel           VARCHAR(30)  NOT NULL, -- 연락처
	user_created_date  DATETIME     NOT NULL DEFAULT now(), -- 가입일
	user_nickname      VARCHAR(50)  NOT NULL, -- 닉네임
	user_autherlize    TINYINT      NOT NULL, -- 권한여부
	user_block         TINYINT      NOT NULL, -- 차단여부
	sns_no             INTEGER      NULL      -- SNS번호
);

-- 유저
ALTER TABLE user
	ADD CONSTRAINT PK_user -- 유저 기본키
	PRIMARY KEY (
	user_no -- 유저번호
	);

-- 유저 유니크 인덱스
CREATE UNIQUE INDEX UIX_user
	ON user ( -- 유저
		user_email ASC -- 이메일
	);

-- 유저 유니크 인덱스2
CREATE UNIQUE INDEX UIX_user2
	ON user ( -- 유저
		user_nickname ASC -- 닉네임
	);

ALTER TABLE user
	MODIFY COLUMN user_no INTEGER NOT NULL AUTO_INCREMENT;

-- 쪽지
CREATE TABLE letter (
	letter_no        INTEGER      NOT NULL, -- 쪽지번호
	send_user_no     INTEGER      NULL,     -- 발신자번호
	receive_user_no  INTEGER      NULL,     -- 수신자번호
	letter_title     VARCHAR(150) NOT NULL, -- 쪽지제목
	letter_content   TEXT         NULL,     -- 쪽지내용
	letter_send_date DATETIME     NOT NULL DEFAULT now(), -- 발신일
	letter_check     TINYINT      NOT NULL  -- 읽음여부
);

-- 쪽지
ALTER TABLE letter
	ADD CONSTRAINT PK_letter -- 쪽지 기본키
	PRIMARY KEY (
	letter_no -- 쪽지번호
	);

ALTER TABLE letter
	MODIFY COLUMN letter_no INTEGER NOT NULL AUTO_INCREMENT;

-- 댓글
CREATE TABLE comment (
	comment_no           INTEGER  NOT NULL, -- 댓글번호
	board_no             INTEGER  NOT NULL, -- 게시판번호
	user_no              INTEGER  NOT NULL, -- 유저번호
	comment_content      TEXT     NOT NULL, -- 댓글내용
	comment_created_date DATETIME NOT NULL DEFAULT now() -- 댓글작성시간
);

-- 댓글
ALTER TABLE comment
	ADD CONSTRAINT PK_comment -- 댓글 기본키
	PRIMARY KEY (
	comment_no -- 댓글번호
	);

ALTER TABLE comment
	MODIFY COLUMN comment_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행지유형대분류
CREATE TABLE firstclass (
	firstclass_no   INTEGER     NOT NULL, -- 대분류번호
	firstclass_name VARCHAR(50) NULL      -- 분류명
);

-- 여행지유형대분류
ALTER TABLE firstclass
	ADD CONSTRAINT PK_firstclass -- 여행지유형대분류 기본키
	PRIMARY KEY (
	firstclass_no -- 대분류번호
	);

-- 여행지유형대분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_firstclass
	ON firstclass ( -- 여행지유형대분류
		firstclass_name ASC -- 분류명
	);

ALTER TABLE firstclass
	MODIFY COLUMN firstclass_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행지정보
CREATE TABLE location (
	location_no    INTEGER      NOT NULL, -- 여행지정보
	state_no       INTEGER      NULL,     -- 시군구번호
	thirdclass_no  INTEGER      NULL,     -- 소분류번호
	location_name  VARCHAR(50)  NULL,     -- 여행지이름
	location_desc  TEXT         NULL,     -- 여행지설명
	location_photo VARCHAR(255) NULL,     -- 여행지사진
	location_tel   VARCHAR(30)  NULL,     -- 연락처
	location_addr  VARCHAR(255) NULL,     -- 주소
	location_x     DOUBLE       NULL,     -- 위도
	location_y     DOUBLE       NULL      -- 경도
);

-- 여행지정보
ALTER TABLE location
	ADD CONSTRAINT PK_location -- 여행지정보 기본키
	PRIMARY KEY (
	location_no -- 여행지정보
	);

ALTER TABLE location
	MODIFY COLUMN location_no INTEGER NOT NULL AUTO_INCREMENT;

-- SNS
CREATE TABLE sns (
	sns_no   INTEGER     NOT NULL, -- SNS번호
	sns_name VARCHAR(50) NOT NULL  -- SNS이름
);

-- SNS
ALTER TABLE sns
	ADD CONSTRAINT PK_sns -- SNS 기본키
	PRIMARY KEY (
	sns_no -- SNS번호
	);

ALTER TABLE sns
	MODIFY COLUMN sns_no INTEGER NOT NULL AUTO_INCREMENT;

-- 즐겨찾기
CREATE TABLE favor (
	board_no       INTEGER  NOT NULL, -- 게시판번호
	user_no        INTEGER  NOT NULL, -- 유저번호
	favor_add_date DATETIME NOT NULL DEFAULT now() -- 추가일자
);

-- 즐겨찾기
ALTER TABLE favor
	ADD CONSTRAINT PK_favor -- 즐겨찾기 기본키
	PRIMARY KEY (
	board_no, -- 게시판번호
	user_no   -- 유저번호
	);

-- 좋아요
CREATE TABLE boardlike (
	board_no      INTEGER  NOT NULL, -- 게시판번호
	user_no       INTEGER  NOT NULL, -- 유저번호
	like_add_date DATETIME NOT NULL DEFAULT now() -- 추가일자
);

-- 좋아요
ALTER TABLE boardlike
	ADD CONSTRAINT PK_boardlike -- 좋아요 기본키
	PRIMARY KEY (
	board_no, -- 게시판번호
	user_no   -- 유저번호
	);

-- 여행이미지
CREATE TABLE tripimg (
	tripimg_no    INTEGER      NOT NULL, -- 여행이미지번호
	tripimg_photo VARCHAR(255) NOT NULL  -- 이미지
);

-- 여행이미지
ALTER TABLE tripimg
	ADD CONSTRAINT PK_tripimg -- 여행이미지 기본키
	PRIMARY KEY (
	tripimg_no -- 여행이미지번호
	);

ALTER TABLE tripimg
	MODIFY COLUMN tripimg_no INTEGER NOT NULL AUTO_INCREMENT;

-- 게시판
CREATE TABLE board (
	board_no           INTEGER      NOT NULL, -- 게시판번호
	boardtype_no       INTEGER      NULL,     -- 게시글분류번호
	board_title        VARCHAR(150) NOT NULL, -- 제목
	board_count        INTEGER      NOT NULL DEFAULT 0, -- 조회수
	board_created_date DATETIME     NOT NULL DEFAULT now(), -- 작성일
	user_no            INTEGER      NULL,     -- 유저번호
	trip_no            INTEGER      NULL,     -- 여행번호
	board_content      MEDIUMTEXT   NOT NULL, -- 내용
	board_tag          VARCHAR(15)  NULL      -- 태그
);

-- 게시판
ALTER TABLE board
	ADD CONSTRAINT PK_board -- 게시판 기본키
	PRIMARY KEY (
	board_no -- 게시판번호
	);

ALTER TABLE board
	MODIFY COLUMN board_no INTEGER NOT NULL AUTO_INCREMENT;

-- 차단
CREATE TABLE ben (
	user_no    INTEGER  NOT NULL, -- 유저번호
	bentype_no INTEGER  NULL,     -- 차단분류번호
	ben_desc   TEXT     NOT NULL, -- 차단내용
	ben_date   DATETIME NOT NULL DEFAULT now() -- 차단날짜
);

-- 차단
ALTER TABLE ben
	ADD CONSTRAINT PK_ben -- 차단 기본키
	PRIMARY KEY (
	user_no -- 유저번호
	);

-- 시도
CREATE TABLE city (
	city_no   INTEGER     NOT NULL, -- 시도번호
	city_name VARCHAR(50) NOT NULL  -- 시도명
);

-- 시도
ALTER TABLE city
	ADD CONSTRAINT PK_city -- 시도 기본키
	PRIMARY KEY (
	city_no -- 시도번호
	);

-- 시도 유니크 인덱스
CREATE UNIQUE INDEX UIX_city
	ON city ( -- 시도
		city_name ASC -- 시도명
	);

ALTER TABLE city
	MODIFY COLUMN city_no INTEGER NOT NULL AUTO_INCREMENT;

-- 시군구
CREATE TABLE state (
	state_no    INTEGER      NOT NULL, -- 시군구번호
	city_no     INTEGER      NULL,     -- 시도번호
	state_name  VARCHAR(50)  NOT NULL, -- 시군구명
	state_photo VARCHAR(255) NULL      -- 썸네일사진
);

-- 시군구
ALTER TABLE state
	ADD CONSTRAINT PK_state -- 시군구 기본키
	PRIMARY KEY (
	state_no -- 시군구번호
	);

-- 시군구 유니크 인덱스
CREATE UNIQUE INDEX UIX_state
	ON state ( -- 시군구
		state_name ASC -- 시군구명
	);

ALTER TABLE state
	MODIFY COLUMN state_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행테마
CREATE TABLE thema (
	thema_no   INTEGER     NOT NULL, -- 여행테마번호
	thema_name VARCHAR(50) NULL      -- 테마명
);

-- 여행테마
ALTER TABLE thema
	ADD CONSTRAINT PK_thema -- 여행테마 기본키
	PRIMARY KEY (
	thema_no -- 여행테마번호
	);

-- 여행테마 유니크 인덱스
CREATE UNIQUE INDEX UIX_thema
	ON thema ( -- 여행테마
		thema_name ASC -- 테마명
	);

ALTER TABLE thema
	MODIFY COLUMN thema_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행일정
CREATE TABLE schedule (
	schedule_no    INTEGER NOT NULL, -- 여행일정번호
	trip_no        INTEGER NULL,     -- 여행번호
	schedule_day   INTEGER NOT NULL DEFAULT 0, -- 여행일차
	schedule_route INTEGER NOT NULL DEFAULT 0, -- 여행순서
	location_no    INTEGER NULL      -- 여행지정보
);

-- 여행일정
ALTER TABLE schedule
	ADD CONSTRAINT PK_schedule -- 여행일정 기본키
	PRIMARY KEY (
	schedule_no -- 여행일정번호
	);

ALTER TABLE schedule
	MODIFY COLUMN schedule_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행지유형중분류
CREATE TABLE secondclass (
	secondclass_no   INTEGER     NOT NULL, -- 중분류번호
	firstclass_no    INTEGER     NULL,     -- 대분류번호
	secondclass_name VARCHAR(50) NOT NULL  -- 분류명
);

-- 여행지유형중분류
ALTER TABLE secondclass
	ADD CONSTRAINT PK_secondclass -- 여행지유형중분류 기본키
	PRIMARY KEY (
	secondclass_no -- 중분류번호
	);

-- 여행지유형중분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_secondclass
	ON secondclass ( -- 여행지유형중분류
		secondclass_name ASC -- 분류명
	);

ALTER TABLE secondclass
	MODIFY COLUMN secondclass_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행지유형소분류
CREATE TABLE thirdclass (
	thirdclass_no   INTEGER     NOT NULL, -- 소분류번호
	secondclass_no  INTEGER     NULL,     -- 중분류번호
	thirdclass_name VARCHAR(50) NOT NULL  -- 분류명
);

-- 여행지유형소분류
ALTER TABLE thirdclass
	ADD CONSTRAINT PK_thirdclass -- 여행지유형소분류 기본키
	PRIMARY KEY (
	thirdclass_no -- 소분류번호
	);

-- 여행지유형소분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_thirdclass
	ON thirdclass ( -- 여행지유형소분류
		thirdclass_name ASC -- 분류명
	);

ALTER TABLE thirdclass
	MODIFY COLUMN thirdclass_no INTEGER NOT NULL AUTO_INCREMENT;

-- 차단분류
CREATE TABLE bentype (
	bentype_no   INTEGER     NOT NULL, -- 차단분류번호
	bentype_name VARCHAR(50) NOT NULL  -- 차단분류명
);

-- 차단분류
ALTER TABLE bentype
	ADD CONSTRAINT PK_bentype -- 차단분류 기본키
	PRIMARY KEY (
	bentype_no -- 차단분류번호
	);

-- 차단분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_bentype
	ON bentype ( -- 차단분류
		bentype_name ASC -- 차단분류명
	);

ALTER TABLE bentype
	MODIFY COLUMN bentype_no INTEGER NOT NULL AUTO_INCREMENT;

-- 이미지첨부
CREATE TABLE attachedimage (
	board_no   INTEGER NULL, -- 게시판번호
	tripimg_no INTEGER NULL  -- 여행이미지번호
);

-- 게시글분류
CREATE TABLE boardtype (
	boardtype_no   INTEGER     NOT NULL, -- 게시글분류번호
	boardtype_name VARCHAR(50) NOT NULL  -- 게시글분류명
);

-- 게시글분류
ALTER TABLE boardtype
	ADD CONSTRAINT PK_boardtype -- 게시글분류 기본키
	PRIMARY KEY (
	boardtype_no -- 게시글분류번호
	);

-- 게시글분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_boardtype
	ON boardtype ( -- 게시글분류
		boardtype_name ASC -- 게시글분류명
	);

ALTER TABLE boardtype
	MODIFY COLUMN boardtype_no INTEGER NOT NULL AUTO_INCREMENT;

-- 동행
CREATE TABLE companion (
	companion_no  INTEGER NOT NULL, -- 동행번호
	board_no      INTEGER NOT NULL, -- 게시판번호
	companion_max INTEGER NOT NULL DEFAULT 0 -- 인원수
);

-- 동행
ALTER TABLE companion
	ADD CONSTRAINT PK_companion -- 동행 기본키
	PRIMARY KEY (
	companion_no -- 동행번호
	);

ALTER TABLE companion
	MODIFY COLUMN companion_no INTEGER NOT NULL AUTO_INCREMENT;

-- 여행
ALTER TABLE trip
	ADD CONSTRAINT FK_user_TO_trip -- 유저 -> 여행
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 여행
ALTER TABLE trip
	ADD CONSTRAINT FK_thema_TO_trip -- 여행테마 -> 여행
	FOREIGN KEY (
	thema_no -- 여행테마번호
	)
	REFERENCES thema ( -- 여행테마
	thema_no -- 여행테마번호
	);

-- 유저
ALTER TABLE user
	ADD CONSTRAINT FK_sns_TO_user -- SNS -> 유저
	FOREIGN KEY (
	sns_no -- SNS번호
	)
	REFERENCES sns ( -- SNS
	sns_no -- SNS번호
	);

-- 쪽지
ALTER TABLE letter
	ADD CONSTRAINT FK_user_TO_letter -- 유저 -> 쪽지
	FOREIGN KEY (
	send_user_no -- 발신자번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 쪽지
ALTER TABLE letter
	ADD CONSTRAINT FK_user_TO_letter2 -- 유저 -> 쪽지2
	FOREIGN KEY (
	receive_user_no -- 수신자번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 댓글
ALTER TABLE comment
	ADD CONSTRAINT FK_board_TO_comment -- 게시판 -> 댓글
	FOREIGN KEY (
	board_no -- 게시판번호
	)
	REFERENCES board ( -- 게시판
	board_no -- 게시판번호
	);

-- 댓글
ALTER TABLE comment
	ADD CONSTRAINT FK_user_TO_comment -- 유저 -> 댓글
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 여행지정보
ALTER TABLE location
	ADD CONSTRAINT FK_state_TO_location -- 시군구 -> 여행지정보
	FOREIGN KEY (
	state_no -- 시군구번호
	)
	REFERENCES state ( -- 시군구
	state_no -- 시군구번호
	);

-- 여행지정보
ALTER TABLE location
	ADD CONSTRAINT FK_thirdclass_TO_location -- 여행지유형소분류 -> 여행지정보
	FOREIGN KEY (
	thirdclass_no -- 소분류번호
	)
	REFERENCES thirdclass ( -- 여행지유형소분류
	thirdclass_no -- 소분류번호
	);

-- 즐겨찾기
ALTER TABLE favor
	ADD CONSTRAINT FK_board_TO_favor -- 게시판 -> 즐겨찾기
	FOREIGN KEY (
	board_no -- 게시판번호
	)
	REFERENCES board ( -- 게시판
	board_no -- 게시판번호
	);

-- 즐겨찾기
ALTER TABLE favor
	ADD CONSTRAINT FK_user_TO_favor -- 유저 -> 즐겨찾기
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 좋아요
ALTER TABLE boardlike
	ADD CONSTRAINT FK_board_TO_boardlike -- 게시판 -> 좋아요
	FOREIGN KEY (
	board_no -- 게시판번호
	)
	REFERENCES board ( -- 게시판
	board_no -- 게시판번호
	);

-- 좋아요
ALTER TABLE boardlike
	ADD CONSTRAINT FK_user_TO_boardlike -- 유저 -> 좋아요
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 게시판
ALTER TABLE board
	ADD CONSTRAINT FK_user_TO_board -- 유저 -> 게시판
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 게시판
ALTER TABLE board
	ADD CONSTRAINT FK_trip_TO_board -- 여행 -> 게시판
	FOREIGN KEY (
	trip_no -- 여행번호
	)
	REFERENCES trip ( -- 여행
	trip_no -- 여행번호
	);

-- 게시판
ALTER TABLE board
	ADD CONSTRAINT FK_boardtype_TO_board -- 게시글분류 -> 게시판
	FOREIGN KEY (
	boardtype_no -- 게시글분류번호
	)
	REFERENCES boardtype ( -- 게시글분류
	boardtype_no -- 게시글분류번호
	);

-- 차단
ALTER TABLE ben
	ADD CONSTRAINT FK_user_TO_ben -- 유저 -> 차단
	FOREIGN KEY (
	user_no -- 유저번호
	)
	REFERENCES user ( -- 유저
	user_no -- 유저번호
	);

-- 차단
ALTER TABLE ben
	ADD CONSTRAINT FK_bentype_TO_ben -- 차단분류 -> 차단
	FOREIGN KEY (
	bentype_no -- 차단분류번호
	)
	REFERENCES bentype ( -- 차단분류
	bentype_no -- 차단분류번호
	);

-- 시군구
ALTER TABLE state
	ADD CONSTRAINT FK_city_TO_state -- 시도 -> 시군구
	FOREIGN KEY (
	city_no -- 시도번호
	)
	REFERENCES city ( -- 시도
	city_no -- 시도번호
	);

-- 여행일정
ALTER TABLE schedule
	ADD CONSTRAINT FK_trip_TO_schedule -- 여행 -> 여행일정
	FOREIGN KEY (
	trip_no -- 여행번호
	)
	REFERENCES trip ( -- 여행
	trip_no -- 여행번호
	);

-- 여행일정
ALTER TABLE schedule
	ADD CONSTRAINT FK_location_TO_schedule -- 여행지정보 -> 여행일정
	FOREIGN KEY (
	location_no -- 여행지정보
	)
	REFERENCES location ( -- 여행지정보
	location_no -- 여행지정보
	);

-- 여행지유형중분류
ALTER TABLE secondclass
	ADD CONSTRAINT FK_firstclass_TO_secondclass -- 여행지유형대분류 -> 여행지유형중분류
	FOREIGN KEY (
	firstclass_no -- 대분류번호
	)
	REFERENCES firstclass ( -- 여행지유형대분류
	firstclass_no -- 대분류번호
	);

-- 여행지유형소분류
ALTER TABLE thirdclass
	ADD CONSTRAINT FK_secondclass_TO_thirdclass -- 여행지유형중분류 -> 여행지유형소분류
	FOREIGN KEY (
	secondclass_no -- 중분류번호
	)
	REFERENCES secondclass ( -- 여행지유형중분류
	secondclass_no -- 중분류번호
	);

-- 이미지첨부
ALTER TABLE attachedimage
	ADD CONSTRAINT FK_board_TO_attachedimage -- 게시판 -> 이미지첨부
	FOREIGN KEY (
	board_no -- 게시판번호
	)
	REFERENCES board ( -- 게시판
	board_no -- 게시판번호
	);

-- 이미지첨부
ALTER TABLE attachedimage
	ADD CONSTRAINT FK_tripimg_TO_attachedimage -- 여행이미지 -> 이미지첨부
	FOREIGN KEY (
	tripimg_no -- 여행이미지번호
	)
	REFERENCES tripimg ( -- 여행이미지
	tripimg_no -- 여행이미지번호
	);

-- 동행
ALTER TABLE companion
	ADD CONSTRAINT FK_board_TO_companion -- 게시판 -> 동행
	FOREIGN KEY (
	board_no -- 게시판번호
	)
	REFERENCES board ( -- 게시판
	board_no -- 게시판번호
	);