const modal = document.getElementById("myModal");
const closeBtn = document.getElementsByClassName("close")[0];
const modalContent = document.getElementById("modalContent");
const openModalBtn = document.getElementById("openModalBtn");


// 모달 열기
openModalBtn.onclick = function () {
    modal.style.display = "block";  // 모달 열기
    fetchPage('/schedule/selectState');  // 첫 번째 페이지를 서버에서 가져오기
}

// 모달 닫기
closeBtn.onclick = function () {
    modal.style.display = "none";  // 모달 닫기
}

// 모달 바깥을 클릭하면 닫기
window.onclick = function (event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

// 서버에서 페이지 HTML 가져오기
function fetchPage(url) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            modalContent.innerHTML = html;
            executeScriptsFromHtml(modalContent);
            attachFormSubmitEvent();  // 폼 제출 이벤트 추가
        })
        .catch(error => {
            modalContent.innerHTML = "데이터를 가져오는 중 오류 발생!";
            console.error('Error fetching page:', error);
        });
}

// 폼 제출을 처리하고 모달 닫기
function attachFormSubmitEvent() {
    const forms = modalContent.querySelectorAll('form'); // 모달 내 모든 form 태그 선택
    forms.forEach(form => {
        form.onsubmit = function () {
            modal.style.display = "none";  // 모달을 닫음
            // 기본 폼 제출 동작을 허용하여 페이지 이동
        };
    });
}

// 동적으로 삽입된 HTML 내부의 스크립트를 실행하는 함수
function executeScriptsFromHtml(htmlContent) {
    const scripts = htmlContent.querySelectorAll('script');
    scripts.forEach(script => {
        const newScript = document.createElement('script');
        if (script.src) {
            newScript.src = script.src;
        } else {
            newScript.text = script.textContent || script.innerHTML;
        }
        document.body.appendChild(newScript);
    });
}
