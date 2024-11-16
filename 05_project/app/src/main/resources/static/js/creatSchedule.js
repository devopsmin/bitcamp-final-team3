function pinMarker(selectedColumns = null) {
    // 기존 마커 모두 제거
    clearAllMarkers();
    clearAllPolylines();

    const colors = ["primary", "warning", "danger", "info", "secondary", "success"];

    // 처리할 컨테이너 선택
    let containers;
    if (selectedColumns) {
        // 특정 컬럼만 선택
        containers = selectedColumns;
    } else {
        // 모든 컬럼 선택
        containers = document.querySelectorAll('.schedule-container');
    }

    // 선택된 컨테이너에 대해서만 마커 처리
    containers.forEach(container => {
        container.querySelectorAll('#locationCard').forEach(card => {
            const locationX = card.querySelector('.location-x').textContent;
            const locationY = card.querySelector('.location-y').textContent;
            const day = parseInt(card.querySelector('.day').textContent);
            const route = card.querySelector('.route').textContent;
            const color = colors[(day - 1) % colors.length];

            // 각 카드 내의 원형과 선 스타일 업데이트
            card.querySelectorAll('.rounded-circle, .position-absolute').forEach(element => {
                // 기존 색상 클래스 제거
                colors.forEach(c => element.classList.remove(`bg-${c}`));
                // 새 색상 클래스 추가
                element.classList.add(`bg-${color}`);
            });

            // 다음 timeCard가 있는 경우 스타일 업데이트
            const nextTimeCard = card.nextElementSibling;
            if (nextTimeCard && nextTimeCard.id === 'timeCard') {
                const carBar = nextTimeCard.querySelector('#carBar .d-flex');
                const carIcon = nextTimeCard.querySelector('#carIcon');

                // 기존 색상 클래스 제거
                colors.forEach(c => {
                    carBar.classList.remove(`bg-${c}`);
                    carIcon.classList.remove(`text-${c}`);
                });

                // 새 색상 클래스 추가
                carBar.classList.add(`bg-${color}`);
                carIcon.classList.add(`text-${color}`);
            }

            // 좌표가 있는 경우에만 마커 추가
            if (locationX && locationY) {
                addMarker(parseFloat(locationX), parseFloat(locationY), parseInt(route), `text-${color}`);
            }
        });
    });

    // 선택된 컨테이너에 대해서만 경로 업데이트
    if (distances) {
        updateDurations(distances, selectedColumns, scheduleKeyMap);
    }
}

function updateDurations(distances, selectedContainers = null, scheduleKeyMap) {
    // 기존 폴리라인들 제거
    clearAllPolylines();

    const bootstrapColors = {
        'primary': '#0d6efd',   // Bootstrap primary 색상
        'warning': '#ffc107',   // Bootstrap warning 색상
        'danger': '#dc3545',    // Bootstrap danger 색상
        'info': '#0dcaf0',      // Bootstrap info 색상
        'secondary': '#6c757d', // Bootstrap secondary 색상
        'success': '#198754'    // Bootstrap success 색상
    };

    const colors = ["primary", "warning", "danger", "info", "secondary", "success"];

    let durationElements;
    if (selectedContainers) {
        durationElements = selectedContainers.flatMap(container =>
            Array.from(container.querySelectorAll(".duration"))
        );
    } else {
        durationElements = document.querySelectorAll(".duration");
    }

    durationElements.forEach(function (element) {
        const originCard = element.closest('.card').previousElementSibling;
        const destinationCard = element.closest('.card').nextElementSibling;

        if (originCard && destinationCard) {
            const originScheduleNo = parseInt(originCard.querySelector('.schedule-no').textContent);
            const destinationScheduleNo = parseInt(destinationCard.querySelector('.schedule-no').textContent);

            // scheduleKeyMap을 사용하여 실제 인덱스 찾기
            const originIndex = Object.entries(scheduleKeyMap)
                .find(([_, value]) => value === originScheduleNo)?.[0];
            const destinationIndex = Object.entries(scheduleKeyMap)
                .find(([_, value]) => value === destinationScheduleNo)?.[0];

            if (originIndex !== undefined && destinationIndex !== undefined) {
                // distances 배열에서 해당 요소의 duration 값을 가져와서 설정
                if (distances[originIndex] && distances[originIndex][destinationIndex]) {
                    element.textContent = millisToTime(distances[originIndex][destinationIndex].duration);
                    const day = parseInt(element.closest('.column-item').querySelector('.fw-bold').textContent.replace('Day ', ''));
                    const colorIndex = (day - 1) % colors.length;
                    const selectedColor = bootstrapColors[colors[colorIndex]];

                    // 경로 추가
                    drawPath(distances[originIndex][destinationIndex].path, selectedColor);
                } else {
                    element.textContent = "N/A";
                }
            }
        }
    });
}

function millisToTime(millis) {
    // 입력값이 문자열인 경우 숫자로 변환
    const millisNum = typeof millis === 'string' ? parseInt(millis, 10) : millis;

    // 유효한 숫자가 아닌 경우 체크
    if (isNaN(millisNum)) {
        return "Invalid input";
    }

    const hours = Math.floor(millisNum / 3600000);  // 시간 계산
    const minutes = Math.floor((millisNum % 3600000) / 60000);  // 분 계산

    // 시간과 분에 따른 문자열 생성
    if (hours === 0) {
        return minutes === 0 ? "0분" : `${minutes}분`;
    } else {
        return minutes === 0 ? `${hours}시간` : `${hours}시간 ${minutes}분`;
    }
}
