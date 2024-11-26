// map-utils.js

// 줌 레벨 상수
let map = null;
let minimap = null;

let markerList = [];
let polylineList = [];

const ZOOM_LEVELS = {
    'area1': 12,  // 시/도
    'area2': 14,  // 시/군/구
    'area3': 15,  // 읍/면/동
    'area4': 17   // 리
};

async function initializeMap(containerId, stateName, cityName) {
    try {
        let regionType = 'area1';
        const searchQuery = cityName === "전체" ? stateName : stateName + cityName;

        const response = await new Promise((resolve, reject) => {
            naver.maps.Service.geocode({
                query: searchQuery
            }, function(status, response) {
                if (status === naver.maps.Service.Status.OK) {
                    resolve(response);
                } else {
                    reject('지도 초기화 실패');
                }
            });
        });

        const items = response.v2.addresses;
        if (items.length === 0) {
            throw new Error('주소를 찾을 수 없습니다');
        }

        const {x: mapx, y: mapy} = items[0];
        const centerPosition = new naver.maps.LatLng(mapy, mapx);
        const zoomLevel = ZOOM_LEVELS[regionType] || ZOOM_LEVELS.area2;

        if (!map) {
            map = new naver.maps.Map(containerId, {
                center: centerPosition,
                zoom: zoomLevel
            });
        } else {
            map.setCenter(centerPosition);
            map.setZoom(zoomLevel);
        }

        return map;
    } catch (error) {
        console.error('지도 초기화 중 오류:', error);
        throw error;
    }
}

async function searchMap(containerId, stateName, cityName) {
    try {
        let regionType = 'area1';
        const searchQuery = cityName === "전체" ? stateName : stateName + cityName;

        const response = await new Promise((resolve, reject) => {
            naver.maps.Service.geocode({
                query: searchQuery
            }, function(status, response) {
                if (status === naver.maps.Service.Status.OK) {
                    resolve(response);
                } else {
                    reject('지도 초기화 실패');
                }
            });
        });

        const items = response.v2.addresses;
        if (items.length === 0) {
            throw new Error('주소를 찾을 수 없습니다');
        }

        const {x: mapx, y: mapy} = items[0];
        const centerPosition = new naver.maps.LatLng(mapy, mapx);
        const zoomLevel = ZOOM_LEVELS[regionType] || ZOOM_LEVELS.area2;

        if (!minimap) {
            minimap = new naver.maps.Map(containerId, {
                center: centerPosition,
                zoom: zoomLevel
            });
        } else {
            minimap.setCenter(centerPosition);
            minimap.setZoom(zoomLevel);
        }

        return minimap;
    } catch (error) {
        console.error('지도 초기화 중 오류:', error);
        throw error;
    }
}

function addMarker(locationX, locationY, index, textColor) {
    let pos = new naver.maps.LatLng(locationY, locationX);
    let marker = new naver.maps.Marker({
        position: pos,
        map: map,
        icon: {
            content: `
                <div class="position-relative d-flex justify-content-center align-items-center" style="width: 40px; height: 40px;">
                    <i class="position-absolute bottom-0 bi bi-geo-alt-fill ${textColor}" style="font-size: 36px;"></i>
                    <div class="position-absolute top-0 d-flex justify-content-center align-items-center rounded-circle bg-white text-dark fw-bold" style="width: 20px; height: 20px; font-size: 15px;">
                        ${index}
                    </div>
                </div>
            `,
            size: new naver.maps.Size(100, 100),
            anchor: new naver.maps.Point(12, 30)
        }
    });
    markerList.push(marker);
    return marker;
}

// 마커 제거 함수
function removeMarker(locationX, locationY) {
    markerList = markerList.filter(marker => {
        const isMatch = marker.locationX === locationX && marker.locationY === locationY;
        if (isMatch) {
            marker.setMap(null);
        }
        return !isMatch;
    });
}

// 모든 마커 제거 함수
function clearAllMarkers() {
    markerList.forEach(marker => {
        marker.setMap(null);
    });
    markerList = [];
}

function drawPath(pathCoordinates, color) {
    const path = pathCoordinates.map(coord =>
        new naver.maps.LatLng(coord.latitude, coord.longitude)
    );

    const newPolyline = new naver.maps.Polyline({
        map: map,
        path: path,
        strokeColor: color,
        strokeOpacity: 1,
        strokeWeight: 3
    });

    polylineList.push(newPolyline);
}

// 특정 폴리라인 제거 함수
function removePolyline(index) {
    if (polylineList[index]) {
        polylineList[index].setMap(null);
        polylineList.splice(index, 1);
    }
}

// 모든 폴리라인 제거 함수
function clearAllPolylines() {
    polylineList.forEach(polyline => {
        polyline.setMap(null);
    });
    polylineList = [];
}

// 특정 구간의 폴리라인 숨기기/보이기
function togglePolyline(index, visible) {
    if (polylineList[index]) {
        polylineList[index].setMap(visible ? map : null);
    }
}

// 폴리라인 스타일 변경
function updatePolylineStyle(index, options) {
    if (polylineList[index]) {
        polylineList[index].setOptions(options);
    }
}


function addMarkerWithPopover(locationX, locationY, index, textColor, locationName, locationAddr, locationPhoto) {
    const marker = addMarker(locationX, locationY, index, textColor);

    const markerElement = marker.getElement();
    if (markerElement) {
        markerElement.setAttribute('data-marker-index', index);
        markerElement.setAttribute('data-location-name', locationName);
        markerElement.setAttribute('data-location-addr', locationAddr);
        markerElement.setAttribute('data-location-photo', locationPhoto || 'https://via.placeholder.com/70x70');

        // 팝오버 설정
        const popoverContent = `
            <div>
                <strong>${locationName}</strong><br>
                ${locationAddr}<br>
                <img src="${locationPhoto || 'https://via.placeholder.com/70x70'}" alt="${locationName}" style="width: 70px; height: 70px; object-fit: cover; margin-top: 5px; !important;">
            </div>
        `;

        markerElement.setAttribute('data-bs-toggle', 'popover');
        markerElement.setAttribute('data-bs-html', 'true');
        markerElement.setAttribute('data-bs-content', popoverContent);

        new bootstrap.Popover(markerElement, {
            container: 'body', // 팝오버가 body에 추가되도록 설정
            customClass: 'custom-popover', // 커스텀 클래스를 추가
            });
    }

    return marker;
}