-- 동행 : 테이블생성(기존 동행테이블 삭제후 신규생성)
DROP TABLE IF EXISTS `companion` RESTRICT;

DROP TABLE IF EXISTS `companioninfo` RESTRICT;

CREATE TABLE `companion` (
	`companion_no`  INTEGER NOT NULL COMMENT '동행번호', -- 동행번호
	`board_no`      INTEGER NOT NULL COMMENT '게시판번호', -- 게시판번호
	`schedule_no`   INTEGER NULL     COMMENT '여행일정번호', -- 여행일정번호
	`user_no`       BIGINT  NULL     COMMENT '유저번호', -- 유저번호
	`checked`       TINYINT NULL     COMMENT '동행신청체크', -- 동행구함 체크
	`all_checked`   TINYINT NULL     COMMENT '동행신청체크', -- 동행구함 전체체크
	`max_num_apply` INTEGER NULL     COMMENT '동행신청 최대 인원수' -- 동행신청 최대 인원수
)
COMMENT '동행';

-- 동행 : 테이블 제약조건 설정(board_no)
ALTER TABLE `companion`
	ADD CONSTRAINT `FK_board_TO_companion` -- 게시판 -> 동행
	FOREIGN KEY (
	`board_no` -- 게시판번호
	)
	REFERENCES `board` ( -- 게시판
	`board_no` -- 게시판번호
	);

-- 동행 : 테이블 제약조건 설정(schedule_no)
ALTER TABLE `companion`
	ADD CONSTRAINT `FK_schedule_TO_companion` -- 여행일정 -> 동행
	FOREIGN KEY (
	`schedule_no` -- 여행일정번호
	)
	REFERENCES `schedule` ( -- 여행일정
	`schedule_no` -- 여행일정번호
	);

-- 동행 : 테이블 제약조건 설정(user_no)
ALTER TABLE `companion`
	ADD CONSTRAINT `FK_user_TO_companion` -- 유저 -> 동행
	FOREIGN KEY (
	`user_no` -- 유저번호
	)
	REFERENCES `user` ( -- 유저
	`user_no` -- 유저번호
	);
