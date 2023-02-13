<%@ page import="com.ebstudy.board_v2.repository.PostDAO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.PostDTO" %>

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
