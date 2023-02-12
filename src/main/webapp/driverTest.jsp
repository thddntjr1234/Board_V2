<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="http://code.jquery.com/ui/1.8.18/themes/base/jquery-ui.css" type="text/css" />
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.8.18/jquery-ui.min.js"></script>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>게시판 목록</title>
    </script>

</head>
<body>
<h1>자유 게시판 - 목록</h1><br>
<%
    String url = "jdbc:mariadb://localhost:3306/board_v1";
    String userName = "root";
    String password = "0927";
    Connection conn = null;

    Class.forName("com.mariadb.jdbc.Driver");
    conn = DriverManager.getConnection(url, userName, password);
%>
<div>
</div>

</body>
</html>
