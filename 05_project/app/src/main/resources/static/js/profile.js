let profilePhoneVerified  = false;
let profilephoneVerificationTimer;

document.addEventListener('DOMContentLoaded', function() {
    // 전화번호 변경 버튼 이벤트 리스너
    const changePhoneBtn = document.getElementById('changePhoneBtn');
    if (changePhoneBtn) {
        changePhoneBtn.addEventListener('click', function() {
            const modal = new bootstrap.Modal(document.getElementById('changePhoneModal'));
            modal.show();
        });
    }

    const nicknameInput = document.getElementById('userNickname');
        if (nicknameInput) {
            let originalNickname = nicknameInput.value;
            let typingTimer;

            nicknameInput.addEventListener('input', function() {
                clearTimeout(typingTimer);

                typingTimer = setTimeout(async function() {
                    const newNickname = nicknameInput.value.trim();

                    if (newNickname === originalNickname) {
                        nicknameInput.classList.remove('is-invalid');
                        return;
                    }

                    if (newNickname.length < 2) {
                        nicknameInput.classList.add('is-invalid');
                        nicknameInput.nextElementSibling.textContent =
                            '닉네임은 2자 이상이어야 합니다.';
                        return;
                    }

                    try {
                        const response = await fetch('/verify/check-nickname', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/x-www-form-urlencoded',
                            },
                            body: `nickname=${encodeURIComponent(newNickname)}`
                        });

                        const result = await response.text();

                        if (result === 'duplicate') {
                            nicknameInput.classList.add('is-invalid');
                            nicknameInput.nextElementSibling.textContent =
                                '이미 사용중인 닉네임입니다.';
                        } else if (result === 'available') {
                            nicknameInput.classList.remove('is-invalid');
                        } else {
                            nicknameInput.classList.add('is-invalid');
                            nicknameInput.nextElementSibling.textContent =
                                '닉네임 중복 확인 중 오류가 발생했습니다.';
                        }
                    } catch (error) {
                        console.error('Error:', error);
                        nicknameInput.classList.add('is-invalid');
                        nicknameInput.nextElementSibling.textContent =
                            '서버 오류가 발생했습니다.';
                    }
                }, 500);
            });
        }

   // 비밀번호 변경 폼 이벤트 리스너
   const passwordChangeForm = document.getElementById('passwordChangeForm');
       if (passwordChangeForm) {
           passwordChangeForm.addEventListener('submit', async function(e) {
               e.preventDefault();
           const currentPassword = document.getElementById('currentPasswordModal').value;
           const newPassword = document.getElementById('newPassword').value;
           const confirmNewPassword = document.getElementById('confirmNewPassword').value;

           if (newPassword !== confirmNewPassword) {
               alert('새 비밀번호가 일치하지 않습니다.');
               return;
           }

           try {
               const response = await fetch('/user/change-password', {
                   method: 'POST',
                   headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                   body: `currentPassword=${encodeURIComponent(currentPassword)}&newPassword=${encodeURIComponent(newPassword)}`
               });
               const result = await response.text();
               alert(result);
               if (result === '비밀번호가 성공적으로 변경되었습니다.') {
                   bootstrap.Modal.getInstance(document.getElementById('passwordChangeModal')).hide();
                   passwordChangeForm.reset();
               }
           } catch (error) {
               alert('비밀번호 변경 중 오류가 발생했습니다.');
           }
       });
   }

   // 전화번호 입력 이벤트 리스너
   const telInput = document.querySelector('input[name="userTel"]');
   if (telInput) {
       telInput.addEventListener('input', function(e) {
           let value = e.target.value.replace(/[^\d]/g, '');
           if (value.length > 3 && value.length <= 7) {
               value = value.slice(0, 3) + '-' + value.slice(3);
           } else if (value.length > 7) {
               value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
           }
           e.target.value = value;
       });
   }

   // SMS 인증번호 전송 버튼 이벤트 리스너
    const sendSmsBtn = document.getElementById('sendSmsBtn');
    if (sendSmsBtn) {
        sendSmsBtn.addEventListener('click', sendChangePhoneVerification);
    }

   // SMS 인증번호 확인 버튼 이벤트 리스너
    const verifyCodeBtn = document.getElementById('verifyCodeBtn');
    if (verifyCodeBtn) {
        verifyCodeBtn.addEventListener('click', verifyChangePhoneCode);
    }

   // 전화번호 변경 확인 버튼 이벤트 리스너
    const updatePhoneBtn = document.getElementById('updatePhoneBtn');
    if (updatePhoneBtn) {
        updatePhoneBtn.addEventListener('click', updatePhone);
    }

   // 새 전화번호 입력 필드 포맷팅
    const newPhoneNumber = document.getElementById('newPhoneNumber');
    if (newPhoneNumber) {
        newPhoneNumber.addEventListener('input', formatPhoneNumber);
    }
});

function validateForm() {
    const currentPasswordInput = document.querySelector('input[name="currentPassword"]');
    if (currentPasswordInput && !currentPasswordInput.value.trim()) {
        alert("현재 비밀번호를 입력해주세요.");
        currentPasswordInput.focus();
        return false;
    }

    const nicknameInput = document.getElementById('userNickname');
    if (nicknameInput && nicknameInput.classList.contains('is-invalid')) {
        alert('사용할 수 없는 닉네임입니다.');
        nicknameInput.focus();
        return false;
    }

    const telInput = document.querySelector('input[name="userTel"]');
    const telPattern = /^\d{3}-\d{4}-\d{4}$/;
    if (!telPattern.test(telInput.value.trim())) {
        alert("올바른 전화번호 형식을 입력해주세요. (예: 010-1234-5678)");
        telInput.focus();
        return false;
    }
    return true;
}

function openPasswordModal() {
   const modal = new bootstrap.Modal(document.getElementById('passwordChangeModal'));
   modal.show();
}

async function deleteAccount() {
   if (!confirm("정말 탈퇴하시겠습니까?")) return;

   try {
       const response = await fetch('/user/profile/delete', {
           method: 'POST',
           headers: { 'Content-Type': 'application/json' }
       });
       const result = await response.text();
       alert(result);
       if (result === "회원 탈퇴가 완료되었습니다.") window.location.href = "/";
   } catch (error) {
       alert("회원 탈퇴 처리 중 오류가 발생했습니다.");
   }
}

function openChangePhoneModal() {
   const modal = new bootstrap.Modal(document.getElementById('changePhoneModal'));
   modal.show();
}

function startVerificationTimer() {
   let timeLeft = 180; // 3분
   const timerDisplay = document.getElementById('smsTimer');

   if (profilephoneVerificationTimer) {
       clearInterval(profilephoneVerificationTimer);
   }

   profilephoneVerificationTimer = setInterval(() => {
       const minutes = Math.floor(timeLeft / 60);
       const seconds = timeLeft % 60;
       timerDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

       if (timeLeft <= 0) {
           clearInterval(profilephoneVerificationTimer);
           timerDisplay.textContent = '인증 시간이 만료되었습니다. 다시 시도해주세요.';
           document.getElementById('sendSmsBtn').disabled = false;
       }
       timeLeft--;
   }, 1000);
}

function formatPhoneNumber(event) {
   let value = event.target.value.replace(/[^\d]/g, '');
   if (value.length > 3 && value.length <= 7) {
       value = value.slice(0, 3) + '-' + value.slice(3);
   } else if (value.length > 7) {
       value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
   }
   event.target.value = value;
}

async function sendChangePhoneVerification() {
   const phoneInput = document.getElementById('newPhoneNumber');
   const phoneNumber = phoneInput.value;
   const sendSmsBtn = document.getElementById('sendSmsBtn');

   if (!phoneNumber.match(/^[0-9]{3}-[0-9]{4}-[0-9]{4}$/)) {
       alert('올바른 전화번호 형식이 아닙니다.');
       return;
   }

   try {
       sendSmsBtn.disabled = true;
       sendSmsBtn.textContent = "전송중...";

       const response = await fetch('/verify/send-sms', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/x-www-form-urlencoded',
           },
           body: `phoneNumber=${encodeURIComponent(phoneNumber)}`
       });

       const result = await response.text();
       if (result === "SMS 전송에 실패했습니다.") {
           alert(result);
           return;
       }

       document.getElementById('smsVerificationDiv').style.display = 'block';
       startVerificationTimer();
       alert('인증번호가 전송되었습니다.');

   } catch (error) {
       console.error('Error:', error);
       alert('인증번호 전송 중 오류가 발생했습니다.');
   } finally {
       sendSmsBtn.disabled = false;
       sendSmsBtn.textContent = "인증번호 전송";
   }
}

async function verifyChangePhoneCode() {
   const phoneNumber = document.getElementById('newPhoneNumber').value;
   const code = document.getElementById('smsVerificationCode').value;

   if (!code) {
       alert('인증번호를 입력해주세요.');
       return;
   }

   try {
       const response = await fetch('/verify/verify-sms', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/x-www-form-urlencoded',
           },
           body: `phoneNumber=${encodeURIComponent(phoneNumber)}&code=${encodeURIComponent(code)}`
       });

       const result = await response.text();
       if (result === 'success') {
           alert('전화번호 인증이 완료되었습니다.');
           profilePhoneVerified  = true;
           document.getElementById('smsVerificationDiv').style.display = 'none';
           clearInterval(profilephoneVerificationTimer);
           document.getElementById('sendSmsBtn').disabled = true;
           document.getElementById('updatePhoneBtn').disabled = false;
       } else {
           alert('인증번호가 일치하지 않습니다.');
       }
   } catch (error) {
       console.error('Error:', error);
       alert('인증번호 확인 중 오류가 발생했습니다.');
   }
}

async function updatePhone() {
   if (!profilePhoneVerified) {
       alert('전화번호 인증이 필요합니다.');
       return;
   }

   const newPhoneNumber = document.getElementById('newPhoneNumber').value;

   try {
       document.querySelector('input[name="userTel"]').value = newPhoneNumber;
       bootstrap.Modal.getInstance(document.getElementById('changePhoneModal')).hide();
       alert('전화번호가 변경되었습니다. 회원정보 수정을 완료하려면 저장 버튼을 클릭해주세요.');
   } catch (error) {
       console.error('Error:', error);
       alert('전화번호 변경 중 오류가 발생했습니다.');
   }
}

function previewImage(input) {
    if (input.files && input.files[0]) {
        const reader = new FileReader();

        reader.onload = function(e) {
            const profileImage = document.querySelector('.rounded-circle');
            profileImage.src = e.target.result;
        };

        const fileSize = input.files[0].size / 1024 / 1024;
        if (fileSize > 10) { // 10MB 제한
            alert('파일 크기는 10MB 이하여야 합니다.');
            input.value = '';
            return;
        }

        const fileType = input.files[0].type;
        if (!fileType.startsWith('image/')) {
            alert('이미지 파일만 선택할 수 있습니다.');
            input.value = '';
            return;
        }

        reader.readAsDataURL(input.files[0]);
    }
}

function resetForm() {
    if (formChanged()) {
        if (!confirm('변경사항이 모두 취소됩니다. 계속하시겠습니까?')) {
            return;
        }
    }

    const form = document.querySelector('form');
    form.reset();

    const originalImageSrc = document.querySelector('.rounded-circle').getAttribute('data-original-src');
    document.querySelector('.rounded-circle').src = originalImageSrc;
}

document.addEventListener('DOMContentLoaded', function() {
    const phoneModal = document.getElementById('changePhoneModal');
    phoneModal.addEventListener('hidden.bs.modal', function () {
        document.getElementById('newPhoneNumber').value = '';
        document.getElementById('smsVerificationCode').value = '';
        document.getElementById('smsVerificationDiv').style.display = 'none';
        document.getElementById('smsTimer').textContent = '';
        document.getElementById('updatePhoneBtn').disabled = true;
        document.getElementById('sendSmsBtn').disabled = false;
        profilePhoneVerified = false;
        if (profilephoneVerificationTimer) {
            clearInterval(profilephoneVerificationTimer);
        }
    });

    const passwordModal = document.getElementById('passwordChangeModal');
    passwordModal.addEventListener('hidden.bs.modal', function () {
        document.getElementById('currentPasswordModal').value = '';
        document.getElementById('newPassword').value = '';
        document.getElementById('confirmNewPassword').value = '';
    });
});
