package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.config.SessionFactory;
import com.ebstudy.board_v2.web.dto.CommentDTO;
import com.ebstudy.board_v2.web.dto.FileDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class CommentDAO {

    private static final CommentDAO commentDAO = new CommentDAO();

    public static CommentDAO getInstance() {
        return commentDAO;
    }

    private CommentDAO() {
    }

    public List<CommentDTO> getCommentList(long postId) {

        List<CommentDTO> comments = new LinkedList<>();

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            comments = mapper.getCommentList(postId);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;

    }

    public void saveComment(CommentDTO comment) {

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            mapper.saveComment(comment);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
