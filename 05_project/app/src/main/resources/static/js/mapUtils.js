// map-utils.js

// 줌 레벨 상수
const ZOOM_LEVELS = {
    'area1': 11,  // 시/도
    'area2': 13,  // 시/군/구
    'area3': 15,  // 읍/면/동
    'area4': 17   // 리
};

let map = null;
let markerList = [];

// 지도 초기화 함수
function initializeMap(containerId, stateName, cityName) {
    let regionType = 'area1';
    const searchQuery = cityName === "전체" ? stateName : stateName+cityName;
    console.log(searchQuery);

    naver.maps.Service.geocode({
        query: searchQuery
    }, function (status, response) {
        if (status !== naver.maps.Service.Status.OK) {
            console.error('지도 초기화 실패');
            return;
        }

        const items = response.v2.addresses;
        if (items.length === 0) {
            console.error('주소를 찾을 수 없습니다');
            return;
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
    });

    return map;
}

// 마커 추가 함수
function addMarker(locationX, locationY) {
    let pos = new naver.maps.LatLng(locationY, locationX);
    let marker = new naver.maps.Marker({
        position: pos,
        map: map,
    });

    marker.locationX = locationX;
    marker.locationY = locationY;
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
