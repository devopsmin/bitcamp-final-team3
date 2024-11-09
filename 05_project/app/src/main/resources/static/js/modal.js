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
