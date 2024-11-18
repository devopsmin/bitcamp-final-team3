// searchLocation.js
const searchElements = {
    input: null,
    icon: null,
    resultsContainer: null,
    clearButton: null
};

let selectedLocationData = null;
let selectedDay = null;
let markers = [];
let isInitialized = false;  // 초기화 상태를 추적하는 플래그 추가

document.addEventListener('DOMContentLoaded', () => {
    if (!isInitialized) {  // 한 번만 초기화되도록 체크
        initializeSearch();
        isInitialized = true;
    }
});

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

function addSearchButtonClickEvent() {
    const searchBtns = document.querySelectorAll('#addSearchBtn');
    searchBtns.forEach(btn => {
        btn.addEventListener('click', (e) => {
            const button = e.target.closest('#addSearchBtn');
            const card = button.closest('.card-body');
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
                locationPhoto: '/images/default.jpg'
            };
            console.log(locationData)

            modalHandler.openModal(locationData);  // 같은 modalHandler 사용
        });
    });
}

// 메인 검색 함수
function performSearch(searchText) {
    return fetch(`/schedule/searchHotel?searchText=${searchText}`, {
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
                console.log(item)
                // 카드 HTML 생성
                resultsHTML += createCardHTML(item, index);

                // 지도 마커 생성
                const marker = createMapMarker(item);
                markers.push(marker);
            });

            // 결과 표시 및 이벤트 연결
            searchElements.resultsContainer.innerHTML = resultsHTML;

            addSearchButtonClickEvent();
            // 이벤트 리스너 추가
            document.querySelectorAll('.hover-card').forEach((card, index) => {
                addHoverEvents(card, index);
            });

            // document.querySelectorAll('#addSearchBtn').forEach(button => {
            //     addClickEvent(button);
            // });
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
                content: `<i class="bi bi-geo-alt-fill text-warning" style="font-size: 48px;"></i>`,
                anchor: new naver.maps.Point(24, 48) // 마커 중심점 조정
            });
        }
    });

    card.addEventListener('mouseleave', () => {
        card.classList.remove('bg-light');
        if (markers[index]) {
            markers[index].setIcon({
                content: `<i class="bi bi-geo-alt-fill text-primary" style="font-size: 24px;"></i>`,
                anchor: new naver.maps.Point(12, 24) // 마커 중심점 조정
            });
        }
    });
}

// 카드 HTML 생성 함수
function createCardHTML(item, index) {
    const cleanTitle = item.locationName.replace(/<[^>]*>/g, '');
    return `
       <div class="card mb-2 hover-card" data-index="${index}">
           <div class="card-body d-flex align-items-center justify-content-between gap-1">
               <div class="d-flex flex-column" style="flex: 1;">
                   <h5 class="card-title mb-1" style="font-size: 16px;">${cleanTitle}</h5>
                   <p class="card-text text-muted mb-0" style="font-size: 12px;">${item.locationAddr}</p>
               </div>

               <button id="addSearchBtn" class="btn btn-outline-secondary btn-location d-flex justify-content-center align-items-center"
                       data-index="${index}"
                       style="height: 60px; width: 30px;">
                   <i class="bi bi-plus-lg"></i>
                   <span class="location-no d-none">${item.locationNo}</span>
                   <span class="location-x d-none">${item.locationX}</span>
                   <span class="location-y d-none">${item.locationY}</span>
                   <span class="location-type d-none">${item.locationtypeNo}</span>
                   <span class="thirdclass-code d-none">${item.thirdclassCode}</span>
                   <span class="city-code d-none">${item.cityCode}</span>
                   <span class="location-desc d-none">${item.locationDesc}</span>
                   <span class="location-tel d-none">${item.locationTel}</span>
                   <span class="location-photo d-none">${item.locationPhoto}</span>
               </button>
           </div>
       </div>
   `;
}

// 지도 마커 생성 함수
function createMapMarker(item) {
    const pos = new naver.maps.LatLng(item.locationY, item.locationX);
    return new naver.maps.Marker({
        position: pos,
        map: minimap,
        icon: {
            content: `<div class="marker-content"><i class="bi bi-geo-alt-fill text-primary" style="font-size: 24px;"></i></div>`,
            anchor: new naver.maps.Point(12, 24) // 마커 중심점 조정
        }
    });
}
