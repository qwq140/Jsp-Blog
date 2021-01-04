<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp"%>

<div class="container">
	<form action="/blog/user?cmd=join" method="post">
		<div class="form-group">
			<input type="text" name="username" class="form-control"
				placeholder="Enter username" required />
		</div>
		<div class="form-group">
			<input type="password" name="password" class="form-control"
				placeholder="Enter password" required />
		</div>
		<div class="form-group">
			<input type="email" name="email" class="form-control"
				placeholder="Enter email" required />
		</div>
		<div class="d-flex justify-content-end">
			<button type="button" class="btn btn-info" onClick="goPopup()">주소검색</button>
		</div>
		<div class="form-group">
			<input type="text" name="address" id="address" class="form-control"
				placeholder="Enter address" required readonly />
		</div>
		<button type="submit" class="btn btn-primary">회원가입완료</button>
	</form>
</div>

<script>
	function goPopup() {
		// 주소검색을 수행할 팝업 페이지를 호출합니다.
		// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
		var pop = window.open("/blog/user/jusoPopup.jsp", "pop",
				"width=570,height=420, scrollbars=yes, resizable=yes");

		// 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
		//var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes"); 
	}

	function jusoCallBack(roadFullAddr) {
		// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
		var addressEl = document.querySelector("#address");
		addressEl.value=roadFullAddr;

	}
</script>
</body>
</html>