// 스크롤 이동 함수
function scrollToSection(sectionId) {
  let target;

  if (sectionId === 'top') {
    target = document.body; // 상단으로 이동
  } else {
    target = document.getElementById(sectionId); // 특정 섹션으로 이동
  }

  if (target) {
    target.scrollIntoView({ behavior: 'smooth' });
  }
}

// 스크롤 이벤트 감지하여 버튼 표시
window.addEventListener('scroll', function() {
  const scrollTopButton = document.getElementById('scrollTopButton');
  if (window.scrollY > 500) {
    scrollTopButton.style.visibility = "visible";
  } else {
    scrollTopButton.style.visibility = "hidden";
  }
});

// DOM 로드 후 이벤트 설정
document.addEventListener('DOMContentLoaded', () => {
  // 버튼 클릭 이벤트 추가
  document.getElementById('scrollTopButton').onclick = () => scrollToSection('top');
  document.getElementById('scrollToCommentsButton').onclick = () => scrollToSection('comments');
  document.getElementById('scrollToTripButton').onclick = () => scrollToSection('trip');
});
