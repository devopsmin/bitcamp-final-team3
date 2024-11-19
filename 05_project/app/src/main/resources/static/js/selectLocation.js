function makeLocationList(button){
    const location = {
        locationNo: button.querySelector('.location-no').textContent,
        locationName: button.closest('.card').querySelector('.card-title').textContent,
        locationAddr: button.closest('.card').querySelector('.card-text').textContent,
        locationX: button.querySelector('.location-x').textContent,
        locationY: button.querySelector('.location-y').textContent,
        locationtypeNo: button.querySelector('.location-type').textContent,
        thirdclassCode: button.querySelector('.thirdclass-code').textContent,
        cityCode: button.querySelector('.city-code').textContent,
        locationDesc: button.querySelector('.location-desc').textContent,
        locationTel: button.querySelector('.location-tel').textContent,
        locationPhoto: button.closest('.card').querySelector('img').src
    };

    // location 객체 사용
    console.log(location);

    selectedItems.push(location);
    const icon = button.querySelector('i');

    button.classList.remove("btn-outline-secondary");
    button.classList.add("btn-primary");

    icon.classList.remove("bi-plus-lg");
    icon.classList.add("bi-check-lg");

    // 버튼 비활성화
    button.disabled = true;
    button.style.pointerEvents = 'none'; // 클릭 이벤트 완전히 차단
}

function modifyLocation(selectedItems) {
    fetch('/schedule/appendMyLocation', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(selectedItems)
    })
        .then(response => response.json()) // 응답 데이터를 JSON 형식으로 파싱
        .then(data => {
            renderLocationCards(data); // 받은 데이터를 이용해 카드 업데이트
        })
        .catch(error => {
            console.error("Error:", error);
        });
}

function renderLocationCards(data) {
    const cardArea = document.getElementById("cardArea");
    const countElement = document.querySelector('#appendMyLocation .fs-4.fw-bold');

    // 선택된 장소 수 업데이트
    countElement.textContent = data.length;

    // markerList의 모든 마커를 지도에서 제거
    markerList.forEach(marker => marker.setMap(null));
    markerList = [];

    // HTML 생성
    const html = data.map((location, index) => `
        <div class="card m-1" style="width: 100%; height: 90px;">
            <div class="card-body px-1 py-0 d-flex align-items-center justify-content-between gap-1">
                <div class="d-flex justify-content-center align-items-center rounded-circle bg-primary text-white"
                     style="width: 20px; height: 20px; font-size: 10px;">
                    ${index + 1}
                </div>

                <img alt="Location Image" class="rounded me-3"
                     src="${location.locationPhoto || '/images/default.jpg'}"
                     style="width: 70px; height: 70px; object-fit: cover;">

                <div class="d-flex flex-column" style="flex: 1;">
                    <h5 class="card-title mb-1" style="font-size: 16px;">
                        ${location.locationName}
                    </h5>
                    <p class="card-text text-muted mb-0" style="font-size: 12px;">
                        ${location.locationAddr}
                    </p>
                </div>

                <button class="btn btn-outline-secondary btn-location d-flex justify-content-center align-items-center"
                        data-index="${index}"
                        style="height: 60px; width: 30px;">
                    <i class="bi bi-trash"></i>
                    <span class="d-none">
                        <span class="location-no">${location.locationNo}</span>
                        <span class="location-x">${location.locationX}</span>
                        <span class="location-y">${location.locationY}</span>
                        <span class="location-type">${location.locationtypeNo}</span>
                        <span class="thirdclass-code">${location.thirdclassCode}</span>
                        <span class="city-code">${location.cityCode}</span>
                        <span class="location-desc">${location.locationDesc}</span>
                        <span class="location-tel">${location.locationTel}</span>
                    </span>
                </button>
            </div>
        </div>
    `).join('');

    // HTML 삽입
    cardArea.innerHTML = html;

    // 마커 추가
    data.forEach((location, index) => {
        if (location.locationX && location.locationY) {
            addMarker(location.locationX, location.locationY, index + 1,"text-primary");
        }
    });
}

function deleteMyLocation(e, selectedItems){
    console.log('deleteMyLocation 함수 시작');
    console.log('클릭된 요소:', e.target);

    if (e.target.classList.contains("btn") || e.target.classList.contains("bi-trash")) {
        // 가장 가까운 .btn-location 버튼 찾기
        const button = e.target.closest(".btn-location");

        if (button) {
            // 해당 버튼에서 locationNo 가져오기
            const locationNo = button.querySelector('.location-no').textContent;
            console.log(selectedItems);
            console.log(locationNo);
            const locationType = button.querySelector('.location-type').textContent;

            // selectedItems에서 해당 locationNo를 가진 항목의 인덱스 찾기
            const removeIndex = selectedItems.findIndex(item => item.locationNo == parseInt(locationNo, 10));
            console.log('클릭된 ', removeIndex);
            if (removeIndex !== -1) {
                // 항목 제거
                const removedItem = selectedItems.splice(removeIndex, 1)[0];
                modifyLocation(selectedItems);
                if (locationType == 1){
                    // 필요한 경우 상태 변경
                    stateChange(removedItem);
                }
            }
        }
    }
}

function stateChange(removedItem) {
    const button = [...document.querySelectorAll('.btn-location')]
        .find(btn => btn.querySelector('.location-no')?.textContent === removedItem.locationNo.toString());

    if (button) {
        button.classList.remove("btn-primary");
        button.classList.add("btn-outline-secondary");
        button.disabled = false;
        button.style.pointerEvents = '';

        const icon = button.querySelector('i');
        if (icon) {
            icon.classList.remove("bi-check-lg");
            icon.classList.add("bi-plus-lg");
        }
    }
}


function checkBtn() {
    const buttons = document.querySelectorAll('#cardArea .btn-location');
    // 각 버튼의 data-index 값 추출 및 처리
    buttons.forEach(button => {
        const dataIndex = button.getAttribute('data-index'); // data-index 값 가져오기
        console.log(`Data Index: ${dataIndex}`);

        const addbtn = document.querySelector(`#addBtn .btn-location[data-index="${dataIndex}"]`);

        if (button) {
            // 아이콘 요소 선택
            const icon = button.querySelector('i');

            // 클래스 변경
            button.classList.remove("btn-outline-secondary");
            button.classList.add("btn-primary");

            icon.classList.remove("bi-plus-lg");
            icon.classList.add("bi-check-lg");

            // 버튼 비활성화
            button.disabled = true;
            button.style.pointerEvents = 'none'; // 클릭 이벤트 완전히 차단
        } else {
            console.warn(`Button with data-index="${index}" not found.`);
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    // locationListDiv 내부의 모든 버튼
    const locationListDiv = document.getElementById('locationListDiv');

    // 클릭 이벤트 처리
    locationListDiv.addEventListener('click', function (event) {
        const clickedButton = event.target.closest('.btn-location'); // 클릭된 버튼 확인
        if (!clickedButton) return; // 클릭된 요소가 버튼이 아니면 종료

        const dataIndex = clickedButton.getAttribute('data-index'); // 클릭된 버튼의 data-index 가져오기
        console.log(`Clicked button with data-index: ${dataIndex}`);

        // 같은 data-index를 가진 버튼을 다시 찾기
        const targetButton = locationListDiv.querySelector(`.btn-location[data-index="${dataIndex}"]`);

        if (targetButton) {
            console.log(`Found target button with data-index: ${dataIndex}`);

            // 아이콘 요소 선택
            const icon = targetButton.querySelector('i');

            // 클래스 변경
            targetButton.classList.remove('btn-outline-secondary');
            targetButton.classList.add('btn-primary');

            // 아이콘 변경
            icon.classList.remove('bi-plus-lg');
            icon.classList.add('bi-check-lg');

            // 버튼 비활성화
            targetButton.disabled = true;
            targetButton.style.pointerEvents = 'none'; // 클릭 이벤트 완전히 차단

            // 필요한 추가 작업 수행 (예: 데이터 저장)
            const locationNo = targetButton.querySelector('.location-no').textContent.trim();
            console.log(`Location No: ${locationNo}`);
        } else {
            console.warn(`No button found with data-index: ${dataIndex}`);
        }
    });
});
