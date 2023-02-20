package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.repository.FileDAO;
import com.ebstudy.board_v2.repository.PostDAO;
import com.ebstudy.board_v2.web.Command;
import com.ebstudy.board_v2.web.dto.FileDTO;
import com.ebstudy.board_v2.web.dto.PostDTO;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SavePostCommand implements Command {

    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {

        String directory = "/Users/wooseok/Desktop/board_v2/src/main/webapp/files";
        int maxSize = 1024 * 1024 * 50; // 최대 업로드 크기(50MB)

        // MultipartRequest 객체에 파라미터들과 기본 설정 인자들을 넣어주면 파일이 알아서 업로드된다.
        MultipartRequest multi = null;
        try {
            multi = new MultipartRequest(request, directory, maxSize, "utf-8", new DefaultFileRenamePolicy());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // DTO에 파라미터 set
        PostDTO post = new PostDTO();
        post.setCategoryId(Long.valueOf(multi.getParameter("categoryId")));
        post.setAuthor(multi.getParameter("author"));
        post.setTitle(multi.getParameter("title"));
        post.setPasswd(multi.getParameter("passwd"));
        post.setSecondPasswd(multi.getParameter("secondPasswd"));
        post.setContent(multi.getParameter("content"));
        post.setCreatedDate(LocalDateTime.now());

        // 유효성 검사
        /*
        유효성 검사 실패시 http status를 ajax에게 반환하는 것은 성공하지만
        이후 ControllerServlet에서 실행되는 dispatcher.forward()를 그대로 실행하는 문제
        또한 forward()가 정상 실행되어 list.jsp를 호출했음에도 페이지가 전환되지 않고 화면에 유지되는 부분 의문
        -> js error alert가 화면에 올라왔을 때 dispatcher.forward()는 무시되는 것인지?
         */

        // TODO: 예외 상황의 경우 throw Exception을 활용하기
        if(!checkValidation(post)) {
            // 검사 실패시 400 set 하고 아래 로직을 스킵
            System.out.println("유효성 검사 실패");
            throw new RuntimeException();
        }

        // 패스워드 암호화
        post.setPasswd(getSHA512(post.getPasswd()));
        System.out.println(post.getPasswd());

        // 게시글 저장
        PostDAO postDAO = PostDAO.getInstance();

        Long postId = postDAO.savePost(post);
        System.out.println("savePost()로 리턴받은 포스트의 index값은 : " + postId);

        // 파일 저장
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

            // -> DB에 확장자와 크기를 집어넣어야 하는 이유? 이름이 같다면 자동으로 다른 이름으로 변환하기 때문에 다른 확장자의 파일을 반환할 일은 없음.
            contentType = multi.getContentType(element);    // 업로드된 파일의 타입을 반환
            length = multi.getFile(element).length(); // 파일의 크기를 반환 (long타입)

            FileDTO file = new FileDTO();
            file.setFileName(filesystemName);
            file.setFileRealName(originalFileName);
            file.setPostId(postId);

            fileLists.add(file);
        }

        if (fileLists.size() >= 1) { // files DB에 저장
            FileDAO fileDAO = FileDAO.getInstance();
            fileDAO.saveFiles(fileLists);
        }

        HashMap<String, String> resultCommandMap = new HashMap<>();
        resultCommandMap.put("command", "redirect");
        resultCommandMap.put("viewPath", "/boards/free/list");

        return resultCommandMap;
    }

    // 서버 사이드 유효성 검사
    boolean checkValidation(PostDTO post) {

        if (post.getCategoryId() == null) {
            return false;
        }

        int authorLength = post.getAuthor().length();
//        authorLength += 100; // error유도
        if (authorLength < 3 || authorLength >= 5) {
            return false;
        }

        if (!post.getPasswd().matches("^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$")) {
            return false;
        } else if (!post.getPasswd().equals(post.getSecondPasswd())) {
            return false;
        }

        int titleLength = post.getTitle().length();
        if (titleLength < 4 || titleLength >= 100) {
            return false;
        }

        int contentLength = post.getContent().length();
        if (contentLength < 4 || contentLength >= 2000) {
            return false;
        }

        return true;
    }


    private String getSHA512(String pw) {
        String encryptPw = null; //암호화된 비밀번호를 저장할 변수

        //1) 해시 함수를 수행할 객체를 참조할 변수 선언
        MessageDigest md = null;

        try {
            // 2) SHA-512 방식의 해시 함수를 수행할 수 있는 객체를 얻어옴
            md = MessageDigest.getInstance("SHA-512");

            // 3) md를 이용해 암호화를 진행할 수 있도록 pw를 byte[] 배열 형태로 변환
            byte[] bytes = pw.getBytes(Charset.forName("UTF-8"));
            //UTF-8 문자열인거 감안하고 변환해라라는 의미

            // 4) 암호화  -> 암호화 결과가 md 내부에 저장함.
            md.update(bytes);

            // 5) 암호화된 비밀번호를 encryptPw에 대입
            //digest가 보관하고 있다
            //byte[]배열을 String으로 변환할 필요가 있다.
            encryptPw = Base64.getEncoder().encodeToString(md.digest());
            //Base64 : byte 코드를 문자열로 변환하는 객체
            //base64 에서 변환할수 있게 해주는 객체를 가져와 변환한다

        } catch (NoSuchAlgorithmException e) {
            //SHA-512 해시 함수가 존재하지 않을 때 예외 발생
            e.printStackTrace();
        }
        return encryptPw;
    }
}
