<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!doctype html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/user.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	$(function() {
		$("#join-form").submit(function() {
			return true;
			//1.이름 체크
			if ($("#name").val() == "") {
				$("#dialog p").text("이름은 필수 입력 항목입니다.");
				//$("#dialog[title]").
				$("#dialog").dialog();
				$("#name").focus();
				return false;
			}

			//2-1. 이메일 체크
			if ($("#email").val() == "") {
				$("#dialog p").text("이메일은 필수 입력 항목입니다.");
				$("#dialog").dialog();
				$("#email").focus();
				return false;
			}

			//2-2. 이메일 중복 체크 유무
			if ($("#chkemail").is(":visible") == false) {
				$("#dialog p").text("이메일 중복체크 하세요.");
				$("#dialog").dialog();
				return false;
			}
			//비밀번호 체크
			if ($("input[type='password']").val() == "") {
				$("#dialog p").text("비밀번호는 필수 입력 항목입니다.");
				$("#dialog").dialog();
				$("input[type='password']").focus();
				return false;
			}
			if ($("#agree-prov").is(":checked") == false) {
				$("#dialog p").text("약관에 동의하세요.");
				$("#dialog").dialog();
				return false;
			}
		});

		$("#email").change(function() {
			$("#chkemail").hide();
			$("#btn-chkemail").show();
		});
		$("#btn-chkemail")
				.click(
						function() {
							var email = $("#email").val();
							if (email == "") {
								return;
							}
							$
									.ajax({
										url : "${pageContext.request.contextPath }/api/user?a=chkemail&email="
												+ email,
										type : "get",
										dataType : "json",
										data : "",
										contentType : "application/json",
										success : function(response) {
											if (response.result != "success") {
												console.log(response.message);
												return;
											} else if (response.data == "exist") {
												$("#dialog p").text(
														"이미존재하는 이메일입니다.");
												$("#dialog").dialog();
												$("#email").val("").focus();

												return;
											} else if (response.data == "not exist") {
												$("#chkemail").show();
												$("#btn-chkemail").hide();
												return;
											}
										},
										error : function(jqXHR, status, e) {
											console.log(status + ":" + e);
										}
									});
						});
	});
</script>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="user">

				<form id="join-form" name="joinForm" method="post"
					action="${pageContext.request.contextPath }/user/join">
					<label class="block-label" for="name">이름</label> <input id="name"
						name="name" type="text" value=""> <label
						class="block-label" for="email">이메일</label> <input id="email"
						name="email" type="text" value=""> <img
						src="${pageContext.request.contextPath }/assets/images/reply.png"
						id="chkemail" style="display: none"> <input
						id="btn-chkemail" type="button" value="중복체크"> <label
						class="block-label">패스워드</label> <input name="password"
						type="password" value="">

					<fieldset>
						<legend>성별</legend>
						<label>여</label> <input type="radio" name="gender" value="female"
							checked="checked"> <label>남</label> <input type="radio"
							name="gender" value="male">
					</fieldset>

					<fieldset>
						<legend>약관동의</legend>
						<input id="agree-prov" type="checkbox" name="agreeProv" value="y">
						<label>서비스 약관에 동의합니다.</label>
					</fieldset>

					<input type="submit" value="가입하기">

				</form>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp"></c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
	<div id="dialog" title="Basic title" style="display: none">
		<p></p>
	</div>
</body>
</html>