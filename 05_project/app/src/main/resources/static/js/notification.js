document.addEventListener('DOMContentLoaded', () => {
  const notificationDot = document.getElementById('notification-dot');
  const bellIcon = document.getElementById('bell-icon');
  const notificationList = document.getElementById('notification-list');

  // 새로운 알림 확인 (숫자 포함)
  function checkNewNotifications() {
    fetch('/notifications/check')
    .then(response => response.json())
    .then(notificationCount => {
      if (notificationCount > 0) {
        notificationDot.style.display = 'inline';
        notificationDot.textContent = notificationCount > 99 ? '99+' : notificationCount; // 99개 이상이면 "99+"
      } else {
        notificationDot.style.display = 'none';
      }
    })
    .catch(error => console.error('Error checking notifications:', error));
  }

  // 알림 리스트 가져오기
  bellIcon.addEventListener('click', () => {
    fetch('/notifications/unread')
    .then(response => response.json())
    .then(notifications => {
      notificationList.innerHTML = ''; // 기존 알림 초기화

      if (notifications.length > 0) {
        notifications.forEach(notification => {
          const listItem = document.createElement('li');
          listItem.className = 'dropdown-item';
          listItem.innerHTML = `
              <a href="${notification.notiLink}" class="text-decoration-none notification-link" data-id="${notification.notificationNo}">
                ${notification.notiMessage}
              </a>`;
          notificationList.appendChild(listItem);
        });

        setupReadNotificationListeners();
      } else {
        notificationList.innerHTML = '<li class="dropdown-item text-muted">새로운 알림이 없습니다.</li>';
      }
    })
    .catch(error => console.error('Error fetching notifications:', error));
  });

  // 알림 읽음 처리
  function setupReadNotificationListeners() {
    const notificationLinks = document.querySelectorAll('.notification-link');
    notificationLinks.forEach(link => {
      link.addEventListener('click', event => {
        const notificationId = link.getAttribute('data-id');
        fetch(`/notifications/${notificationId}/read`, { method: 'POST' })
        .then(() => {
          // 클릭된 알림을 제거 (선택 사항)
          link.closest('li').remove();

          // 레드닷 갱신
          checkNewNotifications();
        })
        .catch(error => console.error('Error marking notification as read:', error));
      });
    });
  }

  // 초기화 및 주기적 확인
  setInterval(checkNewNotifications, 60000); // 1분마다 확인
  checkNewNotifications();
});
