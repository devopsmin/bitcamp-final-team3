typingText.js


// 번갈아가며 표시할 텍스트 배열
const textArray = [
  "고민만 하던 여행 계획",
  "신나는 나만의 여행",
  "숨겨진 명소의 발견",
  "즐거웠던 여행 추억",
  "두근거리는 새로운 만남"
];

// 텍스트 표시를 위한 요소 선택
const typingTextElement = document.querySelector('.typing-text');

let currentTextIndex = 0;

// 텍스트 타이핑 애니메이션 함수
function typeText(text, callback) {
  let charIndex = 0;
  typingTextElement.textContent = ''; // 기존 텍스트 초기화

  const typingInterval = setInterval(() => {
    if (charIndex < text.length) {
      typingTextElement.textContent += text[charIndex];
      charIndex++;
    } else {
      clearInterval(typingInterval);
      setTimeout(callback, 1000); // 다음 텍스트로 넘어가기 전 대기 시간
    }
  }, 100); // 타이핑 속도
}

// 텍스트 삭제 애니메이션 함수
function deleteText(callback) {
  const deleteInterval = setInterval(() => {
    if (typingTextElement.textContent.length > 0) {
      typingTextElement.textContent = typingTextElement.textContent.slice(0, -1);
    } else {
      clearInterval(deleteInterval);
      setTimeout(callback, 500); // 다음 텍스트 타이핑 전 대기 시간
    }
  }, 50); // 삭제 속도
}

// 텍스트 순환 애니메이션 함수
function cycleText() {
  typeText(textArray[currentTextIndex], () => {
    deleteText(() => {
      currentTextIndex = (currentTextIndex + 1) % textArray.length; // 다음 텍스트로 이동
      cycleText();
    });
  });
}

// 애니메이션 시작
cycleText();
