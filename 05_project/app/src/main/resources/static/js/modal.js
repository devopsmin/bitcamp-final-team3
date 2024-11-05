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

    initializeCustomAlert();
});

function initializeScheduleForm() {
    const scheduleForm = document.querySelector('#scheduleModalContent form');
    if (scheduleForm) {
        scheduleForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);

            fetch(this.action, {
                method: 'POST',
                body: formData
            })
            .then(response => response.text())
            .then(html => {
                document.getElementById('scheduleModalContent').innerHTML = html;
                initializeScheduleForm();
            })
            .catch(error => {
                console.error('Error submitting schedule form:', error);
                showCustomAlert('오류', '일정 생성 중 오류가 발생했습니다.');
            });
        });
    }
}

function initializeCustomAlert() {
    const customAlertModal = document.getElementById('customAlertModal');
    const customAlertCloseBtn = document.getElementById('customAlertCloseBtn');

    if (customAlertCloseBtn) {
        customAlertCloseBtn.onclick = function() {
            customAlertModal.style.display = "none";
        }
    }
}

function showCustomAlert(title, message) {
    const customAlertModal = document.getElementById('customAlertModal');
    const customAlertTitle = document.getElementById('customAlertTitle');
    const customAlertMessage = document.getElementById('customAlertMessage');

    if (customAlertTitle) customAlertTitle.textContent = title;
    if (customAlertMessage) customAlertMessage.textContent = message;
    if (customAlertModal) customAlertModal.style.display = "block";
}
