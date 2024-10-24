// 모달 요소
const modal = document.getElementById("myModal");
const closeBtn = document.getElementsByClassName("close")[0];
const modalContent = document.getElementById("modalContent");
const openModalBtn = document.getElementById("openModalBtn");

// 모달 열기
openModalBtn.onclick = function () {
    modal.style.display = "block";  // 모달 열기
    fetchPage('/schedule/main');       // 첫 번째 페이지를 서버에서 받아오기
}

// 모달 닫기
closeBtn.onclick = function () {
    modal.style.display = "none";   // 모달 닫기
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
        .then(response => response.text())  // 서버로부터 받은 HTML을 텍스트로 변환
        .then(html => {
            modalContent.innerHTML = html;  // 모달에 HTML 삽입
            attachFormSubmitEvent();        // 폼 제출 이벤트 추가
            attachButtonSubmitEvent();
            executeScriptsFromHtml(modalContent) // 동적으로 삽입된 스크립트 실행
        })
        .catch(error => {
            modalContent.innerHTML = "데이터를 가져오는 중 오류 발생!";
            console.error('Error fetching page:', error);
        });
}

// 폼 제출을 AJAX로 처리
function attachFormSubmitEvent() {
    const forms = document.querySelectorAll('form'); // 모든 form 태그 선택
    forms.forEach(form => {
        form.onsubmit = function (event) {
            event.preventDefault();  // 폼의 기본 제출 동작(페이지 새로고침)을 막음

            // form의 action 속성에서 URL을 추출하여 서버에 요청
            const formData = new FormData(form);
            let url = form.action;

            // POST 요청을 통해 폼 데이터 전송
            fetch(url, {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())  // 응답을 텍스트로 변환 (HTML 페이지)
                .then(html => {
                    modalContent.innerHTML = html;  // 모달에 새로운 페이지(응답된 HTML)를 렌더링
                    attachFormSubmitEvent();        // 새로 로드된 페이지에도 이벤트 연결
                    attachButtonSubmitEvent();      // 새로 로드된 페이지에도 버튼 이벤트 연결
                })
                .catch(error => {
                    modalContent.innerHTML = "데이터를 가져오는 중 오류 발생!";
                    console.error('Error submitting form:', error);
                });
        };
    });
}

// 버튼 제출 AJAX로 처리
function attachButtonSubmitEvent() {
    const buttons = document.querySelectorAll('button');  // 모든 button 요소를 선택
    buttons.forEach(button => {
        if (button.getAttribute('data-url')) {  // data-url 속성이 있는 버튼만 처리
            button.onclick = function (event) {
                event.preventDefault();  // 버튼 기본 동작 방지 (필요 시)

                // 버튼의 데이터 속성 또는 URL을 가져옴
                const url = button.getAttribute('data-url');  // data-url 속성에서 값을 가져옴
                console.log('Button Data:', url);  // 가져온 데이터를 확인

                // GET 요청을 통해 서버로 URL 전송
                fetch(url, {
                    method: 'GET'  // GET 요청으로 변경
                })
                    .then(response => response.text())  // 응답을 텍스트로 변환 (HTML 페이지)
                    .then(html => {
                        modalContent.innerHTML = html;  // 모달에 새로운 페이지(응답된 HTML)를 렌더링
                        attachFormSubmitEvent();        // 새로 로드된 페이지에도 이벤트 연결
                        attachButtonSubmitEvent();      // 새로 로드된 페이지에도 버튼 이벤트 연결
                    })
                    .catch(error => {
                        modalContent.innerHTML = "데이터를 가져오는 중 오류 발생!";
                        console.error('Error fetching page:', error);
                    });
            }
        }
    });
}

// 동적으로 삽입된 HTML 내부의 스크립트를 실행하는 함수
function executeScriptsFromHtml(htmlContent) {
    const scripts = htmlContent.querySelectorAll('script');  // HTML 내의 모든 <script> 태그를 찾음
    scripts.forEach(script => {
        const newScript = document.createElement('script');  // 새로운 <script> 태그를 생성
        if (script.src) {
            // 외부 스크립트 파일을 로드하는 경우
            newScript.src = script.src;
        } else {
            // 인라인 스크립트인 경우
            newScript.text = script.innerHTML;
        }
        document.body.appendChild(newScript);  // <script> 태그를 DOM에 삽입하여 실행
    });
}
