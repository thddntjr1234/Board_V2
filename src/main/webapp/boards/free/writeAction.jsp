<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.ebstudy.board_v2.Post.PostDAO" %>
<%@ page import="com.ebstudy.board_v2.Post.PostDTO" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="com.ebstudy.board_v2.File.FileDAO" %>
<%@ page import="com.ebstudy.board_v2.File.FileDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedList" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8"/>
</head>
<body>
<%
    String directory = "/Users/wooseok/Desktop/board_v2/src/main/webapp/files";
    int maxSize = 1024 * 1024 * 50; // 최대 업로드 크기(50MB)

    // MultipartRequest 객체에 파라미터들과 기본 설정 인자들을 넣어주면 파일이 알아서 업로드된다.
    MultipartRequest multi = new MultipartRequest(request, directory, maxSize, "utf-8", new DefaultFileRenamePolicy());

    Long postId = 0L;

    // DTO에 파라미터 받아서 build
    PostDTO postDTO = PostDTO.builder()
            .category(multi.getParameter("category"))
            .author(multi.getParameter("author"))
            .passwd(multi.getParameter("passwd"))
            .title(multi.getParameter("title"))
            .content(multi.getParameter("content"))
            .build();
    System.out.println("입력된 게시글 정보는 - " + postDTO.toString());


    // 게시글 저장
    PostDAO postDAO = PostDAO.getInstance();
    try {
        postId = postDAO.savePost(postDTO);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    System.out.println("savePost()로 리턴받은 포스트의 index값은 : " + postId);

    Enumeration<?> files = multi.getFileNames(); // type=file이었던 input태그의 name을 가져온다
    List<FileDTO> fileLists = new LinkedList<>();

    String element = "";
    String filesystemName = "";
    String originalFileName = "";
    String contentType = "";
    long length = 0;

    while (files.hasMoreElements()) { // 다음 정보가 있으면 Like rs.next()
        element = (String) files.nextElement(); // file을 반환

        filesystemName = multi.getFilesystemName(element); // 서버에 업로드된 파일명을 반환
        originalFileName = multi.getOriginalFileName(element); // 사용자가 업로드한 파일명을 반환

        // 해당 요소 이름
        if (filesystemName == null) {
            continue;
        }

        contentType = multi.getContentType(element);    // 업로드된 파일의 타입을 반환
        length = multi.getFile(element).length(); // 파일의 크기를 반환 (long타입)

        FileDTO fileDTO = FileDTO.builder()
                .fileName(filesystemName)
                .fileRealName(originalFileName)
                .build();

        fileLists.add(fileDTO);
    }

    if (fileLists.size() >= 1) { // files DB에 저장
        FileDAO fileDAO = FileDAO.getInstance();
        try {
            fileDAO.saveFiles(postId,fileLists);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
%>
</body>
</html>
