<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="${pageContext.request.contextPath }/assets/css/board.css"
	rel="stylesheet" type="text/css">
<jsp:scriptlet>pageContext.setAttribute("newline", "\n");</jsp:scriptlet>
</head>
<body>
	<div id="container">
		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<div id="content">
			<div id="board" class="board-form">
				<table class="tbl-ex">
					<tr>
						<th colspan="2">글보기</th>
					</tr>
					<tr>
						<td class="label">제목</td>
						<td>${hm.view.title }</td>
					</tr>
					<tr>
						<td class="label">내용</td>
						<td>
							<div class="view-content">${fn:replace(hm.view.content,newline, "<br/>")}
							</div>
						</td>
					</tr>
				</table>
				<div class="bottom">
					<c:if test="${authUser.no == hm.view.userNo}">
						<a
							href="${pageContext.request.contextPath }/board/modify?no=${hm.view.no}&p=${hm.p}&kwd=${kwd}">글수정</a>
					</c:if>
					<c:choose>
						<c:when test="${empty authUser }">
						</c:when>
						<c:otherwise>
							<a
								href="${pageContext.request.contextPath }/board/reply?r	no=${hm.view.no}&p=${hm.p}&kwd=${kwd}">답글
								달기</a>
						</c:otherwise>
					</c:choose>

					<a
						href="${pageContext.request.contextPath }/board?p=${p}&kwd=${kwd}">글목록</a>

				</div>
			</div>
		</div>
		<c:import url="/WEB-INF/views/includes/navigation.jsp">
			<c:param name="menu" value="board"></c:param>
		</c:import>
		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
	</div>
</body>
</html>