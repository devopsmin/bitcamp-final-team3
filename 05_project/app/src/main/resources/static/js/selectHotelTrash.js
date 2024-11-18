locationListDiv.addEventListener('click', (event) => {
    const button = event.target.closest('.btn-location');
    if (button) {

        locationData = {
            locationNo: card.querySelector('.location-no').textContent,
            locationName: card.querySelector('.card-title').textContent,
            locationAddr: card.querySelector('.card-text').textContent,
            locationX: card.querySelector('.location-x').textContent,
            locationY: card.querySelector('.location-y').textContent,
            locationtypeNo: card.querySelector('.location-type').textContent,
            thirdclassCode: card.querySelector('.thirdclass-code').textContent,
            cityCode: card.querySelector('.city-code').textContent,
            locationDesc: card.querySelector('.location-desc').textContent,
            locationTel: card.querySelector('.location-tel').textContent,
            locationPhoto: card.querySelector('.location-photo').textContent
        };
        //
        //
        // const cardBody = button.closest('.card-body');
        // imageUrl = cardBody.querySelector('img').getAttribute('src');
        // title = cardBody.querySelector('.card-title').textContent;
        // dataIndex = button.getAttribute('data-index');
        openModal();
    }
});

document.addEventListener("DOMContentLoaded", function () {
    // 모달 콘텐츠 요소 선택
    const modalContent = document.getElementById("modalContent");
    // container 요소 생성
    const container = document.createElement("div");
    container.classList.add("container", "border");
    // row 요소 생성
    const row = document.createElement("div");
    row.classList.add("row", "row-cols-5", "justify-content-center", "p-4");
    // 날짜 범위 내에서 각 날짜마다 col 요소 생성
    for (let date = new Date(startDate); date < endDate; date.setDate(date.getDate() + 1)) {
        // col 생성
        const col = document.createElement("div");
        col.classList.add("col", "d-flex", "flex-column", "justify-content-center", "align-items-center", "border", "border-primary", "rounded", "pt-3", "m-1");
        // 날짜 p 요소 생성 및 설정
        const dateP = document.createElement("p");
        dateP.textContent = date.toISOString().split("T")[0]; // "YYYY-MM-DD" 형식으로 표시
        col.appendChild(dateP);
        // 버튼 생성
        const button = document.createElement("button");
        button.classList.add("btn", "btn-outline-secondary", "btn-location", "d-flex", "justify-content-center", "align-items-center");
        // 아이콘 생성 및 버튼에 추가
        const icon = document.createElement("i");
        icon.classList.add("bi", "bi-plus-lg");
        button.appendChild(icon);
        col.appendChild(button);
        // 텍스트 p 요소 생성
        const textP = document.createElement("p");
        textP.classList.add("text-center", "text-wrap", "w-100");
        textP.classList.add("small-text");
        textP.textContent = "";
        col.appendChild(textP);
        // 정보 저장을 위한 숨김 span 요소 생성
        const infoSpan = document.createElement("span");
        infoSpan.classList.add("d-none"); // Bootstrap의 d-none 클래스로 숨김 처리
        infoSpan.textContent = ""; // 저장할 정보 설정
        // 필요한 부모 요소에 추가 (예: col)
        col.appendChild(infoSpan);
        // col을 row에 추가
        row.appendChild(col);
    }

    // row를 container에 추가
    container.appendChild(row);
    // 최종 container를 modalContent에 추가
    modalContent.appendChild(container);
});

// 모달 열기 함수
function openModal() {
    const modalElement = document.getElementById("myModal");
    const myModal = new bootstrap.Modal(modalElement);
    myModal.show();
}

// 모달이 열릴 때 이전 선택된 상태 복원
modal.addEventListener('show.bs.modal', function () {
    const modalRow = document.querySelector('.modal .row');
    preSelectedItems = [...selectedItems]; // 현재 상태를 복사하여 저장
    if (modalRow) {
        modalRow.querySelectorAll('.btn-location').forEach((modalButton, index) => {
            const modalCol = modalButton.closest('.col');
            const textElement = modalCol.querySelector('.text-center');
            const spanElement = modalCol.querySelector('.d-none');

            // `selectedItems`에 포함된 인덱스는 선택된 상태로 복원
            const selectedItem = selectedItems[index];
            if (selectedItem) {
                modalButton.innerHTML = `<img src="${selectedItem.url}" alt="Selected Image" style="width: 30px; height: 30px; object-fit: cover;">`;
                textElement.textContent = selectedItem.title;
                spanElement.textContent = selectedItem.dtIndex;
            } else {
                modalButton.innerHTML = `<i class="bi bi-plus-lg"></i>`;
                textElement.textContent = "";
                spanElement.textContent = "";
            }
        });
    }
});

// 모달 내에서 버튼 클릭 시 실시간으로 selectedItems 배열 업데이트
modal.addEventListener('click', function (event) {
    if (event.target.closest('.btn-location')) {
        const modalButton = event.target.closest('.btn-location');
        const modalCol = modalButton.closest('.col');
        const textElement = modalCol.querySelector('.text-center');
        const spanElement = modalCol.querySelector('.d-none');
        const index = Array.from(modal.querySelectorAll('.btn-location')).indexOf(modalButton);

        if (modalButton.querySelector('img')) {
            // 선택 해제
            modalButton.innerHTML = `<i class="bi bi-plus-lg"></i>`;
            textElement.textContent = "";
            spanElement.textContent = "";

            // selectedItems 배열에서 해당 항목을 null로 설정하여 선택 해제
            selectedItems[index] = null;
        } else {
            // 선택 추가
            modalButton.innerHTML = `<img data-image-url="${imageUrl}" src="${imageUrl}" alt="Selected Image" style="width: 30px; height: 30px; object-fit: cover;">`;
            textElement.textContent = title;
            spanElement.textContent = dataIndex;

            // selectedItems 배열의 해당 인덱스에 title과 url 정보 저장
            selectedItems[index] = {
                dataIndex: dataIndex,
                title: title,
                url: imageUrl
            };
        }
        console.log(selectedItems);
    }
});

function initializeButtons(buttons) {
    // 1. 모든 버튼을 초기화
    addBtns.forEach(button => {
        button.classList.remove("btn-primary");
        button.classList.add("btn-outline-secondary");

        const icon = button.querySelector("i");
        if (icon) {
            icon.classList.remove("bi-check-lg");
            icon.classList.add("bi-plus-lg");
        }
    });
}

function updateSelectedButtons(dataIndexList) {
    // 2. dataIndexList를 순회하며 해당하는 버튼의 아이콘 업데이트
    dataIndexList.forEach(dataIndex => {
        if (dataIndex !== null) { // null이 아닌 경우만 처리
            // data-index가 해당 dataIndex인 버튼 찾기
            const button = document.querySelector(`.btn-location[data-index="${dataIndex}"]`);
            const icon = button.querySelector("i");

            if (button && icon) {
                // 선택된 버튼 스타일 업데이트
                button.classList.remove("btn-outline-secondary");
                button.classList.add("btn-primary");

                // 아이콘을 체크 아이콘으로 변경
                icon.classList.remove("bi-plus-lg");
                icon.classList.add("bi-check-lg");
            }
        }
    });
}

// 저장 버튼 클릭 시 선택된 항목 유지하고 모달 닫기
saveButton.addEventListener('click', function () {
    const myModal = bootstrap.Modal.getInstance(modal);
    preSelectedItems = [...selectedItems]; // 저장된 상태를 preSelectedItems에 덮어쓰기
    const dataIndexList = selectedItems.map(item => item ? item.dataIndex : null);

    // 버튼 초기화 및 선택된 항목 업데이트
    console.log(selectedItems);
    initializeButtons(addBtns);
    updateSelectedButtons(dataIndexList);
    modifyLocation(dataIndexList);
    myModal.hide(); // 모달 닫기
});

// 모달이 닫힐 때 selectedItems를 preSelectedItems로 복구하는 이벤트 추가
modal.addEventListener('hide.bs.modal', function () {
    selectedItems = [...preSelectedItems]; // 닫기 시 이전 선택 상태로 복구
});

function renderLocationCards(data) {

    const container = document.getElementById("appendMyLocation");
    // markerList의 모든 마커를 지도에서 제거
    markerList.forEach(marker => {
        marker.setMap(null);
    });

    // markerList 배열 비우기 (옵션)
    markerList = [];
    data.forEach((location, index) => {
        const locationDiv = container.children[index];
        // img-tag 및 btn-location 선택
        const imgTag = locationDiv.querySelector(".img-tag");
        const locationButton = locationDiv.querySelector(".btn-location");
        const titleText = locationDiv.querySelector(".card-title");
        const spanX = locationButton.querySelector(".location-x");
        const spanY = locationButton.querySelector(".location-y");
        let icon = locationButton.querySelector("i.bi-trash");

        if (location) {
            // location이 존재하는 경우: 이미지, 제목, 좌표, 삭제 버튼 추가
            addMarker(location.locationX, location.locationY,index+1);
            imgTag.innerHTML = ''; // img-tag 초기화
            const img = document.createElement("img");
            img.src = location.locationPhoto;
            img.alt = "Location Photo";
            img.style.width = "100%";
            img.style.height = "100%";
            img.style.objectFit = "cover";
            imgTag.appendChild(img);

            // 제목 및 날짜 정보 업데이트
            titleText.textContent = location.locationName;

            // 버튼을 표시하고 휴지통 아이콘 추가
            locationButton.style.display = "block";

            // 휴지통 아이콘 추가: 이미 추가된 경우 중복 방지
            if (!icon) {
                icon = document.createElement("i");
                icon.classList.add("bi", "bi-trash");
                locationButton.appendChild(icon);
            }
            // 좌표 업데이트
            spanX.textContent = location.locationX != null ? location.locationX : "N/A";
            spanY.textContent = location.locationY != null ? location.locationY : "N/A";
        } else {
            // location이 없는 경우: 기본 구조로 설정
            imgTag.innerHTML = `
            <div class="btn btn-outline-secondary" id="img-container">
                <i class="bi bi-plus"></i>
            </div>
            `;

            // 기본 제목과 설명 설정
            const titleText = locationDiv.querySelector(".card-title");
            titleText.textContent = "장소를 추가하세요.";

            // 버튼을 숨기고 좌표 초기화
            spanX.textContent = "";
            spanY.textContent = "";
            if (icon) {
                icon.remove();
            }
            locationButton.style.display = "none";
        }
    });
}
appendMyLocation.addEventListener('click', (event) => {
    // 클릭된 요소가 .bi-trash 클래스를 가진 아이콘인지 확인
    const icon = event.target.closest('.bi-trash');
    if (icon) {
        // 아이콘이 속한 버튼 요소를 찾아 인덱스 가져오기
        const button = icon.closest('.btn-location');
        const cardBody = button.closest('.card-body');

        const cardBodies = Array.from(appendMyLocation.querySelectorAll('.card-body'));
        const index = cardBodies.indexOf(cardBody);

        // `card-body` 내부의 img-tag를 초기화
        const imgTag = cardBody.querySelector(".img-tag");
        imgTag.innerHTML = `
        <div class="btn btn-outline-secondary" id="img-container">
            <i class="bi bi-plus"></i>
        </div>
        `;

        // 텍스트 및 날짜 초기화
        const titleText = cardBody.querySelector(".card-title");
        titleText.textContent = "장소를 추가하세요.";

        // 좌표 초기화
        const spanX = button.querySelector(".location-x");
        const spanY = button.querySelector(".location-y");
        spanX.textContent = "";
        spanY.textContent = "";

        // 휴지통 아이콘 삭제 및 버튼 숨기기
        icon.remove();
        button.style.display = "none";

        // 인덱스에 따라 selectedItems 업데이트
        selectedItems[index] = null;
        const dataIndexList = selectedItems.map(item => item ? item.dataIndex : null);

        initializeButtons(addBtns);
        updateSelectedButtons(dataIndexList);
        modifyLocation(dataIndexList);

    }
});

function modifyLocation(dataIndexList) {
    fetch('/schedule/appendMyHotel', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dataIndexList)
    })
        .then(response => response.json()) // 응답 데이터를 JSON 형식으로 파싱
        .then(data => {
            renderLocationCards(data); // 받은 데이터를 이용해 카드 업데이트
            // dataIndexList를 순회하며 해당하는 버튼의 아이콘 업데이트
            dataIndexList.forEach(dataIndex => {
            });
        })
        .catch(error => {
            console.error("Error:", error);
        });
}
