package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.web.dto.CommentDTO;
import com.ebstudy.board_v2.config.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

public class CommentDAO {

    private static final CommentDAO commentDAO = new CommentDAO();

    private MyConnection myConnection = MyConnection.getInstance();

    public static CommentDAO getInstance() {
        return commentDAO;
    }

    private CommentDAO() {
    }

    public List<CommentDTO> getCommentList(Long postId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<CommentDTO> comments = new LinkedList<>();
        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT post_id, comment, created_date FROM comments WHERE post_id = ? ORDER BY created_date DESC");
            pstmt.setLong(1, postId);
            rs = pstmt.executeQuery();
            System.out.println("getCommentList 쿼리 정상 동작");
            while (rs.next()) {
                CommentDTO comment = CommentDTO.builder()
                        .postId(rs.getLong("post_id"))
                        .comment(rs.getString("comment"))
                        .createdDate(rs.getTimestamp("created_date").toLocalDateTime())
                        .build();

                comments.add(comment);
            }
        } catch (Exception e) {
            System.out.println("getcommentList에서 오류 발생: " + e);
        } finally {
            // conn은 DB Connection이 변경될 일이 없으므로 close하지 않고 유지
            if (rs != null) try { rs.close(); } catch (Exception e) {}
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
        }

        return comments;
    }

    public void saveComment(CommentDTO commentDTO) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO comments(post_id, comment, created_date) VALUES (?, ?, ?)");
            pstmt.setLong(1, commentDTO.getPostId());
            pstmt.setString(2, commentDTO.getComment());
            pstmt.setTimestamp(3, Timestamp.from(Instant.now()));

            pstmt.executeUpdate();
            System.out.println("댓글 삽입 완료");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
        }
    }
}
