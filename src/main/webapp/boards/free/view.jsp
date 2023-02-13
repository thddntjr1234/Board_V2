<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.io.File" %>
<%@ page import="com.ebstudy.board_v2.repository.PostDAO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.PostDTO" %>
<%@ page import="com.ebstudy.board_v2.repository.FileDAO" %>
<%@ page import="com.ebstudy.board_v2.repository.CommentDAO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.FileDTO" %>
<%@ page import="com.ebstudy.board_v2.web.dto.CommentDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="multipart/form-data" ; charset=UTF-8">
    <meta name="viewport" content="width=device-width" , inital-scale="1">
    <title>게시판 - 등록</title>
    <%--bootstrap 적용--%>
    <link rel="stylesheet" href="/webjars/bootstrap/5.1.3/css/bootstrap.css">

</head>
<%
    request.setCharacterEncoding("utf-8");

    // TODO: postId가 전달되지 않을 경우(예외)를 고려하자
    Long postId = Long.valueOf(request.getParameter("id"));

    PostDAO postDAO = PostDAO.getInstance();
    postDAO.increaseHits(postId);
    PostDTO post = postDAO.getPost(postId);

    FileDAO fileDAO = FileDAO.getInstance();
    List<FileDTO> files = fileDAO.getFileNames(post.getId());

    CommentDAO commentDAO = CommentDAO.getInstance();
    List<CommentDTO> comments = commentDAO.getCommentList(postId);
%>
<body>
<div class="container">
    <h1>게시판 - 보기</h1>
    <br>
</div>
<div class="container">
    <div class="container">
        <span><%=post.getAuthor()%></span>
        <span class="float-end">
            등록일시 <%=post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%>
            &nbsp 수정일시
        <%
            if (post.getCreatedDate() != null) {
                out.println(post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }
        %>
        </span>
    </div>
    <br>
    <div class="container" style="border: black">
        <span style="font-size: 20px;">[<%=post.getCategory()%>]    <%=post.getTitle()%>
        </span>
        <span class="float-end">조회수: <%=post.getHits()%></span>
    </div>
    <br>
    <div class="container\">
        <p class="text-md-start" style="font-size: 14px;"><%=post.getContent().replaceAll("\r\n", "<br>")%>
        </p>
    </div>
    <br>
    <div class="container">
<%--        이렇게 <div><a href="download(32)"></a></div>--%>
        <%
            for (FileDTO file : files) {
                out.println("<div><a href=\"#\" onclick=\"DownloadFile(\'" + file.getFileName() + "\')" + "\">" + file.getFileRealName() + "</a></div>");
            }
        %>
    </div>
</div>
<hr>
</div>
<div class="container bg-secondary" style="--bs-bg-opacity: 0.3; font-size: 15px;">
    <div class="container">
        <table>
            <%
                if (comments != null) {
                    for (CommentDTO comment : comments) {
                        System.out.println("comments: " + comment.toString());
                        out.println("<tr>");
                        out.println("<td>" + comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "</td>");
                        out.println("</tr>");
                        out.println("<tr>");
                        out.println("<td>" + comment.getComment() + "</td>");
                        out.println("</tr>");
                        out.println("<tr></tr>");
                    }
                }
            %>
        </table>
    </div>
    <div class="container">
        <form class="table" id="commentForm">
            <textarea class="form-control border border-secondary" rows="2" name="comment"></textarea>
            <div class="d-flex justify-content-end">
                <button class="btn btn-primary d-flex justify-content-sm-end" type="button"
                        onclick="saveComment(<%=postId%>)">등록
                </button>
            </div>
        </form>
    </div>
</div>
<div class="container d-flex justify-content-center">
    <button class="btn btn-secondary" onclick="location.href='/boards/free/list.jsp'">목록</button>
    <button class="btn btn-secondary" onclick="location.href='/boards/free/checkPwd.jsp?postId=<%=postId%>&operation=modify'">수정</button>
    <button class="btn btn-secondary" onclick="location.href='/boards/free/checkPwddelete.jsp?postId=<%=postId%>&operation=delete'">삭제</button>
</div>
<%--bootstrap, jquery--%>
<script src="/webjars/jquery/3.3.1/jquery.min.js"></script>
<script src="/webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script type="text/javascript">
    function DownloadFile(fileName) {
        //Set the File URL.
        var url = "/files/" + fileName;

        console.log("url: ", url);
        $.ajax({
            url: url,
            cache: false,
            xhr: function () {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState == 2) {
                        if (xhr.status == 200) {
                            xhr.responseType = "blob";
                        } else {
                            xhr.responseType = "text";
                        }
                    }
                };
                return xhr;
            },
            success: function (data) {
                //Convert the Byte Data to BLOB object.
                var blob = new Blob([data], {type: "application/octetstream"});

                //Check the Browser type and download the File.
                var isIE = false || !!document.documentMode;
                if (isIE) {
                    window.navigator.msSaveBlob(blob, fileName);
                } else {
                    var url = window.URL || window.webkitURL;
                    link = url.createObjectURL(blob);
                    var a = $("<a />");
                    a.attr("download", fileName);
                    a.attr("href", link);
                    $("body").append(a);
                    a[0].click();
                    $("body").remove(a);
                }
            }
        });
    };

    function saveComment(postId) {

        let formData = $("#commentForm").serialize();
        formData += "&postId=" + postId;
        console.log("formdata = ", formData);
        $.ajax({
            type: "POST",
            url: "/boards/free/commentAction.jsp",
            data: formData,
            error: function (e) {
                alert("전송 실패", e);
            },
            success: function () {
                alert("댓글 작성 성공");
                window.location.href = "/boards/free/view.jsp?id=" + postId;
            }
        });
    }

</script>
</body>
</html>
