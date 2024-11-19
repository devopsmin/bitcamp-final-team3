var DateRangeCalendar = function() {
    // startDate와 endDate는 Thymeleaf에서 제공된 값을 직접 사용
    this.currentDate = startDate ? new Date(startDate) : new Date();
    startDate = startDate ? new Date(startDate) : null;
    endDate = endDate ? new Date(endDate) : null;
    console.log("statdasl;jkafsdjkasdljlkdfas",typeof endDate, endDate)
    this.hoverDate = null;

    this.init = function() {
        var container = document.querySelector('.calendars-container');

        // 현재 달력
        var currentMonth = this.createCalendarElement(this.currentDate);
        container.appendChild(currentMonth);

        // 다음 달력
        var nextMonth = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1);
        var nextMonthCalendar = this.createCalendarElement(nextMonth);
        container.appendChild(nextMonthCalendar);

        this.addEventListeners();
        this.updateCalendarDisplay();
    };

    this.handleDateClick = function(e) {
        var clickedDate = new Date(e.target.dataset.date);
        // console.log(clickedDate);

        if (!startDate || (startDate && endDate)) {
            startDate = clickedDate
            endDate = null;
        } else {
            if (clickedDate < startDate) {
                startDate = clickedDate;
                endDate = null;
            } else {
                endDate = clickedDate;
            }
        }

        this.updateCalendarDisplay();
        // this.updateSelectedDatesDisplay();
    };

// var DateRangeCalendar = function() {
//     this.currentDate = window.selectedStartDate || new Date();
//     this.selectedStartDate = window.selectedStartDate;
//     this.selectedEndDate = window.selectedEndDate;
//     this.hoverDate = null;
//
//     this.init = function() {
//         var container = document.querySelector('.calendars-container');
//
//         // 현재 달력
//         var currentMonth = this.createCalendarElement(this.currentDate);
//         container.appendChild(currentMonth);
//
//         // 다음 달력
//         var nextMonth = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1);
//         var nextMonthCalendar = this.createCalendarElement(nextMonth);
//         container.appendChild(nextMonthCalendar);
//
//         this.addEventListeners();
//
//         // 선택된 날짜가 있으면 표시
//         this.updateCalendarDisplay();
//     };
//
//     // 날짜 선택 처리
//     this.handleDateClick = function(e) {
//         var clickedDate = new Date(e.target.dataset.date);
//
//         if (!this.selectedStartDate || (this.selectedStartDate && this.selectedEndDate)) {
//             // 새로운 선택 시작
//             this.selectedStartDate = clickedDate;
//             this.selectedEndDate = null;
//             window.selectedStartDate = clickedDate;  // 전역변수 업데이트
//             window.selectedEndDate = null;
//         } else {
//             if (clickedDate < this.selectedStartDate) {
//                 // 이전 날짜 클릭 시
//                 this.selectedStartDate = clickedDate;
//                 this.selectedEndDate = null;
//                 window.selectedStartDate = clickedDate;  // 전역변수 업데이트
//                 window.selectedEndDate = null;
//             } else {
//                 // 종료일 선택 시
//                 this.selectedEndDate = clickedDate;
//                 window.selectedEndDate = clickedDate;  // 전역변수 업데이트
//             }
//         }
//
//         this.updateCalendarDisplay();
//         this.updateSelectedDatesDisplay();
//     };

    this.createCalendarElement = function(date) {
        var calendarDiv = document.createElement('div');
        calendarDiv.className = 'calendar';

        // 헤더 컨테이너 추가
        var headerContainer = document.createElement('div');
        headerContainer.className = 'd-flex justify-content-between align-items-center mb-4';

        // 이전 달 버튼 (왼쪽 캘린더만)
        var prevButton = document.createElement('button');
        if (date.getMonth() === this.currentDate.getMonth()) {
            prevButton.className = 'btn btn-sm btn-outline-secondary px-2';
            prevButton.innerHTML = '&lt;';
            prevButton.onclick = this.previousMonth.bind(this);
        } else {
            prevButton.className = 'btn btn-sm invisible';
            prevButton.style.visibility = 'hidden';
        }

        // 월 표시
        var header = document.createElement('h6');
        header.className = 'mb-0';
        header.textContent = date.getFullYear() + '년 ' + (date.getMonth() + 1) + '월';

        // 다음 달 버튼 (오른쪽 캘린더만)
        var nextButton = document.createElement('button');
        if (date.getMonth() !== this.currentDate.getMonth()) {
            nextButton.className = 'btn btn-sm btn-outline-secondary px-2';
            nextButton.innerHTML = '&gt;';
            nextButton.onclick = this.nextMonth.bind(this);
        } else {
            nextButton.className = 'btn btn-sm invisible';
            nextButton.style.visibility = 'hidden';
        }

        headerContainer.appendChild(prevButton);
        headerContainer.appendChild(header);
        headerContainer.appendChild(nextButton);
        calendarDiv.appendChild(headerContainer);

        // 테이블 생성
        var table = document.createElement('table');
        table.className = 'table table-borderless';

        // 요일 헤더
        var thead = document.createElement('thead');
        var weekDays = ['일', '월', '화', '수', '목', '금', '토'];
        var headerRow = document.createElement('tr');

        for (var i = 0; i < weekDays.length; i++) {
            var th = document.createElement('th');
            th.className = 'text-center';
            th.textContent = weekDays[i];
            headerRow.appendChild(th);
        }
        thead.appendChild(headerRow);
        table.appendChild(thead);

        // 날짜 생성
        var tbody = document.createElement('tbody');
        var calendar = this.generateCalendarDates(date);

        for (var i = 0; i < calendar.length; i++) {
            var row = document.createElement('tr');
            for (var j = 0; j < calendar[i].length; j++) {
                var td = document.createElement('td');
                var day = calendar[i][j];

                if (day) {
                    td.textContent = day.getDate();
                    td.className = 'cursor-pointer';
                    td.setAttribute('data-date', day.toISOString());

                    if (this.isToday(day)) {
                        td.classList.add('today');
                    }
                } else {
                    td.className = 'bg-light';
                }
                row.appendChild(td);
            }
            tbody.appendChild(row);
        }

        table.appendChild(tbody);
        calendarDiv.appendChild(table);

        return calendarDiv;
    };

    // 이전 달로 이동하는 함수
    this.previousMonth = function() {
        this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() - 1);
        this.refreshCalendars();
    };

    // 다음 달로 이동하는 함수
    this.nextMonth = function() {
        this.currentDate = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1);
        this.refreshCalendars();
    };

    // 캘린더 새로고침 함수
    this.refreshCalendars = function() {
        var container = document.querySelector('.calendars-container');
        container.innerHTML = ''; // 기존 캘린더 제거

        // 현재 달력
        var currentMonth = this.createCalendarElement(this.currentDate);
        container.appendChild(currentMonth);

        // 다음 달력
        var nextMonth = new Date(this.currentDate.getFullYear(), this.currentDate.getMonth() + 1);
        var nextMonthCalendar = this.createCalendarElement(nextMonth);
        container.appendChild(nextMonthCalendar);

        this.addEventListeners(); // 이벤트 리스너 다시 연결
        this.updateCalendarDisplay(); // 선택된 날짜 표시 업데이트
    };

    this.generateCalendarDates = function(date) {
        var year = date.getFullYear();
        var month = date.getMonth();
        var firstDay = new Date(year, month, 1).getDay();
        var lastDate = new Date(year, month + 1, 0).getDate();
        var calendar = [];
        var week = [];

        for (var i = 0; i < firstDay; i++) {
            week.push(null);
        }

        for (var day = 1; day <= lastDate; day++) {
            if (week.length === 7) {
                calendar.push(week);
                week = [];
            }
            week.push(new Date(year, month, day));
        }

        while (week.length < 7) {
            week.push(null);
        }
        calendar.push(week);

        return calendar;
    };

    this.handleDateHover = function(e) {
        if (startDate && !endDate) {
            this.hoverDate = new Date(e.target.dataset.date);
            this.updateCalendarDisplay();
        }
    };

    this.updateCalendarDisplay = function() {
        var self = this;
        var cells = document.querySelectorAll('.calendar td[data-date]');

        cells.forEach(function(td) {
            var date = new Date(td.dataset.date);
            td.classList.remove('selected-start', 'selected-end', 'bg-secondary', 'text-white');

            if (td.classList.contains('cursor-pointer')) {
                td.style.color = '';
            }

            if (self.isToday(date)) {
                td.classList.add('today');
            }

            // 로컬 변수 사용
            if (startDate && self.isSameDate(date, startDate)) {
                td.classList.add('selected-start');
            }

            if (endDate && self.isSameDate(date, endDate)) {
                td.classList.add('selected-end');
            }

            if (startDate && endDate &&
                date > startDate && date < endDate) {
                td.classList.add('bg-secondary');
                td.classList.add('text-white');
            }

            if (startDate && self.hoverDate && !endDate &&
                date > startDate && date <= self.hoverDate) {
                td.classList.add('bg-secondary');
                td.classList.add('text-white');
            }
        });

        this.updateSelectedDatesDisplay();
    };

    this.updateSelectedDatesDisplay = function() {
        // console.log(startDate);
        // console.log(endDate);
        if (startDate) {
            document.getElementById('startDate').value = this.formatDate(startDate);
        }
        if (endDate) {
            document.getElementById('endDate').value = this.formatDate(endDate);
        }
    };

    this.formatDate = function(date) {
        return date.getFullYear() + '-' +
            ('0' + (date.getMonth() + 1)).slice(-2) + '-' +
            ('0' + date.getDate()).slice(-2);
    };

    this.isToday = function(date) {
        var today = new Date();
        return this.isSameDate(date, today);
    };

    this.isSameDate = function(date1, date2) {
        return date1.getDate() === date2.getDate() &&
            date1.getMonth() === date2.getMonth() &&
            date1.getFullYear() === date2.getFullYear();
    };

    // 호버 이벤트가 끝날 때 처리를 추가
    this.addEventListeners = function() {
        var self = this;
        var cells = document.querySelectorAll('.calendar td[data-date]');

        cells.forEach(function(td) {
            td.addEventListener('click', function(e) {
                self.handleDateClick.call(self, e);
            });
            td.addEventListener('mouseenter', function(e) {
                self.handleDateHover.call(self, e);
            });
            // mouseleave 이벤트 추가
            td.addEventListener('mouseleave', function(e) {
                self.hoverDate = null;
                self.updateCalendarDisplay();
            });
        });
    };
};

document.addEventListener('DOMContentLoaded', function() {
    var calendar = new DateRangeCalendar();
    calendar.init();
});


