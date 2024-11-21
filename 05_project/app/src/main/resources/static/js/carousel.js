document.addEventListener('DOMContentLoaded', function() {
  // 캐러셀 요소 가져오기
  const tripCarousel = document.querySelector('#tripCarousel');

  if (tripCarousel) {
    // 캐러셀 초기화
    const carousel = new bootstrap.Carousel(tripCarousel, {
      interval: 3000, // 3초마다 자동 슬라이드
      ride: 'carousel',
      wrap: true, // 순환 허용
      touch: true // 터치/스와이프 허용
    });

    // 마우스 오버시 자동 재생 일시 정지
    tripCarousel.addEventListener('mouseenter', () => {
      carousel.pause();
    });

    // 마우스 아웃시 자동 재생 재개
    tripCarousel.addEventListener('mouseleave', () => {
      carousel.cycle();
    });

    // 키보드 방향키로 제어
    document.addEventListener('keydown', (e) => {
      if (e.key === 'ArrowLeft') {
        carousel.prev();
      } else if (e.key === 'ArrowRight') {
        carousel.next();
      }
    });

    // 슬라이드 이벤트 리스너
    tripCarousel.addEventListener('slide.bs.carousel', event => {
      // 슬라이드 전환 시작시 실행할 코드
      const { direction, from, to } = event;
      console.log(`Sliding ${direction} from slide ${from} to ${to}`);
    });

    tripCarousel.addEventListener('slid.bs.carousel', event => {
      // 슬라이드 전환 완료 후 실행할 코드
      const { direction, from, to } = event;
      console.log(`Completed sliding ${direction} from slide ${from} to ${to}`);
    });
  }
});
