<%--<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>--%>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>--%>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.ebstudy.board_v2.repository.PostDAO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.PostDTO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.CategoryDTO" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css"/>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, inital-scale=1">

    <title>게시판 목록</title>
    <%--bootstrap, datetimepicker 적용--%>
    <%--bootstrap, jquery--%>
    <link rel="stylesheet" href="/webjars/bootstrap/5.1.3/css/bootstrap.css">
</head>
<%
    List<CategoryDTO> categoryList = (List<CategoryDTO>) request.getAttribute("categoryList");
    List<PostDTO> postList = (List<PostDTO>) request.getAttribute("postList");

    HashMap<String, Integer> pagingValues = (HashMap<String, Integer>) request.getAttribute("pagingValues");

    int totalPostCount = (int) request.getAttribute("totalPostCount");
    int startPage = pagingValues.get("startPage");
    int endPage = pagingValues.get("endPage");
    int currentPage = pagingValues.get("currentPage");
    int totalPage = pagingValues.get("totalPage");
%>
<body>
<div class="container">
    <h1>게시판 - 목록</h1><br>
</div>
<div class="container">
    <form class="form-inline" action="/WEB-INF/boards/free/list" method="get">
        <div class="input-group-sm">
            등록일

            <input name="startDate" class="form-control-sm" type="date" required/>
            <input name="endDate" class="form-control-sm" type="date" required/>

            <select class="form-select-sm" name="category" required>
                <option value="all">전체 카테고리</option>
                <%
                    for (CategoryDTO category : categoryList) {
                        out.println("<option value=" + category.getCategoryId() + ">" + category.getCategory() + "</option>");
                    }
                %>
            </select>
            <input type="search" name="keyword">
            <input type="submit" value="검색">
        </div>
    </form>
</div>
<br>
<div class="container">
    <p> 총  <%=totalPostCount%>건 </p>
</div>

<%--게시글 부분 list.get(page*1~10)으로 id값 가져오게 해야할 것 같음--%>
<div class="container">
    <table class="table table-hover" style="text-align: center; font-size: 12px">
        <thead>
        <tr>
            <th class="w-auto" style="text-align: center;">카테고리</th>
            <th class="w-auto" style="text-align: center;">&nbsp</th>
            <th class="w-auto" style="text-align: center;">제목</th>
            <th class="w-auto" style="text-align: center;">작성자</th>
            <th class="w-auto" style="text-align: center;">조회수</th>
            <th class="w-auto" style="text-align: center;">등록 일시</th>
            <th class="w-auto" style="text-align: center;">수정 일시</th>
        </tr>
        </thead>
        <tbody>
        <%
            // LocalDateTime -> yyyy-MM-dd HH:mm String 타입으로 변환, modifiedDate NullPointException 방지하기 위해 값 체크
            for (PostDTO post : postList) {
                String createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String modifiedDate = "-";
                if (post.getModifiedDate() != null) {
                    modifiedDate = post.getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                }

                out.println("<tr>");
                out.println("<td>" + post.getCategory() + "</td>");
                if (post.getIsHaveFile()) {
                    out.println("<td> F </td>");
                } else {
                    out.println("<td> </td>");
                }
                if (post.getTitle().length() > 80) {
                    out.println("<td class=\"d-flex justify-content-start\"><a href=\"view?postId=" + post.getPostId() + "\">" + post.getTitle().substring(0, 80) + "..." + "</a></td>");
                } else {
                    out.println("<td class=\"d-flex justify-content-start\"><a href=\"view?postId=" + post.getPostId() + "\">" + post.getTitle() + "</a></td>");
                }
                out.println("<td>" + post.getAuthor() + "</td>");
                out.println("<td>" + post.getHits() + "</td>");
                out.println("<td>" + createdDate + "</td>");
                out.println("<td>" + modifiedDate + "</td>");
                out.println("</tr>");
            }
        %>
        </tbody>
    </table>
</div>
<%--페이지 부분--%>
<div class="d-flex justify-content-center">
    <ul class="pagination">
        <%
            if (startPage > 1) {
                out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"list?pageNumber=1\">처음</a></li>");
            }

            if (currentPage > 1) {
                out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"list?pageNumber=" + (currentPage - 1) + "\">이전</a></li>");

            }

            for (int i = startPage; i <= endPage; i++) {
                out.println(
                        "<li class=\"page-item\"><a class=\"page-link\" href=\"list?pageNumber=" + i + "\">" + i + "</a></li>");
            }

            if (currentPage < totalPage) {
                out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"list?pageNumber=" + (currentPage + 1) + "\">다음</a></li>");
            }

            if (endPage < totalPage) {

                out.println("<li class=\"page-item\"><a class=\"page-link\" href=\"list.jsp?pageNumber=" + totalPage + "\">끝</a></li>");
            }
        %>
    </ul>
</div>
<div class="d-flex justify-content-end">
    <button class="btn btn-secondary" onclick="location.href='write'">등록</button>
</div>


<script src="/webjars/jquery/3.3.1/jquery.min.js"></script>
<script src="/webjars/bootstrap/5.1.3/js/bootstrap.js"></script>
<script src="/webjars/popper.js/2.9.3/umd/popper.min.js"></script>
</body>
</html>
