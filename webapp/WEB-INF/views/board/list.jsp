<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link href="${pageContext.request.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
	var dataId, p, dialog, keyword;

	$(function() {
		$("#kwdsubmit").click(function() {
			keyword = $("#kwd").val();
			console.log(keyword);
		});
		dialog = $("#dialog-confirm")
				.dialog(
						{
							autoOpen : false,
							resizable : false,
							height : "auto",
							width : 400,
							modal : true,
							buttons : {
								"삭제" : function() {
									var deleteUrl = "${pageContext.request.contextPath }/board/delete?no="
											+ dataId + "&kwd=" + keyword;
									window.location.href = deleteUrl;
									$(this).dialog("close");
								},
								"취소" : function() {
									$(this).dialog("close");
								}
							}
						});
		//삭제 버튼 click event (live event)
		$(document).on("click", "#delete_td a", function(event) {
			event.preventDefault();
			dialog.dialog("open");
			dataId = $(this).data("id");
		});

	});
</script>
</head>
<body>
	<c:set var="start" value="${hm.page.startPage}" />
	<c:set var="size" value="${hm.page.listSize}" />
	<c:set var="last" value="${hm.page.lastPage}" />
	<c:set var="current" value="${hm.page.currentPage}" />
	<c:set var="kwd" value="${hm.page.kwd}" />
	<c:set var="count" value="${hm.total}" />

	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="board">
				<form id="search_form"
					action="${pageContext.request.contextPath }/board" method="get">
					<input type="text" id="kwd" name="kwd" value=""> <input
						id="kwdsubmit" type="submit" value="찾기">
				</form>
				<table class="tbl-ex" id="board_table">
					<tr>
						<th>번호</th>
						<th>제목</th>
						<th>글쓴이</th>
						<th>조회수</th>
						<th>작성일</th>
						<th>&nbsp;</th>
					</tr>

					<c:forEach items="${hm.list}" var="list" varStatus="status">
						<tr>
							<td>[${count - list.rn+1}]</td>
							<td class="left" style="padding-left:${list.depth*20}px"><c:choose>
									<c:when test="${list.depth == 0}">
										<a
											href="${pageContext.request.contextPath }/board/view?no=${list.no}&p=${current}&kwd=${kwd}">
											${list.title}</a>
									</c:when>
									<c:otherwise>
										<img
											src="${pageContext.request.contextPath }/assets/images/reply.png">
										<a
											href="${pageContext.request.contextPath }/board/view?no=${list.no}&p=${current}&kwd=${kwd}">
											${list.title}</a>
									</c:otherwise>
								</c:choose></td>
							<td>${list.userName}</td>
							<td>${list.hits }</td>
							<td>[${list.regDate }]</td>
							<c:choose>
								<c:when test="${empty authUser }">
									<td></td>
								</c:when>
								<c:otherwise>
									<c:if test="${authUser.no == list.userNo}">
										<td id="delete_td"><a href="" data-id="${list.no}"
											class="del"><img
												src="${pageContext.request.contextPath }/assets/images/recycle.png"></a></td>
									</c:if>
									<c:if test="${authUser.no != list.userNo}">
										<td><img
											src="${pageContext.request.contextPath }/assets/images/recycle1.png"></td>
									</c:if>
								</c:otherwise>
							</c:choose>
						</tr>

					</c:forEach>
				</table>
				<div class="pager">

					<ul>
						<c:choose>
							<c:when test="${1 <= start-size+1 }">
								<li><a
									href="${pageContext.request.contextPath }/board?p=${start-size}&kwd=${kwd}">◀</a></li>
							</c:when>
							<c:otherwise>
								<li></li>
							</c:otherwise>
						</c:choose>


						<c:forEach var="i" begin="${ start }" end="${start + size -1}">
							<c:choose>
								<c:when test="${hm.page.currentPage == i }">
									<li class="selected">${i}</li>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${last >= i}">
											<li><a
												href="${pageContext.request.contextPath }/board?p=${i}&kwd=${kwd}">${i}</a></li>
										</c:when>
										<c:otherwise>
											<li>${i}</li>
										</c:otherwise>
									</c:choose>
								</c:otherwise>

							</c:choose>
						</c:forEach>
						<c:choose>
							<c:when test="${last >= start+size }">
								<li><a
									href="${pageContext.request.contextPath }/board?p=${start+size}&kwd=${kwd}">▶</a></li>
							</c:when>
							<c:otherwise>
								<li></li>
							</c:otherwise>
						</c:choose>

					</ul>
				</div>
				<div class="bottom">
					<c:choose>
						<c:when test="${empty authUser }">
						</c:when>
						<c:otherwise>
							<a
								href="${pageContext.request.contextPath }/board/write?p=${current}&kwd=${kwd}"
								id="new-book">글쓰기</a>
						</c:otherwise>
					</c:choose>

				</div>
			</div>
		</div>
		<div id="dialog-confirm" title="게시글 삭제" style="display: none">
			<p>
				<span class="ui-icon ui-icon-alert"
					style="float: left; margin: 12px 12px 20px 0;"></span>삭제하시겠습니까?
			</p>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board" />
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>