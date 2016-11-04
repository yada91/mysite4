<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<link
	href="${pageContext.request.contextPath }/assets/css/guestbook.css"
	rel="stylesheet" type="text/css">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath }/assets/js/jquery/jquery-1.9.0.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	var dataId = 0;
	var isEnd = false;
	var page = 0;
	var dialog, form;
	var render = function(vo, flag) {

		var htmls = "<li id="+vo.id+">" + "<strong>" + vo.name + "</strong>"
				+ "<strong>" + vo.regdate + "</strong>" + "<p>"
				+ vo.content.replace(/\n/gi, "<br>") + "</p>"
				+ "<a href='' data-id="+vo.id+">삭제</a>" + "</li>";
		if (flag == true) {
			$("#list-guestbook").append(htmls);
		} else {
			$("#list-guestbook").prepend(htmls);
		}
	}
			emailRegex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/,
			name1 = $("#name1"), password1 = $("#password1"), allFields = $([])
					.add(name1).add(password1);
	function deleteform() {

		var valid = true;
		allFields.removeClass("ui-state-error");

		if (valid) {
			var param = "a=ajax-delete&name=" + $("#name1").val() + "&pw="
					+ $("#password1").val() + "&id=" + dataId;

			//ajax 통신 시작
			$.ajax({
				url : "${pageContext.request.contextPath }/api/gb?" + param,
				type : "get",
				dataType : "json",
				data : param,
				contentType : "application/json",
				success : function(response) {
					if (response.result != "success") {
						console.error(response.message);
						return;
					}
					if (response.data == "yes") {
						$("#" + dataId).remove();
						dialog.dialog("close");
					} else {
						$("#dialog-form .validateTips.normal").hide();
						$("#dialog-form .validateTips.error").show();
						$("#name1").val("").focus();
						$("#password1").val("");
					}
				},
				error : function(jqXHR, status, e) {
					console.log(status + ":" + e);
				}
			});

		}
		return valid;
	}

	var fetchList = function() {
		if (isEnd == true) {
			return;
		}
		++page;
		$.ajax({
			url : "${pageContext.request.contextPath }/api/gb?a=ajax-list&p="
					+ page,
			type : "get",
			dataType : "json",
			data : "",
			success : function(response) {
				if (response.result != "success") {
					console.error(response.message);
					isEnd = true;
					return;
				}

				//redering
				$(response.data).each(function(index, vo) {
					render(vo, true);
				});

				if (response.data.length < 5) {
					isEnd = true;
					$("#btn-fetch").prop("disabled", true);
				}

			},
			error : function(jqXHR, status, e) {
				console.error(status + ":" + e);
			}

		});
	}

	$(function() {

		dialog = $("#dialog-form").dialog({
			autoOpen : false,
			height : 300,
			width : 250,
			modal : true,
			buttons : {
				"delete" : deleteform,
				Cancel : function() {
					dialog.dialog("close");
				}
			},
			close : function() {
				form[0].reset();
				allFields.removeClass("ui-state-error");
			}
		});

		form = dialog.find("form").on("submit", function(event) {
			event.preventDefault();
			deleteform();
		});

		//삭제 버튼 click event (live event)
		$(document).on("click", "#list-guestbook li a", function(event) {
			event.preventDefault();
			dialog.dialog("open");
			console.log($(this).data("id"));
			dataId = $(this).data("id");
		});//라이브 이벤트 추가 

	});

	$(function() {
		//추가
		$("#add-form").submit(
				function(event) {
					event.preventDefault();

					var name = $("input[name='name']").val()
					var password = $("input[name='password']").val()
					var content = $("textarea[name='content']").val()
					var param = "a=ajax-add" + "&name=" + name + "&pw="
							+ password + "&ct=" + content;
					console.log("${pageContext.request.contextPath }/api/gb?"
							+ param);

					//ajax insert 
					$.ajax({
						url : "${pageContext.request.contextPath }/api/gb?"
								+ param,
						type : "get",
						dataType : "json",
						data : param,
						contentType : "application/json",
						success : function(response) {
							if (response.result != "success") {
								console.error(response.message);
								return;
							}
							render(response.data, false);
						},
						error : function(jqXHR, status, e) {
							console.log(status + ":" + e);
						}
					});
				});
		//스크롤
		$(window).scroll(function() {
			var $window = $(this);
			var scrollTop = $window.scrollTop();
			var windowHeight = $window.height();
			var documentHeight = $(document).height();

			if (scrollTop + windowHeight + 5 > documentHeight) {
				fetchList();
			}
		});
		//버튼 리스트
		$("#btn-fetch").click(function() {
			fetchList();
		});
		fetchList();
	});
</script>

</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<h1>방명록</h1>
				<form id="add-form" method="post">
					<input type="text" name="name" placeholder="이름"> <input
						type="password" name="password" placeholder="비밀번호">
					<textarea name="content" placeholder="내용을 입력해 주세요."></textarea>
					<input type="submit" value="보내기" />
				</form>
				<ul id="list-guestbook">
					<!-- 리스트 부분 -->
				</ul>
				<button style="margin-top: 27px" id="btn-fetch">가져오기</button>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="guestbook-ajax" />
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
	<div id="dialog-form" title="delete form">
		<p class="validateTips normal">이름과 비밀번호를 입력하세요.</p>
		<p class="validateTips error" style="display: none">이름과 비밀번호가
			틀립니다.</p>
		<form>
			<fieldset>
				<label for="name">name</label> <input type="text" id="name1"
					value="" class="text ui-widget-content ui-corner-all"> <label
					for="password">Password</label> <input type="password"
					id="password1" value=""
					class="text ui-widget-content ui-corner-all">

				<!-- Allow form submission with keyboard without duplicating the dialog button -->
				<input type="submit" tabindex="-1"
					style="position: absolute; top: -1000px">
			</fieldset>
		</form>
	</div>
</body>
</html>
