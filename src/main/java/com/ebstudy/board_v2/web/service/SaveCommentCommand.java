package com.ebstudy.board_v2.web.service;

import com.ebstudy.board_v2.repository.CommentDAO;
import com.ebstudy.board_v2.repository.PostDAO;
import com.ebstudy.board_v2.web.Command;
import com.ebstudy.board_v2.web.dto.CategoryDTO;
import com.ebstudy.board_v2.web.dto.CommentDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class SaveCommentCommand implements Command {
    @Override
    public HashMap<String, String> service(HttpServletRequest request, HttpServletResponse response) {

        HashMap<String, String> resultCommandMap = new HashMap<>();

        Long postId = Long.valueOf(request.getParameter("postId"));
        String comment = request.getParameter("comment");

        // DTO에 파라미터 받아서 build
        CommentDTO commentDto = new CommentDTO();
        commentDto.setComment(comment);
        commentDto.setPostId(postId);
        commentDto.setCreatedDate(LocalDateTime.now());
        System.out.println("입력된 게시글 정보는 - " + commentDto.toString());

        CommentDAO commentDAO = CommentDAO.getInstance();
        commentDAO.saveComment(commentDto);
        System.out.println("댓글 저장 완료");

        resultCommandMap.put("command", "redirect");
        resultCommandMap.put("viewPath", "/boards/free/view?postId=" + postId);

        return resultCommandMap;
    }
}
