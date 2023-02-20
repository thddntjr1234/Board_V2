package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.repository.CommentDAO;
import com.ebstudy.board_v2.repository.FileDAO;
import com.ebstudy.board_v2.repository.PostDAO;
import com.ebstudy.board_v2.web.Command;
import com.ebstudy.board_v2.web.dto.CommentDTO;
import com.ebstudy.board_v2.web.dto.FileDTO;
import com.ebstudy.board_v2.web.dto.PostDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalLong;

public class LoadPostCommand implements Command {
    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, String> resultCommandMap = new HashMap<>();

        // TODO: 로그 기능 별도로 사용하기
        // null, 0 check
        String inputPostId = request.getParameter("postId");
        if ("0".equals(inputPostId) || "".equals(inputPostId) || inputPostId == null) {
            resultCommandMap.put("command", "redirect");
            resultCommandMap.put("viewPath", "/boards/free/list");
            return resultCommandMap;
        }

        Long postId = Long.valueOf(request.getParameter("postId"));
        System.out.println("postId: " + postId);

        // 게시글 로드
        PostDAO postDAO = PostDAO.getInstance();
        postDAO.increaseHits(postId);
        PostDTO post = postDAO.getPost(postId);

        // 게시글에 포함된 파일 로드
        FileDAO fileDAO = FileDAO.getInstance();
        List<FileDTO> files = fileDAO.getFileList(post.getPostId());

        // 게시글에 포함된 댓글 로드
        CommentDAO commentDAO = CommentDAO.getInstance();
        List<CommentDTO> comments = commentDAO.getCommentList(postId);

        // 모델에 데이터 입력
        request.setAttribute("post", post);
        request.setAttribute("files", files);
        request.setAttribute("comments", comments);

        resultCommandMap.put("command", "forward");
        resultCommandMap.put("viewPath", "/WEB-INF/boards/free/view.jsp");

        return resultCommandMap;
    }
}
