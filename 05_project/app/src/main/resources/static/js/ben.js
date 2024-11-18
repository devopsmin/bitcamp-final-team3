let banModal;

document.addEventListener('DOMContentLoaded', function() {
   banModal = new bootstrap.Modal(document.getElementById('banModal'), {
       backdrop: 'static',
       keyboard: false
   });
});

function openBanModal(userNo) {
   document.getElementById('banUserNo').value = userNo;
   document.getElementById('banForm').reset();
   banModal.show();
}

async function banUser() {
   const form = document.getElementById('banForm');
   if (!form.checkValidity()) {
       form.reportValidity();
       return;
   }

   const formData = new FormData(form);
   const params = new URLSearchParams();
   for (let [key, value] of formData.entries()) {
       params.append(key, value);
   }

   try {
       const response = await fetch('/admin/ban', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/x-www-form-urlencoded',
           },
           body: params
       });

       const result = await response.text();
       if (result === 'success') {
           alert('사용자가 차단되었습니다.');
           banModal.hide();
           location.reload();
       } else {
           alert(result);
       }
   } catch (error) {
       console.error('Error:', error);
       alert('오류가 발생했습니다.');
   }
}

function deleteUser(no) {
   if (confirm("정말 삭제하시겠습니까?")) {
       const xhr = new XMLHttpRequest();
       xhr.open("DELETE", "/admin/" + no, true);
       xhr.onload = function() {
           if (xhr.status === 200) {
               location.href = "/admin";
           } else {
               alert("삭제 실패입니다!");
           }
       };
       xhr.send();
   }
}

async function unbanUser(userNo) {
    if (!confirm('정말 차단을 해제하시겠습니까?')) {
        return;
    }

    try {
        const response = await fetch('/admin/unban', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: `userNo=${userNo}`
        });

        const result = await response.text();
        if (result === 'success') {
            alert('차단이 해제되었습니다.');
            location.reload();
        } else {
            alert(result);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('오류가 발생했습니다.');
    }
}
