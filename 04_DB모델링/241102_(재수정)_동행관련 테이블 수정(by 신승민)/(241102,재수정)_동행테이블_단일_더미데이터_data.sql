
-- 동행모집테이블 : 동행 선택 구함
INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 12, 1, 0, 3);

INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 13, 1, 0, 5);

INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 15, 1, 0, 6);


-- 동행모집테이블 : 동행 전체구함
INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 16, 0, 1, 7);


INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 17, 0, 1, 0);


INSERT INTO
	`final_project`.`companionrecruit` (`board_no`, `schedule_no`, `checked`, `checked_all`, `recruit_max_no`)
VALUES
	(11, 18, 0, 1, 0);





-- 동행신청테이블
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (1, 13);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (1, 10);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (3, 1);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (3, 3);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (3, 5);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (4, 2);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (4, 6);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (4, 7);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (4, 8);
INSERT INTO `final_project`.`companionapply` (`companion_recruit_no`, `user_no`) VALUES (4, 9);
