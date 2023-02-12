<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.oreilly.servlet.MultipartResponse" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="com.ebstudy.board_v2.*" %>
<%@ page import="com.ebstudy.board_v2.Comment.CommentDAO" %>
<%@ page import="com.ebstudy.board_v2.Comment.CommentDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
  <meta charset="UTF-8"/>
</head>
<body>
<%
  String comment = request.getParameter("comment");
  Long postId = Long.parseLong(request.getParameter("postId"));

  // DTO에 파라미터 받아서 build
  CommentDTO commentDTO = CommentDTO.builder()
          .comment(comment)
          .postId(postId)
          .build();
  System.out.println("입력된 게시글 정보는 - " + commentDTO.toString());

  CommentDAO commentDAO = CommentDAO.getInstance();
  commentDAO.saveComment(commentDTO);
%>
</body>
</html>
