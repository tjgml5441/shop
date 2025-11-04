<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>shop - 회원가입</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

<style>
	body {
	    font-family: Arial, sans-serif;
	    background-color: #f0f0f0;
	    display: flex;
	    justify-content: center;
	    align-items: center;
	    height: 100vh;
	    margin: 0;
	}
	
	.login-container { 
	    background: white;
	    padding: 30px 50px; 
	    border-radius: 10px;
	    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	    width: 500px; 
        box-sizing: border-box;
	}
	
	h1 {
	    font-size: 30px;
	    color: #333;
	    margin-bottom: 5px; 
	    font-weight: bold;
	    display: block; 
	    padding-bottom: 0;
	    text-align: center;
	}

    .login-container > p.description {
        font-size: 14px;
        color: #777;
        margin-top: 0;
        margin-bottom: 10px; 
        text-align: center;
    }
	
	/* SIGN IN 하단에 표시될 메시지 스타일 */
	.message {
	    color: #d9534f;
	    text-align: center;
        margin-top: 10px;
	    margin-bottom: 5px; 
	    padding: 8px;
        min-height: 1.2em; /* 메시지가 없을 때 공간 확보 */
	}
	.input-group {
	    margin-bottom: 15px; 
	    width: 100%;
        text-align: left; 
	}
    
    .input-group label {
        display: block; 
        font-size: 14px;
        color: #555;
        font-weight: bold;
        margin-bottom: 5px; 
        width: auto; 
    }

	.input-group input[type="text"],
	.input-group input[type="password"] {
	    width: 100%; 
	    padding: 12px 15px; 
	    border: 1px solid #ddd;
	    border-radius: 5px;
	    box-sizing: border-box; 
	    font-size: 13px;
        text-align: left; 
	}
    
    /* 아이디 입력 폼의 중복확인 버튼 정렬을 위한 스타일 */
    .field-input-area {
        display: flex;
        gap: 5px;
    }
    .field-input-area input {
        flex-grow: 1;
    }
    .check-btn {
        width: 100px;
        padding: 12px 15px;
        border: 1px solid #ddd;
        border-radius: 5px;
        background-color: #f7f7f7;
        cursor: pointer;
        font-size: 13px;
        box-sizing: border-box;
        height: 40px; 
    }
    .check-btn:disabled {
        background-color: #eee;
        cursor: not-allowed;
    }
    .check-msg {
        margin-top: 5px; 
        font-size: 13px;
        height: 1.2em; /* 높이 고정 */
    }

    .button-group {
        display: flex;
        justify-content: space-between; 
        gap: 10px;
        margin-top: 25px; 
    }

	.signup-btn, .cancel-btn {
	    width: 50%; 
	    padding: 14px; 
	    color: white;
	    border: none;
	    border-radius: 40px; 
	    cursor: pointer;
	    font-size: 16px; 
	    font-weight: bold;
	    transition: background-color 0.3s;
	}

	.signup-btn {
	    background-color: #dfd3c3; 
	}
	
	.signup-btn:hover:enabled {
	    background-color: #c7b199;
	}
    
    .cancel-btn {
        background-color: #777; 
    }
    
    .cancel-btn:hover {
        background-color: #555;
    }
</style>
</head>
<body>
	<div class="login-container">
        
		<h1>SIGN UP</h1>
        <p class="description">회원 정보를 입력해주세요.</p>
		
		<form method="post" action="${pageContext.request.contextPath}/out/addCustomer">
			<input type="hidden" id="idCheck" name="idCheck" value="${empty idCheck ? '0' : idCheck}"> 
			
			<p class="message">${message}</p>

			<div class="input-group">
				<label for="id">아이디:</label>
                <div class="field-input-area">
				    <input type="text" id="id" name="id" value="${id}" required>
				    <button type="button" id="idCkBtn" class="check-btn">중복확인</button>
                </div>
				<div id="idCkMsg" class="check-msg"></div> 
			</div>

			<div class="input-group">
				<label for="password">비밀번호:</label>
				<input type="password" id="password" name="password" required>
			</div>
			<div class="input-group">
				<label for="passwordCheck">비밀번호 확인:</label>
				<input type="password" id="passwordCheck" name="passwordCheck" required>
			</div>
			
			<div class="input-group">
				<label for="name">이름:</label>
                <div class="field-input-area">
				    <input type="text" id="name" name="name" value="${name}" required>
                </div>
			</div>
			
			<div class="input-group">
				<label for="phone">전화번호:</label>
                <div class="field-input-area">
				    <input type="text" id="phone" name="phone" value="${phone}" required>
                </div>
			</div>
			
			<div class="button-group">
				<button type="submit" id="submitBtn" class="signup-btn">회원가입</button>
				<button type="button" class="cancel-btn" onclick="location.href='${pageContext.request.contextPath}/out/login'">취소</button>
			</div>
		</form>
	</div>
	

<script>
$(document).ready(function(){
    // 아이디 중복 확인 상태만 관리
    const $idInput = $('#id');
    const $idCheckFlag = $('#idCheck');
    const $idCkMsg = $('#idCkMsg');
    
    // 이전에 submit 버튼 활성화를 제어하던 updateSubmitButton() 함수를 제거하거나 비활성화합니다.
    // 여기서는 완전히 제거하여 submit 버튼의 상태를 변경하지 않도록 합니다.

    // 초기 아이디 메시지 설정
    if ($idCheckFlag.val() === '1') {
        $idCkMsg.text('확인 완료').css('color', 'green'); 
    }

    // 아이디 입력 필드 변경 시 중복확인 플래그 초기화
    $idInput.on('change keyup', function() {
        $idCheckFlag.val('0'); 
        $idCkMsg.text(''); // 메시지 초기화
    });
	
	// 중복확인 버튼 클릭 이벤트 (AJAX 호출)
    $('#idCkBtn').click(function() {
        const idValue = $idInput.val().trim();
        let message = '';

        if (idValue.length === 0) {
            message = '아이디를 입력해 주세요.';
        }

        if (message) {
            $idCkMsg.text(message).css('color', 'red');
            $idCheckFlag.val('0');
            return;
        }
        
        // AJAX 호출
        $.ajax({
            url: '${pageContext.request.contextPath}/out/idCkRestController',
            type: 'post',
            data: { id: idValue }, 
            dataType: 'json',
            success: function(data){
                if(data.result === 'duplicate'){
                    $idCkMsg.text('이미 사용 중인 아이디입니다.').css('color', 'red');
                    $idCheckFlag.val('0'); 
                } else {
                    $idCkMsg.text('사용 가능한 아이디입니다.').css('color', 'green');
                    $idCheckFlag.val('1'); // 중복 확인 완료 (사용 가능)
                }
            },
            error: function(xhr, status, error){
                console.error("AJAX Error: ", status, error);
                $idCkMsg.text('오류가 발생했습니다. 다시 시도해 주세요.').css('color', 'red');
                $idCheckFlag.val('0');
            }
        });
    });
});
</script>

</body>
</html>