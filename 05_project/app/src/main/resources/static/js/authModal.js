let serverVerificationCode = '';
let isEmailVerified = false;
let isNicknameAvailable = false;
let isPhoneVerified = false;
let phoneVerificationTimer;
let findIdTimer = null;
let resetPasswordTimer = null;


document.addEventListener('DOMContentLoaded', function() {

    if (typeof bootstrap === 'undefined') {
        console.error('Bootstrap is not loaded');
        return;
    }

    createModals();

    const loginModalEl = document.getElementById('loginModal');
    const termsModalEl = document.getElementById('termsModal');
    const registerModalEl = document.getElementById('registerModal');
    const findIdModalEl = document.getElementById('findIdModal');
    const resetPasswordModalEl = document.getElementById('resetPasswordModal');

const modals = {};

    if (loginModalEl) {
        modals.login = new bootstrap.Modal(loginModalEl, {
            backdrop: 'static',  // 배경 클릭으로 닫히지 않도록 설정
            keyboard: false     // ESC 키로 닫히지 않도록 설정
        });
    }
    if (termsModalEl) {
        modals.terms = new bootstrap.Modal(termsModalEl, {
            backdrop: 'static',
            keyboard: false
        });
    }
    if (registerModalEl) {
        modals.register = new bootstrap.Modal(registerModalEl, {
            backdrop: 'static',
            keyboard: false
        });
    }
    if (findIdModalEl) {
        modals.findId = new bootstrap.Modal(findIdModalEl, {
            backdrop: 'static',
            keyboard: false
        });
    }
    if (resetPasswordModalEl) {
        modals.resetPassword = new bootstrap.Modal(resetPasswordModalEl, {
            backdrop: 'static',
            keyboard: false
        });
    }

  window.modals = modals;

  initializeModals();
});

function createModals() {
  // 로그인 모달
  const loginModal = `
      <div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="loginModalLabel">로그인</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body" id="loginModalContent">
                  </div>
              </div>
          </div>
      </div>
  `;

  // 약관 동의 모달
  const termsModal = `
      <div class="modal fade" id="termsModal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="termsModalLabel">약관 동의</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                      <h6 class="terms-title">모두의 여행 약관</h6>
                      <form id="termsForm">
                          <div class="mb-4">
                              <div class="form-check">
                                  <input type="checkbox" id="allAgree" class="form-check-input">
                                  <label class="form-check-label" for="allAgree">
                                      전체 동의
                                  </label>
                              </div>
                          </div>
                          <hr>
                          <div class="mb-3">
                              <div class="form-check d-flex justify-content-between align-items-center">
                                  <div>
                                      <input type="checkbox" id="term1" class="form-check-input terms-checkbox" required>
                                      <label class="form-check-label" for="term1">
                                          (필수) 개인정보 수집 및 이용 동의
                                      </label>
                                  </div>
                                  <button type="button" class="btn btn-link view-terms" data-terms-type="privacy">보기</button>
                              </div>
                          </div>
                          <div class="mb-3">
                              <div class="form-check d-flex justify-content-between align-items-center">
                                  <div>
                                      <input type="checkbox" id="term2" class="form-check-input terms-checkbox" required>
                                      <label class="form-check-label" for="term2">
                                          (필수) 서비스 이용약관 동의
                                      </label>
                                  </div>
                                  <button type="button" class="btn btn-link view-terms" data-terms-type="service">보기</button>
                              </div>
                          </div>
                          <div class="mb-4">
                              <div class="form-check d-flex justify-content-between align-items-center">
                                  <div>
                                      <input type="checkbox" id="term3" class="form-check-input terms-checkbox">
                                      <label class="form-check-label" for="term3">
                                          (선택) 마케팅 정보 수신 동의
                                      </label>
                                  </div>
                                  <button type="button" class="btn btn-link view-terms" data-terms-type="marketing">보기</button>
                              </div>
                          </div>
                          <button type="submit" class="btn btn-primary w-100" id="termsSubmitBtn" disabled>다음</button>
                      </form>
                  </div>
              </div>
          </div>
      </div>
  `;

  // 회원가입 모달
  const registerModal = `
      <div class="modal fade" id="registerModal" tabindex="-1" aria-labelledby="registerModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="registerModalLabel">회원가입</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body" id="registerModalContent">
                  </div>
              </div>
          </div>
      </div>
  `;

  // 아이디 찾기 모달
  const findIdModal = `
      <div class="modal fade" id="findIdModal" tabindex="-1" aria-labelledby="findIdModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="findIdModalLabel">아이디 찾기</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                      <form id="findIdForm">
                          <div class="mb-3">
                              <div class="input-group">
                                  <input type="tel" class="form-control" id="findIdPhone"
                                         placeholder="전화번호를 입력하세요" required
                                         pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}">
                                  <button type="button" class="btn btn-primary" onclick="sendFindIdVerification()">
                                      인증번호 전송
                                  </button>
                              </div>
                          </div>
                          <div class="mb-3" id="findIdVerificationDiv" style="display: none;">
                              <div class="input-group">
                                  <input type="text" class="form-control" id="findIdVerificationCode"
                                         placeholder="인증번호를 입력하세요" maxlength="6">
                                  <button type="button" class="btn btn-primary" onclick="verifyFindIdCode()">
                                      확인
                                  </button>
                              </div>
                              <div id="findIdTimer" class="text-danger mt-1"></div>
                          </div>
                          <button type="submit" class="btn btn-primary w-100" disabled>아이디 찾기</button>
                      </form>
                  </div>
              </div>
          </div>
      </div>
  `;

  // ID 찾기 결과 모달
  const findIdResultModal = `
      <div class="modal fade" id="findIdResultModal" tabindex="-1" aria-labelledby="findIdResultModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="findIdResultModalLabel">아이디 찾기 결과</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body text-center">
                      <p id="findIdResult"></p>
                      <button type="button" class="btn btn-primary mt-3" id="goToLogin">로그인하기</button>
                  </div>
              </div>
          </div>
      </div>
  `;


  // 비밀번호 재설정 모달
  const resetPasswordModal = `
      <div class="modal fade" id="resetPasswordModal" tabindex="-1" aria-labelledby="resetPasswordModalLabel" aria-hidden="true">
          <div class="modal-dialog modal-dialog-centered">
              <div class="modal-content">
                  <div class="modal-header">
                      <h5 class="modal-title" id="resetPasswordModalLabel">비밀번호 재설정</h5>
                      <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                      <form id="resetPasswordForm">
                          <div class="mb-3">
                              <div class="input-group">
                                  <input type="email" class="form-control" id="resetPasswordEmail"
                                         placeholder="이메일을 입력하세요" required>
                              </div>
                          </div>
                          <div class="mb-3">
                              <div class="input-group">
                                  <input type="tel" class="form-control" id="resetPasswordPhone"
                                         placeholder="전화번호를 입력하세요" required
                                         pattern="[0-9]{3}-[0-9]{4}-[0-9]{4}">
                                  <button type="button" class="btn btn-primary" onclick="sendResetPasswordVerification()">
                                      인증번호 전송
                                  </button>
                              </div>
                          </div>
                          <div class="mb-3" id="resetPasswordVerificationDiv" style="display: none;">
                              <div class="input-group">
                                  <input type="text" class="form-control" id="resetPasswordVerificationCode"
                                         placeholder="인증번호를 입력하세요" maxlength="6">
                                  <button type="button" class="btn btn-primary" onclick="verifyResetPasswordCode()">
                                      확인
                                  </button>
                              </div>
                              <div id="resetPasswordTimer" class="text-danger mt-1"></div>
                          </div>
                          <div id="newPasswordDiv" style="display: none;">
                              <div class="mb-3">
                                  <input type="password" class="form-control" id="newPassword"
                                         placeholder="새 비밀번호" required>
                                  <div class="form-check mt-2">
                                      <input type="checkbox" class="form-check-input" id="showNewPassword"
                                             onclick="toggleNewPasswordVisibility()">
                                      <label class="form-check-label" for="showNewPassword">
                                          비밀번호 표시
                                      </label>
                                  </div>
                              </div>
                              <div class="mb-3">
                                  <input type="password" class="form-control" id="confirmNewPassword"
                                         placeholder="새 비밀번호 확인" required>
                                  <div class="form-check mt-2">
                                      <input type="checkbox" class="form-check-input" id="showConfirmNewPassword"
                                             onclick="toggleConfirmNewPasswordVisibility()">
                                      <label class="form-check-label" for="showConfirmNewPassword">
                                          비밀번호 표시
                                      </label>
                                  </div>
                              </div>
                          </div>
                          <button type="submit" class="btn btn-primary w-100" disabled>
                              비밀번호 재설정
                          </button>
                      </form>
                  </div>
              </div>
          </div>
      </div>
  `;

   // 개인정보 수집 약관 모달
   const privacyTermsModal = `
       <div class="modal fade" id="privacyTermsModal" tabindex="-1" aria-labelledby="privacyTermsModalLabel" aria-hidden="true">
           <div class="modal-dialog modal-dialog-centered">
               <div class="modal-content">
                   <div class="modal-header">
                       <h5 class="modal-title" id="privacyTermsModalLabel">개인정보 수집 및 이용 동의서</h5>
                       <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                   </div>
                   <div class="modal-body">
                       <div class="terms-content">
                           <h6><strong>1. 수집 항목 및 목적</strong></h6>
                           <p>본 서비스는 원활한 회원가입 및 서비스 제공을 위해 아래와 같은 최소한의 개인정보를 수집합니다.</p>
                           <ul>
                               <li><strong>이메일:</strong> 계정 식별 및 회원 공지사항 발송</li>
                               <li><strong>비밀번호:</strong> 계정 보호를 위한 암호화된 정보</li>
                               <li><strong>연락처 (전화번호):</strong> 본인 확인 및 중요 공지 발송</li>
                               <li><strong>닉네임:</strong> 서비스 내 개인 식별 및 소통을 위한 이름</li>
                           </ul>

                           <h6><strong>2. 이용 목적</strong></h6>
                           <p>수집된 개인정보는 회원 식별, 서비스 이용 기록 확인, 계정 보안 관리 및 본인 확인 목적으로 활용되며, 법령에서 정한 경우를 제외하고는 목적 외로 사용되지 않습니다.</p>

                           <h6><strong>3. 보유 및 이용 기간</strong></h6>
                           <p>개인정보는 회원 탈퇴 시 지체 없이 파기되며, 법령에 따라 보관이 필요한 경우 별도 저장됩니다.</p>

                           <h6><strong>4. 동의 거부 권리 및 제한 사항</strong></h6>
                           <p>이용자는 개인정보 수집 및 이용에 대해 동의를 거부할 권리가 있습니다. 다만 필수 정보에 대한 동의를 거부할 경우 서비스 이용이 제한될 수 있습니다.</p>
                       </div>
                       <div class="text-center mt-3">
                           <button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   `;

   // 서비스 이용약관 모달
   const serviceTermsModal = `
       <div class="modal fade" id="serviceTermsModal" tabindex="-1" aria-labelledby="serviceTermsModalLabel" aria-hidden="true">
           <div class="modal-dialog modal-dialog-centered">
               <div class="modal-content">
                   <div class="modal-header">
                       <h5 class="modal-title" id="serviceTermsModalLabel">서비스 이용약관 동의서</h5>
                       <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                   </div>
                   <div class="modal-body">
                       <div class="terms-content">
                           <h6><strong>1. 목적</strong></h6>
                           <p>본 약관은 회원이 제공하는 서비스의 이용과 관련한 기본적인 사항을 규정하며, 회원과 서비스 제공자 간의 권리와 의무를 정의합니다.</p>

                           <h6><strong>2. 회원의 의무</strong></h6>
                           <p>회원은 본 약관과 관련 법령을 준수하여야 하며, 다음 행위를 하지 않아야 합니다.</p>
                           <ul>
                               <li>타인의 개인정보 도용</li>
                               <li>서비스의 불법적 사용</li>
                               <li>다른 이용자의 권리 침해</li>
                           </ul>

                           <h6><strong>3. 서비스 제공자의 권리와 의무</strong></h6>
                           <p>서비스 제공자는 회원이 안전하게 서비스를 이용할 수 있도록 최선을 다하며, 불법 행위 발생 시 회원에게 사전 경고 후 이용을 제한할 수 있습니다.</p>

                           <h6><strong>4. 서비스 이용 제한</strong></h6>
                           <p>회원의 부정 행위나 약관 위반 시, 서비스 제공자는 서비스 이용을 중지하거나 제한할 수 있으며, 이로 인해 발생한 불이익에 대해서는 책임을 지지 않습니다.</p>
                       </div>
                       <div class="text-center mt-3">
                           <button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   `;

   // 마케팅 정보 수신 동의 모달
   const marketingTermsModal = `
       <div class="modal fade" id="marketingTermsModal" tabindex="-1" aria-labelledby="marketingTermsModalLabel" aria-hidden="true">
           <div class="modal-dialog modal-dialog-centered">
               <div class="modal-content">
                   <div class="modal-header">
                       <h5 class="modal-title" id="marketingTermsModalLabel">마케팅 정보 수신 동의서 (선택)</h5>
                       <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                   </div>
                   <div class="modal-body">
                       <div class="terms-content">
                           <h6><strong>1. 목적</strong></h6>
                           <p>회사는 다양한 혜택, 이벤트 정보, 맞춤형 서비스 제공을 위해 마케팅 정보 수신에 동의를 요청합니다. 해당 정보는 이메일, 문자 메시지, 푸시 알림 등을 통해 제공될 수 있습니다.</p>

                           <h6><strong>2. 수집 항목</strong></h6>
                           <ul>
                               <li><strong>이메일:</strong> 이벤트 및 맞춤형 서비스 알림 제공</li>
                               <li><strong>연락처 (전화번호):</strong> 문자 메시지를 통한 프로모션 알림</li>
                           </ul>

                           <h6><strong>3. 이용 목적</strong></h6>
                           <p>동의 시, 다음과 같은 정보를 수신하게 됩니다.</p>
                           <ul>
                               <li>신규 서비스 출시 안내 및 프로모션 정보</li>
                               <li>할인 혜택 및 이벤트 초대</li>
                               <li>맞춤형 추천 콘텐츠 및 광고</li>
                           </ul>

                           <h6><strong>4. 수신 동의 철회</strong></h6>
                           <p>이용자는 언제든지 수신 동의를 철회할 수 있습니다. 동의 철회는 설정 메뉴 또는 고객센터를 통해 요청하실 수 있으며, 철회 후에는 마케팅 정보가 더 이상 제공되지 않습니다.</p>
                       </div>
                       <div class="text-center mt-3">
                           <button type="button" class="btn btn-primary" data-bs-dismiss="modal">확인</button>
                       </div>
                   </div>
               </div>
           </div>
       </div>
   `;

   // body에 모달 추가
   document.body.insertAdjacentHTML('beforeend', loginModal);
   document.body.insertAdjacentHTML('beforeend', termsModal);
   document.body.insertAdjacentHTML('beforeend', registerModal);
   document.body.insertAdjacentHTML('beforeend', findIdModal);
   document.body.insertAdjacentHTML('beforeend', findIdResultModal);
   document.body.insertAdjacentHTML('beforeend', resetPasswordModal);
   document.body.insertAdjacentHTML('beforeend', privacyTermsModal);
   document.body.insertAdjacentHTML('beforeend', serviceTermsModal);
   document.body.insertAdjacentHTML('beforeend', marketingTermsModal);;
   }

  function initializeModals() {
      const loginModalEl = document.getElementById('loginModal');
      const termsModalEl = document.getElementById('termsModal');
      const registerModalEl = document.getElementById('registerModal');
      const findIdModalEl = document.getElementById('findIdModal');
      const resetPasswordModalEl = document.getElementById('resetPasswordModal');

          // 아이디 찾기 모달 초기화
          if (findIdModalEl) {
              const findIdForm = document.getElementById('findIdForm');
              const findIdPhone = document.getElementById('findIdPhone');

              findIdPhone?.addEventListener('input', function(e) {
                  let value = e.target.value.replace(/[^\d]/g, '');
                  if (value.length > 3 && value.length <= 7) {
                      value = value.slice(0, 3) + '-' + value.slice(3);
                  } else if (value.length > 7) {
                      value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
                  }
                  e.target.value = value;
              });

              findIdForm?.addEventListener('submit', async function(e) {
                  e.preventDefault();
                  if (!isPhoneVerified) {
                      alert('전화번호 인증이 필요합니다.');
                      return;
                  }
                  const phone = findIdPhone.value;
                  try {
                      const response = await fetch('/auth/find/id', {
                          method: 'POST',
                          headers: {
                              'Content-Type': 'application/x-www-form-urlencoded',
                          },
                          body: `userTel=${encodeURIComponent(phone)}`
                      });
                      const result = await response.text();

                      // 기존 아이디 찾기 모달 닫기
                      const findIdModal = bootstrap.Modal.getInstance(findIdModalEl);
                      findIdModal.hide();

                      // 결과 모달 표시
                      const findIdResultModal = new bootstrap.Modal(document.getElementById('findIdResultModal'));
                      document.getElementById('findIdResult').textContent = result;
                      findIdResultModal.show();

                      // 로그인하기 버튼 이벤트 추가
                      document.getElementById('goToLogin').onclick = function() {
                          findIdResultModal.hide();
                          setTimeout(() => {
                              const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                              loginModal.show();
                          }, 500);
                      };
                  } catch (error) {
                      console.error('Error:', error);
                      alert('아이디 찾기 중 오류가 발생했습니다.');
                  }
              });
          }

      // 비밀번호 재설정 모달 초기화
          if (resetPasswordModalEl) {
              const resetPasswordForm = document.getElementById('resetPasswordForm');
              const resetPasswordPhone = document.getElementById('resetPasswordPhone');
              const resetPasswordEmail = document.getElementById('resetPasswordEmail');
              const newPasswordDiv = document.getElementById('newPasswordDiv');
              const newPassword = document.getElementById('newPassword');
              const confirmNewPassword = document.getElementById('confirmNewPassword');

              resetPasswordPhone?.addEventListener('input', function(e) {
                  let value = e.target.value.replace(/[^\d]/g, '');
                  if (value.length > 3 && value.length <= 7) {
                      value = value.slice(0, 3) + '-' + value.slice(3);
                  } else if (value.length > 7) {
                      value = value.slice(0, 3) + '-' + value.slice(3, 7) + '-' + value.slice(7, 11);
                  }
                  e.target.value = value;
              });

              resetPasswordForm?.addEventListener('submit', async function(e) {
                  e.preventDefault();
                  if (!isPhoneVerified) {
                      alert('전화번호 인증이 필요합니다.');
                      return;
                  }

                  if (newPassword.value !== confirmNewPassword.value) {
                      alert('새 비밀번호가 일치하지 않습니다.');
                      return;
                  }

                  try {
                      const response = await fetch('/auth/reset/password', {
                          method: 'POST',
                          headers: {
                              'Content-Type': 'application/x-www-form-urlencoded',
                          },
                          body: `userEmail=${encodeURIComponent(resetPasswordEmail.value)}&userTel=${encodeURIComponent(resetPasswordPhone.value)}&newPassword=${encodeURIComponent(newPassword.value)}`
                      });
                      const result = await response.text();
                      if (result === 'success') {
                          alert('비밀번호가 성공적으로 재설정되었습니다.');
                          const resetPasswordModal = bootstrap.Modal.getInstance(resetPasswordModalEl);
                          resetPasswordModal.hide();
                          setTimeout(() => {
                              const loginModal = new bootstrap.Modal(loginModalEl);
                              loginModal.show();
                          }, 500);
                      } else {
                          alert(result);
                      }
                  } catch (error) {
                      console.error('Error:', error);
                      alert('비밀번호 재설정 중 오류가 발생했습니다.');
                  }
              });
          }

        // 로그인 모달 초기화
        if (loginModalEl) {
            loginModalEl.addEventListener('show.bs.modal', function() {
                fetch('/auth/login/form')
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('loginModalContent').innerHTML = html;
                        initializeLoginForm();
                    })
                    .catch(error => {
                        console.error('Error loading login form:', error);
                        alert('로그인 폼을 불러오는데 실패했습니다.');
                    });
            });
        }

        // 약관 동의 모달 초기화
        if (termsModalEl) {
            const allAgree = document.getElementById('allAgree');
            const termsCheckboxes = document.getElementsByClassName('terms-checkbox');
            const termsSubmitBtn = document.getElementById('termsSubmitBtn');
            const termsForm = document.getElementById('termsForm');
            const viewTermsButtons = document.querySelectorAll('.view-terms');

            // "보기" 버튼 이벤트 리스너
            viewTermsButtons.forEach(button => {
                button.addEventListener('click', function() {
                    const termsType = this.dataset.termsType;
                    const termsModal = bootstrap.Modal.getInstance(termsModalEl);
                    termsModal.hide();

                    let targetModalId;
                    switch(termsType) {
                        case 'privacy':
                            targetModalId = 'privacyTermsModal';
                            break;
                        case 'service':
                            targetModalId = 'serviceTermsModal';
                            break;
                        case 'marketing':
                            targetModalId = 'marketingTermsModal';
                            break;
                    }

                    if (targetModalId) {
                        const targetModal = new bootstrap.Modal(document.getElementById(targetModalId));
                        targetModal.show();

                        document.getElementById(targetModalId).addEventListener('hidden.bs.modal', function() {
                            termsModal.show();
                        }, { once: true });
                    }
                });
            });

            // 전체 동의 체크박스 이벤트
            allAgree?.addEventListener('change', function() {
                Array.from(termsCheckboxes).forEach(checkbox => {
                    checkbox.checked = allAgree.checked;
                });
                updateTermsSubmitButton();
            });

            // 개별 약관 체크박스 이벤트
            Array.from(termsCheckboxes).forEach(checkbox => {
                checkbox?.addEventListener('change', function() {
                    updateAllAgreeCheckbox();
                    updateTermsSubmitButton();
                });
            });

            // 약관 동의 폼 제출 이벤트
            termsForm?.addEventListener('submit', function(e) {
                e.preventDefault();
                bootstrap.Modal.getInstance(termsModalEl).hide();
                const registerModal = new bootstrap.Modal(registerModalEl);
                registerModal.show();
            });
        }

        // 회원가입 모달 초기화
        if (registerModalEl) {
            registerModalEl.addEventListener('show.bs.modal', function() {
                fetch('/auth/register/user')
                    .then(response => response.text())
                    .then(html => {
                        document.getElementById('registerModalContent').innerHTML = html;
                        initializeRegisterForm();
                    })
                    .catch(error => {
                        console.error('Error loading register form:', error);
                        alert('회원가입 폼을 불러오는데 실패했습니다.');
                    });
            });
        }
    }

   function initializeRegisterForm() {
       const registerForm = document.querySelector('#registerModalContent form');
       if (registerForm) {
           // 비밀번호 입력 필드 이벤트 리스너
           const passwordInput = document.getElementById('password');
           const confirmPasswordInput = document.getElementById('confirmPassword');

           // 전화번호 입력 필드 이벤트 리스너
           const telInput = document.getElementById('userTel');
           if (telInput) {
               telInput.addEventListener('input', function(e) {
                   let value = e.target.value.replace(/[^\d-]/g, '');

                   if (value.length > 3 && !value.includes('-')) {
                       value = value.replace(/(\d{3})(\d+)/, '$1-$2');
                   }
                   if (value.length > 8) {
                       value = value.replace(/(\d{3})-(\d{4})(\d+)/, '$1-$2-$3');
                   }

                   if (value.length > 13) {
                       value = value.slice(0, 13);
                   }

                   e.target.value = value;

                   const isValid = /^[0-9]{3}-[0-9]{4}-[0-9]{4}$/.test(value);
                   if (isValid) {
                       telInput.style.borderColor = '#4DCFBD';
                       telInput.setCustomValidity('');
                   } else {
                       telInput.style.borderColor = '';
                       telInput.setCustomValidity('올바른 전화번호 형식이 아닙니다.');
                   }
               });
           }

           // 비밀번호 검증 메시지 요소 생성
           const messageDiv = document.createElement('div');
           messageDiv.style.color = 'red';
           messageDiv.style.fontSize = '12px';
           messageDiv.style.marginTop = '5px';
           messageDiv.className = 'password-error-message';

           const checkboxDiv = confirmPasswordInput.parentNode.querySelector('.form-check');
           confirmPasswordInput.parentNode.insertBefore(messageDiv, checkboxDiv);

           function validatePassword() {
               if (!confirmPasswordInput.value) {
                   messageDiv.textContent = '';
                   confirmPasswordInput.style.borderColor = '';
                   return;
               }

               if (passwordInput.value !== confirmPasswordInput.value) {
                   messageDiv.textContent = '비밀번호가 일치하지 않습니다.';
                   confirmPasswordInput.style.borderColor = 'red';
               } else {
                   messageDiv.textContent = '';
                   confirmPasswordInput.style.borderColor = '#4DCFBD';
               }
           }

           if (passwordInput && confirmPasswordInput) {
               confirmPasswordInput.addEventListener('input', validatePassword);
               passwordInput.addEventListener('input', function() {
                   if (confirmPasswordInput.value) {
                       validatePassword();
                   }
               });
           }

           // 폼 제출 이벤트
           registerForm.addEventListener('submit', function(e) {
               e.preventDefault();

               const password = document.getElementById('password').value;
               const confirmPassword = document.getElementById('confirmPassword').value;
               const telInput = document.getElementById('userTel');

               if (password !== confirmPassword) {
                   alert('비밀번호가 일치하지 않습니다.');
                   return;
               }

               if (!isEmailVerified || !isNicknameAvailable || !isPhoneVerified) {
                   alert('이메일 인증, 닉네임 중복 확인, 전화번호 인증이 모두 필요합니다.');
                   return;
               }

               if (telInput && !telInput.checkValidity()) {
                   alert('올바른 전화번호 형식으로 입력해주세요.');
                   return;
               }

               const formData = new FormData(this);

               fetch(this.action, {
                   method: 'POST',
                   body: formData
               })
               .then(response => {
                   if (response.ok) {
                       alert('회원가입이 완료되었습니다.');
                       const registerModal = bootstrap.Modal.getInstance(document.getElementById('registerModal'));
                       registerModal.hide();

                       setTimeout(() => {
                           const loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
                           loginModal.show();
                       }, 500);
                   } else {
                       return response.text().then(html => {
                           const errorMessage = html.match(/errorMessage">(.*?)</);
                           if (errorMessage && errorMessage[1]) {
                               alert(errorMessage[1]);
                           } else {
                               alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
                           }
                       });
                   }
               })
               .catch(error => {
                   console.error('Error submitting register form:', error);
                   alert('회원가입 중 오류가 발생했습니다. 다시 시도해주세요.');
               });
           });
       }
   }

function initializeLoginForm() {
    const loginForm = document.querySelector('#loginModalContent form');
    if (loginForm) {
        loginForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const formData = new FormData(this);

            fetch(this.action, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    window.location.reload();
                } else {
                    return response.text().then(html => {
                        document.getElementById('loginModalContent').innerHTML = html;
                        initializeLoginForm();
                    });
                }
            })
            .catch(error => {
                console.error('Error submitting login form:', error);
                alert('로그인 중 오류가 발생했습니다.');
            });
        });
    }
}

   // 일반 회원가입 sms 인증
  async function sendSMSVerification() {
      const phoneInput = document.getElementById('userTel');
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

          serverVerificationCode = result;
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

   // 일반 회원가입 sms 인증
   async function verifySMSCode() {
       const phoneNumber = document.getElementById('userTel').value;
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
               isPhoneVerified = true;
               document.getElementById('userTel').readOnly = true;
               document.getElementById('smsVerificationDiv').style.display = 'none';
               clearInterval(phoneVerificationTimer);
               document.getElementById('sendSmsBtn').disabled = true;
               checkAllVerifications();
           } else {
               alert('인증번호가 일치하지 않습니다.');
           }
       } catch (error) {
           console.error('Error:', error);
           alert('인증번호 확인 중 오류가 발생했습니다.');
       }
   }

   // 일반 회원가입 sms 인증
   function startVerificationTimer() {
       let timeLeft = 180; // 3분
       const timerDisplay = document.getElementById('smsTimer');

       if (phoneVerificationTimer) {
           clearInterval(phoneVerificationTimer);
       }

       phoneVerificationTimer = setInterval(() => {
           const minutes = Math.floor(timeLeft / 60);
           const seconds = timeLeft % 60;
           timerDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

           if (timeLeft <= 0) {
               clearInterval(phoneVerificationTimer);
               timerDisplay.textContent = '인증 시간이 만료되었습니다. 다시 시도해주세요.';
               document.getElementById('sendSmsBtn').disabled = false;
           }
           timeLeft--;
       }, 1000);
   }


   // 소셜 회원가입 sms 인증
   async function sendSocialSMSVerification() {
       const phoneInput = document.getElementById('userTel');
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
           startSocialVerificationTimer();  // 함수명 변경
           alert('인증번호가 전송되었습니다.');

       } catch (error) {
           console.error('Error:', error);
           alert('인증번호 전송 중 오류가 발생했습니다.');
       } finally {
           sendSmsBtn.disabled = false;
           sendSmsBtn.textContent = "인증번호 전송";
       }
   }

   // 소셜 회원가입 sms 인증
   async function sendVerificationEmail() {
       const emailInput = document.getElementById('registerUserEmail');
       const emailVerificationBtn = document.querySelector('#registerModalContent #emailVerificationBtn');

       if (!emailInput || !emailInput.value) {
           alert('이메일을 입력하세요.');
           return;
       }
       const email = emailInput.value;

       emailVerificationBtn.disabled = true;
       emailVerificationBtn.innerText = "전송 중";

       try {
           const response = await fetch('/verify/mail-confirm', {
               method: 'POST',
               headers: {
                   'Content-Type': 'application/x-www-form-urlencoded'
               },
               body: 'email=' + encodeURIComponent(email)
           });
           const result = await response.text();
           if (result === "이미 가입된 이메일입니다." || result === "이메일 전송에 실패했습니다.") {
               alert(result);
               return;
           }
           serverVerificationCode = result;
           alert('인증번호가 발송되었습니다.');
       } catch (error) {
           alert('인증번호 발송에 실패했습니다. 잠시 후 다시 시도하세요.');
           console.error('Error:', error);
       } finally {
           emailVerificationBtn.disabled = false;
           emailVerificationBtn.innerText = "확인";
       }
   }

   // 소셜 회원가입 sms 인증
   async function verifySocialSMSCode() {
       const phoneNumber = document.getElementById('userTel').value;
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
               isPhoneVerified = true;
               document.getElementById('userTel').readOnly = true;
               document.getElementById('smsVerificationDiv').style.display = 'none';
               clearInterval(phoneVerificationTimer);
               document.getElementById('sendSmsBtn').disabled = true;
               document.getElementById('submitBtn').disabled = false;
           } else {
               alert('인증번호가 일치하지 않습니다.');
           }
       } catch (error) {
           console.error('Error:', error);
           alert('인증번호 확인 중 오류가 발생했습니다.');
       }
   }

   // 소셜 회원가입 sms 인증
   function startSocialVerificationTimer() {
       let timeLeft = 180;
       const timerDisplay = document.getElementById('smsTimer');

       if (phoneVerificationTimer) {
           clearInterval(phoneVerificationTimer);
       }

       phoneVerificationTimer = setInterval(() => {
           const minutes = Math.floor(timeLeft / 60);
           const seconds = timeLeft % 60;
           timerDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

           if (timeLeft <= 0) {
               clearInterval(phoneVerificationTimer);
               timerDisplay.textContent = '인증 시간이 만료되었습니다. 다시 시도해주세요.';
               document.getElementById('sendSmsBtn').disabled = false;
           }
           timeLeft--;
       }, 1000);
   }

      // 소셜 회원가입 sms 인증 (자동으로 - 붙게 하기 000-0000-0000 이런식으로 하기 위해서)
function formatPhoneNumber(event) {
  let phoneNumber = event.target.value.replace(/\D/g, ''); // 숫자가 아닌 모든 문자를 제거

  if (phoneNumber.length <= 3) {
    event.target.value = phoneNumber;
  } else if (phoneNumber.length <= 7) {
    event.target.value = phoneNumber.replace(/(\d{3})(\d{0,4})/, '$1-$2');
  } else {
    event.target.value = phoneNumber.replace(/(\d{3})(\d{4})(\d{0,4})/, '$1-$2-$3');
  }

  // 전화번호 길이가 11자를 넘으면 잘라내기
  if (phoneNumber.length > 11) {
    event.target.value = event.target.value.slice(0, 13);
  }
}

       function verifyCode() {
           const inputCode = document.getElementById('verificationCode').value;

           if (inputCode === '') {
               alert('인증번호를 입력하세요.');
           } else if (inputCode === serverVerificationCode) {
               alert('인증되었습니다.');
               isEmailVerified = true;
               checkAllVerifications();
           } else {
               alert('인증번호가 일치하지 않습니다.');
           }
       }

       async function checkNickname() {
           const nickname = document.getElementById('userNickname').value;
           const nicknameButton = document.querySelector('#userNickname + button');

           if (!nickname) {
               alert('닉네임을 입력하세요.');
               return;
           }

           try {
               nicknameButton.disabled = true;
               const response = await fetch('/verify/check-nickname', {
                   method: 'POST',
                   headers: {
                       'Content-Type': 'application/x-www-form-urlencoded'
                   },
                   body: 'nickname=' + encodeURIComponent(nickname)
               });

               const result = await response.text();

               if (result === "available") {
                   alert('사용 가능한 닉네임입니다.');
                   isNicknameAvailable = true;
                   nicknameButton.style.backgroundColor = '#4DCFBD';
                   checkAllVerifications();
               } else if (result === "duplicate") {
                   alert('이미 사용 중인 닉네임입니다.');
                   isNicknameAvailable = false;
                   nicknameButton.style.backgroundColor = '#ff4444';
               } else {
                   alert('닉네임 중복 확인 중 오류가 발생했습니다.');
               }
           } catch (error) {
               alert('닉네임 중복 확인 중 오류가 발생했습니다.');
               console.error('Error:', error);
           } finally {
               nicknameButton.disabled = false;
           }
       }

       function togglePasswordVisibility() {
           const passwordInput = document.getElementById("password");
           const checkbox = document.getElementById("showPassword");
           passwordInput.type = checkbox.checked ? "text" : "password";
       }

       function toggleConfirmPasswordVisibility() {
           const confirmPasswordInput = document.getElementById("confirmPassword");
           const checkbox = document.getElementById("showConfirmPassword");
           confirmPasswordInput.type = checkbox.checked ? "text" : "password";
       }

       function checkAllVerifications() {
           const submitBtn = document.getElementById('submitBtn');
           if (submitBtn) {
               if (isEmailVerified && isNicknameAvailable && isPhoneVerified) {
                   submitBtn.disabled = false;
                   submitBtn.style.backgroundColor = '#4DCFBD';
               } else {
                   submitBtn.disabled = true;
                   submitBtn.style.backgroundColor = '#ccc';
               }
           }
       }

function openSocialLoginPopup(url) {
    const width = 500;
    const height = 600;
    const left = (window.screen.width - width) / 2;
    const top = (window.screen.height - height) / 2;

    window.open(
        url,
        'socialLoginPopup',
        `width=${width},height=${height},left=${left},top=${top}`
    );
}

// phoneVerificationForm 이벤트 리스너
document.addEventListener('DOMContentLoaded', function() {
    const phoneVerificationForm = document.getElementById('phoneVerificationForm');
    if (phoneVerificationForm) {
        phoneVerificationForm.addEventListener('submit', function(e) {
            e.preventDefault();

            if (!isPhoneVerified) {
                alert('전화번호 인증이 필요합니다.');
                return;
            }

            const formData = new FormData(this);
            fetch(this.action, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (response.ok) {
                    alert('전화번호가 등록되었습니다.');
                    window.opener.location.href = '/home';  // 부모 창 홈으로 이동
                    window.close();  // 현재 창 닫기
                } else {
                    throw new Error('전화번호 등록에 실패했습니다.');
                }
            })
            .catch(error => {
                alert(error.message);
            });
        });
    }
});


function sendFindIdVerification() {
    const phone = document.getElementById('findIdPhone').value;
    if (!phone.match(/^\d{3}-\d{4}-\d{4}$/)) {
        alert('올바른 전화번호 형식이 아닙니다.');
        return;
    }

    fetch('/verify/send-sms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `phoneNumber=${encodeURIComponent(phone)}`
    })
    .then(response => response.text())
    .then(result => {
        if (result === "SMS 전송에 실패했습니다.") {
            alert(result);
            return;
        }
        document.getElementById('findIdVerificationDiv').style.display = 'block';
        startFindIdTimer();
        alert('인증번호가 전송되었습니다.');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('인증번호 전송 중 오류가 발생했습니다.');
    });
}

function verifyFindIdCode() {
    const phone = document.getElementById('findIdPhone').value;
    const code = document.getElementById('findIdVerificationCode').value;

    fetch('/verify/verify-sms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `phoneNumber=${encodeURIComponent(phone)}&code=${encodeURIComponent(code)}`
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            alert('전화번호 인증이 완료되었습니다.');
            isPhoneVerified = true;
            document.getElementById('findIdForm').querySelector('button[type="submit"]').disabled = false;
            clearInterval(findIdTimer);
            document.getElementById('findIdVerificationDiv').style.display = 'none';
        } else {
            alert('인증번호가 일치하지 않습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('인증번호 확인 중 오류가 발생했습니다.');
    });
}

function startFindIdTimer() {
    let timeLeft = 180; // 3분
    const timerDisplay = document.getElementById('findIdTimer');

    if (findIdTimer) {
        clearInterval(findIdTimer);
    }

    findIdTimer = setInterval(() => {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        timerDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

        if (timeLeft <= 0) {
            clearInterval(findIdTimer);
            timerDisplay.textContent = '인증 시간이 만료되었습니다. 다시 시도해주세요.';
        }
        timeLeft--;
    }, 1000);
}

function sendResetPasswordVerification() {
    const phone = document.getElementById('resetPasswordPhone').value;
    if (!phone.match(/^\d{3}-\d{4}-\d{4}$/)) {
        alert('올바른 전화번호 형식이 아닙니다.');
        return;
    }

    fetch('/verify/send-sms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `phoneNumber=${encodeURIComponent(phone)}`
    })
    .then(response => response.text())
    .then(result => {
        if (result === "SMS 전송에 실패했습니다.") {
            alert(result);
            return;
        }
        document.getElementById('resetPasswordVerificationDiv').style.display = 'block';
        startResetPasswordTimer();
        alert('인증번호가 전송되었습니다.');
    })
    .catch(error => {
        console.error('Error:', error);
        alert('인증번호 전송 중 오류가 발생했습니다.');
    });
}

function verifyResetPasswordCode() {
    const phone = document.getElementById('resetPasswordPhone').value;
    const code = document.getElementById('resetPasswordVerificationCode').value;

    fetch('/verify/verify-sms', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `phoneNumber=${encodeURIComponent(phone)}&code=${encodeURIComponent(code)}`
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'success') {
            alert('전화번호 인증이 완료되었습니다.');
            isPhoneVerified = true;
            document.getElementById('newPasswordDiv').style.display = 'block';
            document.getElementById('resetPasswordForm').querySelector('button[type="submit"]').disabled = false;
            clearInterval(resetPasswordTimer);
            document.getElementById('resetPasswordVerificationDiv').style.display = 'none';
        } else {
            alert('인증번호가 일치하지 않습니다.');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('인증번호 확인 중 오류가 발생했습니다.');
    });
}

function startResetPasswordTimer() {
    let timeLeft = 180; // 3분
    const timerDisplay = document.getElementById('resetPasswordTimer');

    if (resetPasswordTimer) {
        clearInterval(resetPasswordTimer);
    }

    resetPasswordTimer = setInterval(() => {
        const minutes = Math.floor(timeLeft / 60);
        const seconds = timeLeft % 60;
        timerDisplay.textContent = `${minutes}:${seconds.toString().padStart(2, '0')}`;

        if (timeLeft <= 0) {
            clearInterval(resetPasswordTimer);
            timerDisplay.textContent = '인증 시간이 만료되었습니다. 다시 시도해주세요.';
        }
        timeLeft--;
    }, 1000);
}

// 비밀번호 표시/숨김 토글 함수들
function toggleNewPasswordVisibility() {
    const passwordInput = document.getElementById('newPassword');
    const checkbox = document.getElementById('showNewPassword');
    passwordInput.type = checkbox.checked ? 'text' : 'password';
}

function toggleConfirmNewPasswordVisibility() {
    const passwordInput = document.getElementById('confirmNewPassword');
    const checkbox = document.getElementById('showConfirmNewPassword');
    passwordInput.type = checkbox.checked ? 'text' : 'password';
}

// 유틸리티 함수들
function updateAllAgreeCheckbox() {
    const allAgree = document.getElementById('allAgree');
    const termsCheckboxes = document.getElementsByClassName('terms-checkbox');
    const allChecked = Array.from(termsCheckboxes).every(checkbox => checkbox.checked);

    if (allAgree) {
        allAgree.checked = allChecked;
    }
}

function updateTermsSubmitButton() {
    const termsSubmitBtn = document.getElementById('termsSubmitBtn');
    const requiredChecked = document.getElementById('term1')?.checked &&
                           document.getElementById('term2')?.checked;

    if (termsSubmitBtn) {
        termsSubmitBtn.disabled = !requiredChecked;
    }
}



// 전역 스코프에서 사용할 수 있도록 window 객체에 할당
window.openSocialLoginPopup = openSocialLoginPopup;
window.formatPhoneNumber = formatPhoneNumber;
window.sendFindIdVerification = sendFindIdVerification;
window.verifyFindIdCode = verifyFindIdCode;
window.sendResetPasswordVerification = sendResetPasswordVerification;
window.verifyResetPasswordCode = verifyResetPasswordCode;
window.toggleNewPasswordVisibility = toggleNewPasswordVisibility;
window.toggleConfirmNewPasswordVisibility = toggleConfirmNewPasswordVisibility;
window.sendSMSVerification = sendSMSVerification;
window.verifySMSCode = verifySMSCode;
window.sendVerificationEmail = sendVerificationEmail;
window.verifyCode = verifyCode;
window.checkNickname = checkNickname;
window.togglePasswordVisibility = togglePasswordVisibility;
window.toggleConfirmPasswordVisibility = toggleConfirmPasswordVisibility;
