// searchLocation.js
const searchElements = {
    input: null,
    icon: null,
    resultsContainer: null,
    clearButton: null
};

let markers = [];

function initializeSearch() {
    searchElements.input = document.querySelector('#addLocationDiv .form-control');
    searchElements.icon = document.querySelector('#addLocationDiv .bi-search');
    searchElements.resultsContainer = document.querySelector('#addLocationDiv .mb-2');

    // x 버튼 생성
    const inputGroup = searchElements.input.parentElement;
    searchElements.clearButton = document.createElement('span');
    searchElements.clearButton.className = 'input-group-text bg-white border-start-0 cursor-pointer';
    searchElements.clearButton.innerHTML = '<i class="bi bi-x-lg text-muted"></i>';
    searchElements.clearButton.style.cursor = 'pointer';
    searchElements.clearButton.style.display = 'none';
    inputGroup.appendChild(searchElements.clearButton);
}

// 메인 검색 함수
function performSearch(searchText) {
    return fetch(`/schedule/searchLocation?searchText=${searchText}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(data => {
            let resultsHTML = '';

            // 기존 마커 제거
            markers.forEach(marker => marker.setMap(null));
            markers = [];

            // 검색 결과 처리
            data.forEach((item, index) => {
                // 카드 HTML 생성
                resultsHTML += createCardHTML(item, index);

                // 지도 마커 생성
                const marker = createMapMarker(item);
                markers.push(marker);
            });

            // 결과 표시 및 이벤트 연결
            searchElements.resultsContainer.innerHTML = resultsHTML;

            // 이벤트 리스너 추가
            document.querySelectorAll('.hover-card').forEach((card, index) => {
                addHoverEvents(card, index);
            });

            document.querySelectorAll('.btn-location').forEach(button => {
                addClickEvent(button);
            });
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}

function handleInputChange() {
    searchElements.clearButton.style.display = searchElements.input.value ? 'flex' : 'none';
}

function clearSearch() {
    searchElements.input.value = '';
    searchElements.clearButton.style.display = 'none';
    searchElements.resultsContainer.innerHTML = '';
    markers.forEach(marker => marker.setMap(null));
    markers = [];
}

function addHoverEvents(card, index) {
    card.addEventListener('mouseenter', () => {
        card.classList.add('bg-light');
        if (markers[index]) {
            markers[index].setIcon({
                content: `<i class="bi bi-geo-alt-fill text-warning" style="font-size: 40px;"></i>`
            });
        }
    });

    card.addEventListener('mouseleave', () => {
        card.classList.remove('bg-light');
        if (markers[index]) {
            markers[index].setIcon({
                content: `<i class="bi bi-geo-alt-fill text-primary" style="font-size: 24px;"></i>`
            });
        }
    });
}

// 버튼 클릭 이벤트 처리 함수
function addClickEvent(button) {
    button.addEventListener('click', function() {
        const card = this.closest('.card');
        const modal = document.getElementById('locationSearchModal');

        // 장소명과 주소 설정
        modal.querySelector('.location-title').textContent = card.querySelector('.card-title').textContent;
        modal.querySelector('.location-address').textContent = card.querySelector('.card-text').textContent;
        modal.querySelector('.location-x').textContent = card.querySelector('.location-x').textContent;
        modal.querySelector('.location-y').textContent = card.querySelector('.location-y').textContent;
        // 모달 열기
        const bsModal = new bootstrap.Modal(modal);
        bsModal.show();
    });
}

// 카드 HTML 생성 함수
function createCardHTML(item, index) {
    const cleanTitle = item.title.replace(/<[^>]*>/g, '');
    return `
        <div class="card mb-2 hover-card" data-index="${index}">
            <div class="card-body d-flex align-items-center justify-content-between gap-1">
                <div class="d-flex flex-column" style="flex: 1;">
                    <h5 class="card-title mb-1" style="font-size: 16px;">${cleanTitle}</h5>
                    <p class="card-text text-muted mb-0" style="font-size: 12px;">${item.roadAddress}</p>
                </div>

                <button class="btn btn-outline-secondary btn-location d-flex justify-content-center align-items-center"
                        data-index="${index}"
                        style="height: 60px; width: 30px;">
                    <i class="bi bi-plus-lg"></i>
                    <span class="location-x d-none">${item.mapx / 10000000.0}</span>
                    <span class="location-y d-none">${item.mapy / 10000000.0}</span>
                </button>
            </div>
        </div>
    `;
}

// 지도 마커 생성 함수
function createMapMarker(item) {
    const pos = new naver.maps.LatLng(item.mapy / 10000000.0, item.mapx / 10000000.0);
    return new naver.maps.Marker({
        position: pos,
        map: minimap,
        Icon: {
            content: `<i class="bi bi-geo-alt-fill text-primary" style="font-size: 24px;"></i>`
        }
    });
}
