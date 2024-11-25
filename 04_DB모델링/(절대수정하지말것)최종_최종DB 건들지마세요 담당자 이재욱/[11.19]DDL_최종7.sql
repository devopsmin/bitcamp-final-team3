-- 현재버전 :
-- BEN Table 수정 / 차단해제 날짜 추가 / BEN Table 제약조건 수정

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

-- 게시판
DROP TABLE IF EXISTS board RESTRICT;

-- 차단
DROP TABLE IF EXISTS ben RESTRICT;

-- 시도
DROP TABLE IF EXISTS state RESTRICT;

-- 시군구
DROP TABLE IF EXISTS city RESTRICT;

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

-- 게시판이미지
DROP TABLE IF EXISTS boardimage RESTRICT;

-- 게시글분류
DROP TABLE IF EXISTS boardtype RESTRICT;

-- 동행
DROP TABLE IF EXISTS companion RESTRICT;

-- 여행지구분
DROP TABLE IF EXISTS locationtype RESTRICT;

-- 동행정보
DROP TABLE IF EXISTS companioninfo RESTRICT;

-- 댓글 좋아요
DROP TABLE IF EXISTS commentlike RESTRICT;

-- 동행테이블
DROP TABLE IF EXISTS companionrecruit RESTRICT;

-- 동행신청 테이블
DROP TABLE IF EXISTS companionapply RESTRICT;


-- View 삭제
DROP VIEW boardlike_count_view;
DROP VIEW favor_count_view;
DROP VIEW commentlike_count_view;


-- 여행
CREATE TABLE trip (
                      trip_no           INTEGER      NOT NULL COMMENT '여행번호', -- 여행번호
                      user_no           BIGINT       NULL     COMMENT '유저번호', -- 유저번호
                      thema_no          INTEGER      NULL     COMMENT '여행테마번호', -- 여행테마번호
                      city_code         VARCHAR(50)  NULL     COMMENT '시군구코드', -- 시군구코드
                      trip_title        VARCHAR(150) NULL     COMMENT '여행제목', -- 여행제목
                      start_date        DATE         NULL     COMMENT '여행시작일', -- 여행시작일
                      end_date          DATE         NULL     COMMENT '여행종료일', -- 여행종료일
                      trip_created_date DATETIME     NULL     DEFAULT now() COMMENT '생성날짜', -- 생성날짜
                      total_day         INTEGER      NULL     COMMENT '여행일수' -- 여행일수
)
    COMMENT '여행';

-- 여행
ALTER TABLE trip
    ADD CONSTRAINT PK_trip -- 여행 기본키
        PRIMARY KEY (
                     trip_no -- 여행번호
            );

ALTER TABLE trip
    MODIFY COLUMN trip_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '여행번호';

-- 유저
CREATE TABLE user (
                      user_no            BIGINT       NOT NULL COMMENT '유저번호', -- 유저번호
                      user_photo         VARCHAR(255) NULL     COMMENT '프로필사진', -- 프로필사진
                      user_lastest_login DATETIME     NOT NULL COMMENT '마지막로그인날짜', -- 마지막로그인날짜
                      user_email         VARCHAR(40)  NOT NULL COMMENT '이메일', -- 이메일
                      user_password      VARCHAR(255) NOT NULL COMMENT '비밀번호', -- 비밀번호
                      user_tel           VARCHAR(30)  NOT NULL COMMENT '연락처', -- 연락처
                      user_created_date  DATETIME     NULL     DEFAULT now() COMMENT '가입일', -- 가입일
                      user_nickname      VARCHAR(50)  NOT NULL COMMENT '닉네임', -- 닉네임
                      user_role          VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER' COMMENT '권한여부', -- 권한여부
                      user_block         TINYINT      NOT NULL DEFAULT 0 COMMENT '차단여부', -- 차단여부
                      sns_no             INTEGER      NULL     COMMENT 'SNS번호' -- SNS번호
)
    COMMENT '유저';

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
    MODIFY COLUMN user_no BIGINT NOT NULL AUTO_INCREMENT COMMENT '유저번호';

ALTER TABLE user 
ADD COLUMN deleted_date DATETIME DEFAULT NULL;

-- 쪽지
CREATE TABLE letter (
                        letter_no        INTEGER      NOT NULL COMMENT '쪽지번호', -- 쪽지번호
                        send_user_no     BIGINT       NULL     COMMENT '발신자번호', -- 발신자번호
                        receive_user_no  BIGINT       NULL     COMMENT '수신자번호', -- 수신자번호
                        letter_title     VARCHAR(150) NOT NULL COMMENT '쪽지제목', -- 쪽지제목
                        letter_content   TEXT         NULL     COMMENT '쪽지내용', -- 쪽지내용
                        letter_send_date DATETIME     NULL     DEFAULT now() COMMENT '발신일', -- 발신일
                        letter_check     TINYINT      NOT NULL COMMENT '읽음여부' -- 읽음여부
)
    COMMENT '쪽지';

-- 쪽지
ALTER TABLE letter
    ADD CONSTRAINT PK_letter -- 쪽지 기본키
        PRIMARY KEY (
                     letter_no -- 쪽지번호
            );

ALTER TABLE letter
    MODIFY COLUMN letter_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '쪽지번호';

-- 댓글
CREATE TABLE comment (
                         comment_no           INTEGER  NOT NULL COMMENT '댓글번호', -- 댓글번호
                         board_no             INTEGER  NOT NULL COMMENT '게시판번호', -- 게시판번호
                         user_no              BIGINT   NOT NULL COMMENT '유저번호', -- 유저번호
                         comment_content      TEXT     NOT NULL COMMENT '댓글내용', -- 댓글내용
                         comment_created_date DATETIME NULL     DEFAULT now() COMMENT '댓글작성시간' -- 댓글작성시간
)
    COMMENT '댓글';

-- 댓글
ALTER TABLE comment
    ADD CONSTRAINT PK_comment -- 댓글 기본키
        PRIMARY KEY (
                     comment_no -- 댓글번호
            );

ALTER TABLE comment
    MODIFY COLUMN comment_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '댓글번호';

-- 댓글 좋아요
DROP TABLE IF EXISTS commentlike RESTRICT;

-- 댓글 좋아요
CREATE TABLE commentlike (
                             comment_no      INTEGER  NOT NULL COMMENT '댓글번호', -- 댓글번호
                             user_no       BIGINT   NOT NULL COMMENT '유저번호', -- 유저번호
                             commentlike_add_date DATETIME NULL     DEFAULT now() COMMENT '추가일자' -- 추가일자
)
    COMMENT '댓글 좋아요';

-- 댓글 좋아요
ALTER TABLE commentlike
    ADD CONSTRAINT PK_commentlike -- 좋아요 기본키
        PRIMARY KEY (
                     comment_no, -- 댓글번호
                     user_no   -- 유저번호
            );

-- 댓글 좋아요
ALTER TABLE commentlike
    ADD CONSTRAINT FK_comment_TO_commentlike -- 댓글 -> 좋아요
        FOREIGN KEY (
                     comment_no -- 댓글번호
            )
            REFERENCES comment ( -- 댓글
                                comment_no -- 댓글번호
                );

-- 댓글 좋아요
ALTER TABLE commentlike
    ADD CONSTRAINT FK_user_TO_commentlike -- 유저 -> 좋아요
        FOREIGN KEY (
                     user_no -- 유저번호
            )
            REFERENCES user ( -- 유저
                             user_no -- 유저번호
                );


-- 여행지유형대분류
CREATE TABLE firstclass (
                            firstclass_code VARCHAR(50) NOT NULL COMMENT '대분류코드', -- 대분류코드
                            firstclass_name VARCHAR(50) NOT NULL COMMENT '대분류명' -- 대분류명
)
    COMMENT '여행지유형대분류';

-- 여행지유형대분류
ALTER TABLE firstclass
    ADD CONSTRAINT PK_firstclass -- 여행지유형대분류 기본키
        PRIMARY KEY (
                     firstclass_code -- 대분류코드
            );

-- 여행지유형대분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_firstclass
    ON firstclass ( -- 여행지유형대분류
                   firstclass_code ASC -- 대분류코드
        );

-- 여행지정보
CREATE TABLE location (
                          location_no     INTEGER      NOT NULL COMMENT '여행지정보', -- 여행지정보
                          thirdclass_code VARCHAR(50)  NULL     COMMENT '소분류코드', -- 소분류코드
                          city_code       VARCHAR(50)  NULL     COMMENT '시군구코드', -- 시군구코드
                          location_name   VARCHAR(50)  NULL     COMMENT '여행지이름', -- 여행지이름
                          location_desc   TEXT         NULL     COMMENT '여행지설명', -- 여행지설명
                          location_photo  VARCHAR(255) NULL     COMMENT '여행지사진', -- 여행지사진
                          location_tel    VARCHAR(30)  NULL     COMMENT '연락처', -- 연락처
                          location_addr   VARCHAR(255) NULL     COMMENT '주소', -- 주소
                          location_x      DOUBLE       NULL     COMMENT '위도', -- 위도
                          location_y      DOUBLE       NULL     COMMENT '경도', -- 경도
                          locationtype_no INTEGER      NULL     COMMENT '여행지구분' -- 여행지구분
)
    COMMENT '여행지정보';

-- 여행지정보
ALTER TABLE location
    ADD CONSTRAINT PK_location -- 여행지정보 기본키
        PRIMARY KEY (
                     location_no -- 여행지정보
            );

ALTER TABLE location
    MODIFY COLUMN location_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '여행지정보';

-- SNS
CREATE TABLE sns (
                     sns_no   INTEGER     NOT NULL COMMENT 'SNS번호', -- SNS번호
                     sns_name VARCHAR(50) NOT NULL COMMENT 'SNS이름' -- SNS이름
)
    COMMENT 'SNS';

-- SNS
ALTER TABLE sns
    ADD CONSTRAINT PK_sns -- SNS 기본키
        PRIMARY KEY (
                     sns_no -- SNS번호
            );

ALTER TABLE sns
    MODIFY COLUMN sns_no INTEGER NOT NULL AUTO_INCREMENT COMMENT 'SNS번호';

-- 즐겨찾기
CREATE TABLE favor (
                       board_no       INTEGER  NOT NULL COMMENT '게시판번호', -- 게시판번호
                       user_no        BIGINT   NOT NULL COMMENT '유저번호', -- 유저번호
                       favor_add_date DATETIME NULL     DEFAULT now() COMMENT '추가일자' -- 추가일자
)
    COMMENT '즐겨찾기';

-- 즐겨찾기
ALTER TABLE favor
    ADD CONSTRAINT PK_favor -- 즐겨찾기 기본키
        PRIMARY KEY (
                     board_no, -- 게시판번호
                     user_no   -- 유저번호
            );

-- 좋아요
CREATE TABLE boardlike (
                           board_no      INTEGER  NOT NULL COMMENT '게시판번호', -- 게시판번호
                           user_no       BIGINT   NOT NULL COMMENT '유저번호', -- 유저번호
                           like_add_date DATETIME NULL     DEFAULT now() COMMENT '추가일자' -- 추가일자
)
    COMMENT '좋아요';

-- 좋아요
ALTER TABLE boardlike
    ADD CONSTRAINT PK_boardlike -- 좋아요 기본키
        PRIMARY KEY (
                     board_no, -- 게시판번호
                     user_no   -- 유저번호
            );

-- 게시판
CREATE TABLE board (
                       board_no           INTEGER      NOT NULL COMMENT '게시판번호', -- 게시판번호
                       boardtype_no       INTEGER      NULL     COMMENT '게시글분류번호', -- 게시글분류번호
                       board_title        VARCHAR(150) NOT NULL COMMENT '제목', -- 제목
                       board_count        INTEGER      NOT NULL DEFAULT 0 COMMENT '조회수', -- 조회수
                       board_created_date DATETIME     NULL     DEFAULT now() COMMENT '작성일', -- 작성일
                       user_no            BIGINT       NULL     COMMENT '유저번호', -- 유저번호
                       trip_no            INTEGER      NULL     COMMENT '여행번호', -- 여행번호
                       board_content      MEDIUMTEXT   NOT NULL COMMENT '내용', -- 내용
                       board_tag          VARCHAR(15)  NULL     COMMENT '태그' -- 태그
)
    COMMENT '게시판';

-- 게시판
ALTER TABLE board
    ADD CONSTRAINT PK_board -- 게시판 기본키
        PRIMARY KEY (
                     board_no -- 게시판번호
            );

ALTER TABLE board
    MODIFY COLUMN board_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '게시판번호';

-- 차단
CREATE TABLE ben (
    user_no    BIGINT   NOT NULL COMMENT '유저번호', -- 유저번호
    bentype_no INTEGER  NULL     COMMENT '차단분류번호', -- 차단분류번호
    ben_desc   TEXT     NOT NULL COMMENT '차단내용', -- 차단내용
    ben_date   DATETIME NULL     DEFAULT now() COMMENT '차단날짜', -- 차단날짜
    unban_date DATETIME NULL     COMMENT '차단해제날짜' -- 차단해제날짜
)
COMMENT '차단';

-- 차단
ALTER TABLE ben
    ADD CONSTRAINT PK_ben -- 차단 기본키
    PRIMARY KEY (user_no);

ALTER TABLE ben
    ADD CONSTRAINT FK_user_TO_ben -- 차단 외래키 (유저)
    FOREIGN KEY (user_no)
    REFERENCES user (user_no);

ALTER TABLE ben
    ADD CONSTRAINT FK_bentype_TO_ben -- 차단 타입 외래키 (벤타입)
    FOREIGN KEY (bentype_no)
    REFERENCES bentype (bentype_no);

-- 시도
CREATE TABLE state (
                       state_code VARCHAR(50) NOT NULL COMMENT '시도코드', -- 시도코드
                       state_name VARCHAR(50) NOT NULL COMMENT '시도명', -- 시도명
                       state_tour VARCHAR(50) NOT NULL COMMENT '시도여행번호'
)
    COMMENT '시도';

-- 시도
ALTER TABLE state
    ADD CONSTRAINT PK_state -- 시도 기본키
        PRIMARY KEY (
                     state_code -- 시도코드
            );

-- 시도 유니크 인덱스
CREATE UNIQUE INDEX UIX_state
    ON state ( -- 시도
              state_code ASC -- 시도코드
        );

-- 시군구
CREATE TABLE city (
                      city_code  VARCHAR(50)  NOT NULL COMMENT '시군구코드', -- 시군구코드
                      state_code VARCHAR(50)  NULL     COMMENT '시도코드', -- 시도코드
                      city_name  VARCHAR(50)  NOT NULL COMMENT '시군구명', -- 시군구명
                      city_tour VARCHAR(50) NOT NULL COMMENT '시도여행번호',
                      city_photo VARCHAR(255) NULL     COMMENT '썸네일사진' -- 썸네일사진
)
    COMMENT '시군구';

-- 시군구
ALTER TABLE city
    ADD CONSTRAINT PK_city -- 시군구 기본키
        PRIMARY KEY (
                     city_code -- 시군구코드
            );

-- 시군구 유니크 인덱스
CREATE UNIQUE INDEX UIX_city
    ON city ( -- 시군구
             city_code ASC -- 시군구코드
        );

-- 여행테마
CREATE TABLE thema (
                       thema_no   INTEGER     NOT NULL COMMENT '여행테마번호', -- 여행테마번호
                       thema_name VARCHAR(50) NULL     COMMENT '테마명' -- 테마명
)
    COMMENT '여행테마';

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
    MODIFY COLUMN thema_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '여행테마번호';

-- 여행일정
CREATE TABLE schedule (
                          schedule_no    INTEGER NOT NULL COMMENT '여행일정번호', -- 여행일정번호
                          location_no    INTEGER NULL     COMMENT '여행지정보', -- 여행지정보
                          trip_no        INTEGER NULL     COMMENT '여행번호', -- 여행번호
                          schedule_day   INTEGER NULL     DEFAULT 0 COMMENT '여행일차', -- 여행일차
                          schedule_route INTEGER NULL     DEFAULT 0 COMMENT '여행순서' -- 여행순서
)
    COMMENT '여행일정';

-- 여행일정
ALTER TABLE schedule
    ADD CONSTRAINT PK_schedule -- 여행일정 기본키
        PRIMARY KEY (
                     schedule_no -- 여행일정번호
            );

-- 여행일정 인덱스
CREATE INDEX IX_schedule
    ON schedule( -- 여행일정
                schedule_day ASC -- 여행일차
        );

-- 여행일정 인덱스2
CREATE INDEX IX_schedule2
    ON schedule( -- 여행일정
                schedule_route ASC -- 여행순서
        );

ALTER TABLE schedule
    MODIFY COLUMN schedule_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '여행일정번호';

-- 여행지유형중분류
CREATE TABLE secondclass (
                             secondclass_code VARCHAR(50) NOT NULL COMMENT '중분류코드', -- 중분류코드
                             firstclass_code  VARCHAR(50) NULL     COMMENT '대분류코드', -- 대분류코드
                             secondclass_name VARCHAR(50) NOT NULL COMMENT '중분류명' -- 중분류명
)
    COMMENT '여행지유형중분류';

-- 여행지유형중분류
ALTER TABLE secondclass
    ADD CONSTRAINT PK_secondclass -- 여행지유형중분류 기본키
        PRIMARY KEY (
                     secondclass_code -- 중분류코드
            );

-- 여행지유형중분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_secondclass
    ON secondclass ( -- 여행지유형중분류
                    secondclass_code ASC -- 중분류코드
        );

-- 여행지유형소분류
CREATE TABLE thirdclass (
                            thirdclass_code  VARCHAR(50) NOT NULL COMMENT '소분류코드', -- 소분류코드
                            thirdclass_name  VARCHAR(50) NOT NULL COMMENT '소분류명', -- 소분류명
                            secondclass_code VARCHAR(50) NULL     COMMENT '중분류코드' -- 중분류코드
)
    COMMENT '여행지유형소분류';

-- 여행지유형소분류
ALTER TABLE thirdclass
    ADD CONSTRAINT PK_thirdclass -- 여행지유형소분류 기본키
        PRIMARY KEY (
                     thirdclass_code -- 소분류코드
            );

-- 여행지유형소분류 유니크 인덱스
CREATE UNIQUE INDEX UIX_thirdclass
    ON thirdclass ( -- 여행지유형소분류
                   thirdclass_code ASC -- 소분류코드
        );

-- 차단분류
CREATE TABLE bentype (
                         bentype_no   INTEGER     NOT NULL COMMENT '차단분류번호', -- 차단분류번호
                         bentype_name VARCHAR(50) NOT NULL COMMENT '차단분류명' -- 차단분류명
)
    COMMENT '차단분류';

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
    MODIFY COLUMN bentype_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '차단분류번호';

-- 게시판이미지
CREATE TABLE boardimage (
                            boardimage_no           INTEGER      NOT NULL, -- 이미지번호
                            board_no                INTEGER      NOT NULL, -- 게시판번호
                            boardimage_name         VARCHAR(255) NOT NULL, -- 이미지이름
                            boardimage_default_name VARCHAR(50)  NOT NULL  -- 이미지원래이름
);

-- 게시판이미지
ALTER TABLE boardimage
    ADD CONSTRAINT PK_boardimage -- 이미지첨부 기본키
        PRIMARY KEY (
                     boardimage_no -- 이미지번호
            );
ALTER TABLE boardimage
    MODIFY boardimage_no INT NOT NULL AUTO_INCREMENT;

-- 게시글분류
CREATE TABLE boardtype (
                           boardtype_no   INTEGER     NOT NULL COMMENT '게시글분류번호', -- 게시글분류번호
                           boardtype_name VARCHAR(50) NOT NULL COMMENT '게시글분류명' -- 게시글분류명
)
    COMMENT '게시글분류';

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
    MODIFY COLUMN boardtype_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '게시글분류번호';

-- 동행
CREATE TABLE companion (
                           companion_no  INTEGER NOT NULL COMMENT '동행번호', -- 동행번호
                           board_no      INTEGER NOT NULL COMMENT '게시판번호', -- 게시판번호
                           companion_max INTEGER NULL     DEFAULT 0 COMMENT '인원수' -- 인원수
)
    COMMENT '동행';

-- 동행
ALTER TABLE companion
    ADD CONSTRAINT PK_companion -- 동행 기본키
        PRIMARY KEY (
                     companion_no -- 동행번호
            );

ALTER TABLE companion
    MODIFY COLUMN companion_no INTEGER NOT NULL AUTO_INCREMENT COMMENT '동행번호';

-- 여행지구분
CREATE TABLE locationtype (
                              locationtype_no INTEGER NOT NULL COMMENT '여행지구분' -- 여행지구분
)
    COMMENT '여행지구분';

-- 여행지구분
ALTER TABLE locationtype
    ADD CONSTRAINT PK_locationtype -- 여행지구분 기본키
        PRIMARY KEY (
                     locationtype_no -- 여행지구분
            );


-- 동행정보
CREATE TABLE companioninfo (
                               companion_check_info_no INTEGER     NOT NULL COMMENT '동행정보번호', -- 동행정보번호
                               companion_no            INTEGER     NULL     COMMENT '동행번호', -- 동행번호
                               location_no             INTEGER     NULL     COMMENT '여행지정보', -- 여행지정보
                               checked                 TINYINT     NULL     COMMENT '동행체크', -- 동행체크
                               apply_user              VARCHAR(50) NULL     COMMENT '동행인원' -- 동행인원
)
    COMMENT '동행정보';

-- 동행정보
ALTER TABLE companioninfo
    ADD CONSTRAINT PK_companioninfo -- 동행정보 기본키
        PRIMARY KEY (
                     companion_check_info_no -- 동행정보번호
            );

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

-- 여행
ALTER TABLE trip
    ADD CONSTRAINT FK_city_TO_trip -- 시군구 -> 여행
        FOREIGN KEY (
                     city_code -- 시군구코드
            )
            REFERENCES city ( -- 시군구
                             city_code -- 시군구코드
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
    ADD CONSTRAINT FK_locationtype_TO_location -- 여행지구분 -> 여행지정보
        FOREIGN KEY (
                     locationtype_no -- 여행지구분
            )
            REFERENCES locationtype ( -- 여행지구분
                                     locationtype_no -- 여행지구분
                );

-- 여행지정보
ALTER TABLE location
    ADD CONSTRAINT FK_thirdclass_TO_location -- 여행지유형소분류 -> 여행지정보
        FOREIGN KEY (
                     thirdclass_code -- 소분류코드
            )
            REFERENCES thirdclass ( -- 여행지유형소분류
                                   thirdclass_code -- 소분류코드
                );

-- 여행지정보
ALTER TABLE location
    ADD CONSTRAINT FK_city_TO_location -- 시군구 -> 여행지정보
        FOREIGN KEY (
                     city_code -- 시군구코드
            )
            REFERENCES city ( -- 시군구
                             city_code -- 시군구코드
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
ALTER TABLE city
    ADD CONSTRAINT FK_state_TO_city -- 시도 -> 시군구
        FOREIGN KEY (
                     state_code -- 시도코드
            )
            REFERENCES state ( -- 시도
                              state_code -- 시도코드
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
                     firstclass_code -- 대분류코드
            )
            REFERENCES firstclass ( -- 여행지유형대분류
                                   firstclass_code -- 대분류코드
                );

-- 여행지유형소분류
ALTER TABLE thirdclass
    ADD CONSTRAINT FK_secondclass_TO_thirdclass -- 여행지유형중분류 -> 여행지유형소분류
        FOREIGN KEY (
                     secondclass_code -- 중분류코드
            )
            REFERENCES secondclass ( -- 여행지유형중분류
                                    secondclass_code -- 중분류코드
                );

-- 게시판이미지
ALTER TABLE boardimage
    ADD CONSTRAINT FK_board_TO_boardimage -- 게시판 -> 이미지첨부
        FOREIGN KEY (
                     board_no -- 게시판번호
            )
            REFERENCES board ( -- 게시판
                              board_no -- 게시판번호
                );


-- 동행
ALTER TABLE companion
    ADD CONSTRAINT FK_board_TO_companion -- 게시판 -> 동행
        FOREIGN KEY (
                     board_no -- 게시판번호
            )
            REFERENCES board ( -- 게시판
                              board_no -- 게시판번호
                )
            ON DELETE CASCADE;  -- CASCADE를 추가하여 DELETE

-- 동행정보
ALTER TABLE companioninfo
    ADD CONSTRAINT FK_companion_TO_companioninfo -- 동행 -> 동행정보
        FOREIGN KEY (
                     companion_no -- 동행번호
            )
            REFERENCES companion ( -- 동행
                                  companion_no -- 동행번호
                );

-- 동행정보
ALTER TABLE companioninfo
    ADD CONSTRAINT FK_location_TO_companioninfo -- 여행지정보 -> 동행정보
        FOREIGN KEY (
                     location_no -- 여행지정보
            )
            REFERENCES location ( -- 여행지정보
                                 location_no -- 여행지정보
                );

-- Comment
-- 기존 외래 키 제약 조건 삭제
ALTER TABLE comment
    DROP FOREIGN KEY FK_board_TO_comment;

-- Comment
-- 외래 키 제약 조건을 다시 생성하며 ON DELETE CASCADE 추가
ALTER TABLE comment
    ADD CONSTRAINT FK_board_TO_comment
        FOREIGN KEY (board_no) REFERENCES board(board_no)
            ON DELETE CASCADE;

-- Favor
-- 기존 외래 키 제약 조건 삭제
ALTER TABLE favor
    DROP FOREIGN KEY FK_board_TO_favor;

-- 외래 키 제약 조건을 다시 생성하며 ON DELETE CASCADE 추가
ALTER TABLE favor
    ADD CONSTRAINT FK_board_TO_favor
        FOREIGN KEY (board_no) REFERENCES board(board_no)
            ON DELETE CASCADE;

-- BoardLike
-- 외래 키 제약 조건을 다시 생성하며 ON DELETE CASCADE 추가
ALTER TABLE boardlike
    DROP FOREIGN KEY FK_board_TO_boardlike;

ALTER TABLE boardlike
    ADD CONSTRAINT FK_board_TO_boardlike
        FOREIGN KEY (board_no) REFERENCES board(board_no)
            ON DELETE CASCADE;

-- Count 조회용 favor View 생성
CREATE VIEW favor_count_view AS
SELECT board_no, COUNT(*) AS favor_count
FROM favor
GROUP BY board_no;

-- Count 조회용 boardLike View 생성
CREATE VIEW boardlike_count_view AS
SELECT board_no, COUNT(*) AS like_count
FROM boardlike
GROUP BY board_no;

-- Count 조회용 commentLike View 생성
CREATE VIEW commentlike_count_view AS
SELECT comment_no, COUNT(*) AS comment_like_count
FROM commentlike
GROUP BY comment_no;

-- 동행모집 테이블(companionrecruit)
CREATE TABLE `companionrecruit` (
                                    `companion_recruit_no` INTEGER NOT NULL, -- 동행모집 번호
                                    `board_no`             INTEGER NOT NULL, -- 게시판번호
                                    `schedule_no`          INTEGER NULL,     -- 여행일정번호
                                    `checked`              TINYINT NULL,     -- 동행모집 선택체크
                                    `checked_all`          TINYINT NULL,     -- 동행모집 전체 체크
                                    `recruit_max_no`       INTEGER NULL     DEFAULT 0 -- 모집 최대인원수
);

ALTER TABLE `companionrecruit`
    ADD CONSTRAINT `PK_companionrecruit` -- 동행모집 기본키
        PRIMARY KEY (
                     `companion_recruit_no` -- 동행모집 번호
            );

ALTER TABLE `companionrecruit`
    MODIFY COLUMN `companion_recruit_no` INTEGER NOT NULL AUTO_INCREMENT;


ALTER TABLE `companionrecruit`
    ADD CONSTRAINT `FK_board_TO_companionrecruit` -- 게시판 -> 동행모집
        FOREIGN KEY (
                     `board_no` -- 게시판번호
            )
            REFERENCES `board` ( -- 게시판
                                `board_no` -- 게시판번호
                );


ALTER TABLE `companionrecruit`
    ADD CONSTRAINT `FK_schedule_TO_companionrecruit` -- 여행일정 -> 동행모집
        FOREIGN KEY (
                     `schedule_no` -- 여행일정번호
            )
            REFERENCES `schedule` ( -- 여행일정
                                   `schedule_no` -- 여행일정번호
                );

-- 동행신청 테이블(companionapply)
CREATE TABLE `companionapply` (
                                  `companion_apply_no`   INTEGER NOT NULL, -- 동행신청 번호
                                  `companion_recruit_no` INTEGER NULL,     -- 동행모집 번호
                                  `user_no`              BIGINT  NULL     COMMENT '동행신청한 유저의 고유번호 저장' -- 유저번호
);

ALTER TABLE `companionapply`
    ADD CONSTRAINT `PK_companionapply` -- 동행신청 기본키
        PRIMARY KEY (
                     `companion_apply_no` -- 동행신청 번호
            );

ALTER TABLE `companionapply`
    MODIFY COLUMN `companion_apply_no` INTEGER NOT NULL AUTO_INCREMENT;

ALTER TABLE `companionapply`
    ADD CONSTRAINT `FK_companionrecruit_TO_companionapply` -- 동행모집 -> 동행신청
        FOREIGN KEY (
                     `companion_recruit_no` -- 동행모집 번호
            )
            REFERENCES `companionrecruit` ( -- 동행모집
                                           `companion_recruit_no` -- 동행모집 번호
                );

ALTER TABLE `companionapply`
    ADD CONSTRAINT `FK_user_TO_companionapply` -- 유저 -> 동행신청
        FOREIGN KEY (
                     `user_no` -- 유저번호
            )
            REFERENCES `user` ( -- 유저
                               `user_no` -- 유저번호
                );


CREATE TABLE notifications (
                               notification_no BIGINT AUTO_INCREMENT PRIMARY KEY, -- 알림 ID
                               user_no BIGINT NOT NULL,                   -- 사용자 ID
                               message VARCHAR(255) NOT NULL,             -- 알림 메시지
                               link VARCHAR(255),                         -- 이동할 URL
                               is_read BOOLEAN DEFAULT FALSE,             -- 읽음 여부
                               created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성 일시
                               FOREIGN KEY (user_no) REFERENCES user(user_no) -- user_id를 users 테이블과 연결
);

