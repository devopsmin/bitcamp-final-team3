function initModalEvents() {
    let selectedLocations = Array(totalDay).fill(null);
    let previousLocations = [];
    let myModal = null;  // 초기값 설정

    // Modal 초기화 함수
    function initModal() {
        const modalElement = document.getElementById('myModal');
        myModal = new bootstrap.Modal(modalElement);
        createModalContent();
    }

    // 버튼 초기화
    function initializeButtons() {
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

    // 선택된 버튼 업데이트
    function updateSelectedButtons(dataIndexList) {
        dataIndexList.forEach(dataIndex => {
            if (dataIndex !== null) {
                const button = document.querySelector(`.btn-location[data-index="${dataIndex}"]`);
                const icon = button?.querySelector("i");
                if (button && icon) {
                    button.classList.remove("btn-outline-secondary");
                    button.classList.add("btn-primary");
                    icon.classList.remove("bi-plus-lg");
                    icon.classList.add("bi-check-lg");
                }
            }
        });
    }

    // 위치 카드 렌더링
    function renderLocationCards(data) {
        const container = document.getElementById("appendMyLocation");
        // 마커 초기화
        markerList.forEach(marker => marker.setMap(null));
        markerList = [];

        data.forEach((location, index) => {
            const locationDiv = container.children[index];
            const imgTag = locationDiv.querySelector(".img-tag");
            const locationButton = locationDiv.querySelector(".btn-location");
            const titleText = locationDiv.querySelector(".card-title");
            const spanX = locationButton.querySelector(".location-x");
            const spanY = locationButton.querySelector(".location-y");
            let icon = locationButton.querySelector("i.bi-trash");

            if (location) {
                // 위치 정보가 있는 경우
                addMarker(location.locationX, location.locationY, index+1);

                // 이미지 설정
                imgTag.innerHTML = '';
                const img = document.createElement("img");
                img.src = location.locationPhoto;
                img.alt = "Location Photo";
                img.style.cssText = "width:100%;height:100%;object-fit:cover;";
                imgTag.appendChild(img);

                // 정보 업데이트
                titleText.textContent = location.locationName;
                locationButton.style.display = "block";

                // 휴지통 아이콘
                if (!icon) {
                    icon = document.createElement("i");
                    icon.classList.add("bi", "bi-trash");
                    locationButton.appendChild(icon);
                }

                // 좌표 업데이트
                spanX.textContent = location.locationX ?? "N/A";
                spanY.textContent = location.locationY ?? "N/A";
            } else {
                // 위치 정보가 없는 경우 초기화
                imgTag.innerHTML = `
                    <div class="btn btn-outline-secondary" id="img-container">
                        <i class="bi bi-plus"></i>
                    </div>
                `;
                titleText.textContent = "장소를 추가하세요.";
                spanX.textContent = "";
                spanY.textContent = "";
                if (icon) icon.remove();
                locationButton.style.display = "none";
            }
        });
    }

    // 모달 DOM 생성
    function createModalContent() {
        const container = document.createElement("div");
        container.classList.add("container", "border");
        const row = document.createElement("div");
        row.classList.add("row", "row-cols-5", "justify-content-center", "p-4");

        for (let date = new Date(startDate); date < endDate; date.setDate(date.getDate() + 1)) {
            const col = createDateColumn(date);
            row.appendChild(col);
        }

        container.appendChild(row);
        modalContent.appendChild(container);
    }

    // 날짜 컬럼 생성
    function createDateColumn(date) {
        const col = document.createElement("div");
        col.classList.add("col", "d-flex", "flex-column", "justify-content-center", "align-items-center",
            "border", "border-primary", "rounded", "pt-3", "m-1");

        const dateP = document.createElement("p");
        dateP.textContent = date.toISOString().split("T")[0];

        const button = document.createElement("button");
        button.classList.add("btn", "btn-outline-secondary", "btn-location",
            "d-flex", "justify-content-center", "align-items-center");
        button.innerHTML = '<i class="bi bi-plus-lg"></i>';

        const textP = document.createElement("p");
        textP.classList.add("text-center", "text-wrap", "w-100", "small-text");

        col.append(dateP, button, textP);
        return col;
    }

    // 모달 열기 함수
    function openModal(locationData) {
        if (!myModal) {
            initModal(); // 모달이 없으면 초기화
        }
        previousLocations = [...selectedLocations];
        updateModalDisplay();
        myModal.show();
    }

    // 모달 내용 업데이트
    function updateModalDisplay() {
        const modalButtons = modal.querySelectorAll('.btn-location');
        modalButtons.forEach((button, index) => {
            const col = button.closest('.col');
            const textElement = col.querySelector('.text-center');
            const location = previousLocations[index];

            if(location) {
                button.innerHTML = `<img src="${location.locationPhoto}" style="width:30px;height:30px;object-fit:cover;">`;
                textElement.textContent = location.locationName;
            } else {
                button.innerHTML = '<i class="bi bi-plus-lg"></i>';
                textElement.textContent = "";
            }
        });
    }

    // 모달 내 버튼 클릭 처리
    function handleModalClick(event) {
        const modalButton = event.target.closest('.btn-location');
        if(!modalButton) return;

        const index = Array.from(modal.querySelectorAll('.btn-location')).indexOf(modalButton);
        const col = modalButton.closest('.col');
        const textElement = col.querySelector('.text-center');

        if(modalButton.querySelector('img')) {
            selectedLocations[index] = null;
            modalButton.innerHTML = '<i class="bi bi-plus-lg"></i>';
            textElement.textContent = "";
        } else {
            // locationData가 있을 때만 처리
                selectedLocations[index] = locationData;
                modalButton.innerHTML = `<img src="${locationData.locationPhoto}" style="width:30px;height:30px;object-fit:cover;">`;
                textElement.textContent = locationData.locationName;
        }

    }

    function updateLocationButtons(selectedLocations) {
        // 모든 버튼 초기화
        const allButtons = document.querySelectorAll('#addBtn');
        allButtons.forEach(button => {
            const icon = button.querySelector('i');
            button.classList.remove('btn-primary');
            button.classList.add('btn-outline-secondary');
            icon.classList.remove('bi-check-lg');
            icon.classList.add('bi-plus-lg');
        });

        // 선택된 버튼만 업데이트
        selectedLocations.forEach(location => {
            if (location) {
                allButtons.forEach(button => {
                    const locationNo = button.querySelector('.location-no').textContent;
                    if(locationNo === location.locationNo) {
                        const icon = button.querySelector('i');
                        button.classList.remove('btn-outline-secondary');
                        button.classList.add('btn-primary');
                        icon.classList.remove('bi-plus-lg');
                        icon.classList.add('bi-check-lg');
                    }
                });
            }
        });
    }

    // 저장 처리
    function handleSave() {
        fetch('/schedule/appendMyHotel', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(selectedLocations)
        })
            .then(response => response.json())
            .then(data => {
                renderLocationCards(data);
                previousLocations = [...selectedLocations];
                initializeButtons();
                // updateSelectedButtons(selectedLocations.map(item => item ? item.locationNo : null));
                updateLocationButtons(selectedLocations);
                myModal.hide();
            })
            .catch(error => console.error("Error:", error));
    }

    // 이벤트 리스너 등록
    modal.addEventListener('click', handleModalClick);
    saveButton.addEventListener('click', handleSave);
    modal.querySelector('[data-bs-dismiss="modal"]').addEventListener('click', () => {
        selectedLocations = [...previousLocations];
        myModal.hide();
    });

    // 초기 모달 콘텐츠 생성
    initModal();

    // 외부에서 필요한 함수만 노출
    return { openModal };
}

// appendMyLocation.addEventListener('click', (event) => {
//     // 클릭된 요소가 .bi-trash 클래스를 가진 아이콘인지 확인
//     const icon = event.target.closest('.bi-trash');
//     if (icon) {
//         // 아이콘이 속한 버튼 요소를 찾아 인덱스 가져오기
//         const button = icon.closest('.btn-location');
//         const cardBody = button.closest('.card-body');
//
//         const cardBodies = Array.from(appendMyLocation.querySelectorAll('.card-body'));
//         const index = cardBodies.indexOf(cardBody);
//
//         // `card-body` 내부의 img-tag를 초기화
//         const imgTag = cardBody.querySelector(".img-tag");
//         imgTag.innerHTML = `
//         <div class="btn btn-outline-secondary" id="img-container">
//             <i class="bi bi-plus"></i>
//         </div>
//         `;
//
//         // 텍스트 및 날짜 초기화
//         const titleText = cardBody.querySelector(".card-title");
//         titleText.textContent = "장소를 추가하세요.";
//
//         // 좌표 초기화
//         const spanX = button.querySelector(".location-x");
//         const spanY = button.querySelector(".location-y");
//         spanX.textContent = "";
//         spanY.textContent = "";
//
//         // 휴지통 아이콘 삭제 및 버튼 숨기기
//         icon.remove();
//         button.style.display = "none";
//
//         // 인덱스에 따라 selectedItems 업데이트
//         selectedItems[index] = null;
//         const dataIndexList = selectedItems.map(item => item ? item.dataIndex : null);
//
//         initializeButtons(addBtns);
//         updateSelectedButtons(dataIndexList);
//         modifyLocation(dataIndexList);
//
//     }

// function updateSelectedButtons(dataIndexList) {
//     // 2. dataIndexList를 순회하며 해당하는 버튼의 아이콘 업데이트
//     dataIndexList.forEach(dataIndex => {
//         if (dataIndex !== null) { // null이 아닌 경우만 처리
//             // data-index가 해당 dataIndex인 버튼 찾기
//             const button = document.querySelector(`.btn-location[data-index="${dataIndex}"]`);
//             const icon = button.querySelector("i");
//
//             if (button && icon) {
//                 // 선택된 버튼 스타일 업데이트
//                 button.classList.remove("btn-outline-secondary");
//                 button.classList.add("btn-primary");
//
//                 // 아이콘을 체크 아이콘으로 변경
//                 icon.classList.remove("bi-plus-lg");
//                 icon.classList.add("bi-check-lg");
//             }
//         }
//     });
// }
