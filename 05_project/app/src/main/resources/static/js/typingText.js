// 번갈아가며 표시할 텍스트 배열
const textArray = [
  "고민만 하던 여행 계획",
  "신나는 나만의 여행",
  "숨겨진 명소의 발견",
  "즐거웠던 여행 추억",
  "두근거리는 새로운 만남"
];

const typingTextElement = document.querySelector('.typing-text');
let currentTextIndex = 0;

function typeText(text, callback) {
  let charIndex = 0;
  typingTextElement.innerHTML = '<span class="cursor"></span>'; // 커서만 남기고 초기화

  const typingInterval = setInterval(() => {
    if (charIndex < text.length) {
      // 커서 앞에 텍스트 추가
      typingTextElement.innerHTML = text.substring(0, charIndex + 1) + '<span class="cursor"></span>';
      charIndex++;
    } else {
      clearInterval(typingInterval);
      setTimeout(callback, 1000);
    }
  }, 100);
}

function deleteText(callback) {
  const deleteInterval = setInterval(() => {
    const currentText = typingTextElement.innerHTML.replace('<span class="cursor"></span>', '');
    if (currentText.length > 0) {
      // 한 글자씩 지우면서 커서 유지
      typingTextElement.innerHTML = currentText.slice(0, -1) + '<span class="cursor"></span>';
    } else {
      clearInterval(deleteInterval);
      setTimeout(callback, 500);
    }
  }, 50);
}

function cycleText() {
  typeText(textArray[currentTextIndex], () => {
    deleteText(() => {
      currentTextIndex = (currentTextIndex + 1) % textArray.length;
      cycleText();
    });
  });
}

cycleText();