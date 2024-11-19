// modal.js
document.addEventListener('DOMContentLoaded', function() {
    const scheduleModalEl = document.getElementById('scheduleModal');

    if (scheduleModalEl) {
        const modalOptions = {
            backdrop: true,
            keyboard: true,
            focus: true
        };

        const scheduleModal = new bootstrap.Modal(scheduleModalEl, modalOptions);

        scheduleModalEl.addEventListener('show.bs.modal', function() {
            fetch('/schedule/selectState')
                .then(response => response.text())
                .then(html => {
                    document.getElementById('scheduleModalContent').innerHTML = html;
                    initializeScheduleForm();
                })
                .catch(error => {
                    console.error('Error loading schedule form:', error);
                });
        });
    }
});

function initializeScheduleForm() {
    const scheduleForm = document.querySelector('#scheduleModalContent form');
    const stateSelect = document.querySelector('#scheduleModalContent #stateCode');
    const citySelect = document.querySelector('#scheduleModalContent #cityCode');

    // 시도 선택 이벤트 리스너
    if (stateSelect) {
        stateSelect.addEventListener('change', function() {
            const stateCode = this.value;

            if (stateCode) {
                fetch('/schedule/selectCity', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain',
                    },
                    body: stateCode
                })
                    .then(response => response.json())
                    .then(cityList => {
                        const citySelect = document.querySelector('#scheduleModalContent #cityCode');
                        citySelect.innerHTML = '<option value="">시군구 선택</option>';

                        cityList.forEach(city => {
                            const option = document.createElement('option');
                            option.value = city.cityCode;
                            option.textContent = city.cityName;
                            citySelect.appendChild(option);
                        });
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('시군구 목록을 불러오는데 실패했습니다.');
                    });
            } else {
                const citySelect = document.querySelector('#scheduleModalContent #cityCode');
                citySelect.innerHTML = '<option value="">시군구 선택</option>';
            }
        });
    }

    // 시군구 선택 이벤트 리스너
    if (citySelect) {
        citySelect.addEventListener('change', function () {
            const stateName = stateSelect.options[stateSelect.selectedIndex]?.text?.trim() || '';
            const cityName = this.options[this.selectedIndex]?.text?.trim() || '';

            if (stateName && cityName) {
                const imagePath = `/images/city/${stateName}/${stateName} ${cityName}.jpg`;
                console.log(imagePath);

                // 오른쪽 컨텐츠 영역에 이미지를 삽입
                const imageWrapper = document.getElementById('imageWrapper');
                if (imageWrapper) {
                    imageWrapper.innerHTML = `<img src="${imagePath}" alt="${stateName} ${cityName}"
                    class="img-fluid rounded border" style="width: 400px; height: 300px;">`;
                } else {
                    console.error('이미지 래퍼를 찾을 수 없습니다.');
                }
            } else {
                // 이미지 제거 (기본 상태로 초기화)
                const imageWrapper = document.getElementById('imageWrapper');
                if (imageWrapper) {
                    imageWrapper.innerHTML = `<p>이미지를 선택하려면 시군구를 선택하세요.</p>`;
                }
            }
        });
    }

    // 폼 제출 이벤트 리스너
    if (scheduleForm) {
        scheduleForm.addEventListener('submit', function(e) {
            e.preventDefault();

            const selectedCity = document.querySelector('#scheduleModalContent #cityCode').value;
            if (!selectedCity) {
                alert('시군구를 선택해주세요.');
                return;
            }

            const cityCodeHidden = document.querySelector('#scheduleModalContent #cityCodeHidden');
            if (cityCodeHidden) {
                cityCodeHidden.value = selectedCity;
            }

            // Form을 직접 제출하는 방식으로 변경
            this.method = 'POST';  // POST 방식으로 설정
            this.action = '/schedule/selectDate';  // action URL 설정
            this.submit();  // 폼 제출
        });
    }
}
