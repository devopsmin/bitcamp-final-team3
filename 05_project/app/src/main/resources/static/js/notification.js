document.addEventListener('DOMContentLoaded', () => {
  const notificationDot = document.getElementById('notification-dot');

  function checkNewNotifications() {
    fetch('/notifications/check')
    .then(response => response.json())
    .then(hasUnread => {
      notificationDot.style.display = hasUnread ? 'inline' : 'none';
    })
    .catch(error => console.error('Error checking notifications:', error));
  }

  // 페이지 로드 후 1분마다 알림 확인
  setInterval(checkNewNotifications, 60000);
  checkNewNotifications();
});

document.addEventListener('DOMContentLoaded', () => {
  const bellIcon = document.getElementById('bell-icon');
  const notificationList = document.getElementById('notification-list');

  bellIcon.addEventListener('click', () => {
    fetch('/notifications/unread')
    .then(response => response.json())
    .then(notifications => {
      notificationList.innerHTML = '';
      if (notifications.length > 0) {
        notifications.forEach(notification => {
          const listItem = document.createElement('li');
          listItem.className = 'list-group-item';
          listItem.innerHTML = `
                            <a href="${notification.notiLink}" class="text-decoration-none">
                                ${notification.notiMessage}
                            </a>
                        `;
          notificationList.appendChild(listItem);
        });
      } else {
        notificationList.innerHTML = '<li class="list-group-item">새로운 알림이 없습니다.</li>';
      }
    })
    .catch(error => console.error('Error fetching notifications:', error));
  });
});

document.addEventListener('DOMContentLoaded', () => {
  const bellIcon = document.getElementById('bell-icon');
  const notificationList = document.getElementById('notification-list');
  const notificationModal = new bootstrap.Modal(document.getElementById('notificationModal'));

  bellIcon.addEventListener('click', () => {
    fetch('/notifications/unread') // userNo는 로그인된 유저 ID로 대체
    .then(response => response.json())
    .then(notifications => {
      notificationList.innerHTML = ''; // 기존 알림 초기화
      if (notifications.length > 0) {
        notifications.forEach(notification => {
          const listItem = document.createElement('li');
          listItem.className = 'list-group-item';
          listItem.innerHTML = `
                            <a href="${notification.notiLink}" class="text-decoration-none notification-link" 
                               data-id="${notification.notificationNo}">
                                ${notification.notiMessage}
                            </a>
                        `;
          notificationList.appendChild(listItem);
        });

        // 알림 읽음 처리
        setupReadNotificationListeners();
      } else {
        notificationList.innerHTML = '<li class="list-group-item">새로운 알림이 없습니다.</li>';
      }
    })
    .catch(error => console.error('Error fetching notifications:', error));

    notificationModal.show(); // 모달 열기
  });

  // 알림 읽음 처리 이벤트 바인딩
  function setupReadNotificationListeners() {
    const notificationLinks = document.querySelectorAll('.notification-link');
    notificationLinks.forEach(link => {
      link.addEventListener('click', (event) => {
        const notificationId = link.getAttribute('data-id');

        // 읽음 처리 API 호출
        fetch(`/notifications/${notificationId}/read`, { method: 'POST' })
        .then(() => {
          // 클릭된 알림을 모달에서 제거 (선택 사항)
          link.closest('li').remove();

          // 레드닷 갱신
          checkNewNotifications();
        })
        .catch(error => console.error('Error marking notification as read:', error));
      });
    });
  }

  // // 레드닷 갱신 (이전 단계에서 작성한 코드 재사용)
  // function checkNewNotifications() {
  //   fetch('/notifications/check')
  //   .then(response => response.json())
  //   .then(hasUnread => {
  //     const notificationDot = document.getElementById('notification-dot');
  //     notificationDot.style.display = hasUnread ? 'inline' : 'none';
  //   })
  //   .catch(error => console.error('Error checking notifications:', error));
  // }

  function checkNewNotifications() {
    fetch('/notifications/check')
    .then(response => {
      if (!response.ok) {
        console.error(`Error fetching /notifications/check: ${response.status} ${response.statusText}`);
        throw new Error(`HTTP error: ${response.status}`);
      }
      return response.json();
    })
    .then(hasUnread => {
      const notificationDot = document.getElementById('notification-dot');
      notificationDot.style.display = hasUnread ? 'inline' : 'none';
    })
    .catch(error => console.error('Error checking notifications:', error));
  }

});
