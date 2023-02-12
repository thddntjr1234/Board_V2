<%@ page import="com.ebstudy.board_v2.Post.PostDAO" %>
<%@ page import="com.ebstudy.board_v2.Post.PostDTO" %><%--
  Created by IntelliJ IDEA.
  User: wooseok
  Date: 2023/02/11
  Time: 11:08 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Long postId = Long.parseLong(request.getParameter("postId"));
    String password = request.getParameter("inputPassword");

    PostDAO postDAO = PostDAO.getInstance();
    PostDTO post = postDAO.getPost(postId);

    if (post.getPasswd().equals(password)) {
        response.setStatus(HttpServletResponse.SC_OK);
    } else {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
%>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
