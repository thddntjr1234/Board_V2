package com.ebstudy.board_v2.Post;


import com.ebstudy.board_v2.Connection.MyConnection;
import lombok.Getter;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
public class PostDAO {

    private static final PostDAO postDAO = new PostDAO();
    private MyConnection myConnection = MyConnection.getInstance();

    // TODO: 싱글톤으로 사용할 떄 상태를 공유하는 변수들이 없도록 고려
    // key.equals("value")와 "value".equals(key)의 차이는
    // null이 일을 하게 하는지의 차이다.

    private PostDAO() {
    }

    public static PostDAO getInstance() {
        return postDAO;
    }

    /**
     * 목록 조회
     * @param pageNumber: 현재 페이지의 값만큼 뒤에서부터 게시글을 SELECT
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<PostDTO> getPostList(int pageNumber) throws SQLException, ClassNotFoundException {
        // TODO: conn, pstmt, rs는 지역변수들로 선언
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        List<PostDTO> posts = new LinkedList<>();

        conn = myConnection.getConnection();
        pstmt = conn.prepareStatement("SELECT * FROM posts ORDER BY id DESC LIMIT ?, 10");
        pstmt.setInt(1, (pageNumber - 1) * 10);
        rs = pstmt.executeQuery();

        // TODO: try-catch-finally
        while (rs.next()) {
            System.out.println("쿼리 실행후 값 가져오기");
            Long id = rs.getLong("id");
            String category = rs.getString("category");
            String title = rs.getString("title");
            String author = rs.getString("author");
            LocalDateTime createdDate = rs.getTimestamp("created_date").toLocalDateTime();
            Boolean isHaveFile = rs.getBoolean("file_flag");
            LocalDateTime modifiedDate = null;

            if (rs.getTimestamp("modified_date") != null) {
                modifiedDate = rs.getTimestamp("modified_date").toLocalDateTime();
            }
            Long hits = rs.getLong("hits");

            PostDTO postDto = PostDTO.builder()
                    .id(id)
                    .category(category)
                    .title(title)
                    .author(author)
                    .createdDate(createdDate)
                    .modifiedDate(modifiedDate)
                    .isHaveFile(isHaveFile)
                    .hits(hits).build();
            posts.add(postDto);
        }
        // 요청이 많아지면 close()가 없어서 오류가 발생하는 것 같다. try-catch-finally로 conn, pstmt, rs close() 리팩토링하기(모든 메소드)

        return posts;
    }

    public int getPostCount() throws SQLException, ClassNotFoundException {
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        conn = myConnection.getConnection();
        pstmt = conn.prepareStatement("SELECT COUNT(id) AS Count FROM posts");
        rs = pstmt.executeQuery();
        int count = 0;
        if (rs.next()) {
            count = rs.getInt("Count");
        }
        return count;
    }

    // view.jsp
    public PostDTO getPost(Long postId) throws SQLException, ClassNotFoundException {
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        conn = myConnection.getConnection();
        pstmt = conn.prepareStatement("SELECT * FROM posts WHERE id = ?");
        pstmt.setLong(1, postId);
        rs = pstmt.executeQuery();

        PostDTO postDTO = PostDTO.builder().build();
        if (rs.next()) {
            LocalDateTime modifiedDate = null;
            if (rs.getTimestamp("modified_date") != null) {
                modifiedDate = rs.getTimestamp("modified_date").toLocalDateTime();
            }
            postDTO = PostDTO.builder()
                    .id(rs.getLong("id"))
                    .category(rs.getString("category"))
                    .author(rs.getString("author"))
                    .title(rs.getString("title"))
                    .content(rs.getString("content"))
                    .passwd(rs.getString("passwd"))
                    .hits(rs.getLong("hits"))
                    .isHaveFile(rs.getBoolean("file_flag"))
                    .createdDate(rs.getTimestamp("created_date").toLocalDateTime())
                    .modifiedDate(modifiedDate)
                    .build();
        }

        return postDTO;
    }

    // write.jsp, writeAction.jsp
    public Long savePost(PostDTO postDTO) throws SQLException, ClassNotFoundException {

        // TODO: 단방향 암호화 구현하기
        long id = 0L;
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        conn = myConnection.getConnection();

        // String sql = "CREATE
//                            FROM
//                            WHERE 방식으로 들여쓰기 하기
        pstmt = conn.prepareStatement("INSERT INTO posts(category, author, passwd, title, content, created_date) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, postDTO.getCategory());
        pstmt.setString(2, postDTO.getAuthor());
        pstmt.setString(3, postDTO.getPasswd());
        pstmt.setString(4, postDTO.getTitle());
        pstmt.setString(5, postDTO.getContent());
        pstmt.setTimestamp(6, Timestamp.from(Instant.now()));

        // 반영된 레코드 건수를 반환
        int result = pstmt.executeUpdate();
        System.out.println("result: " + result);

        rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            id = rs.getLong(1);
        }
        return id;
    }

    // 미구현
    public void updatePost(PostDTO postDTO) {
        Long id = 0L;
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE posts SET ");

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (Exception e) {}
        }
    }

    public void deletePost(PostDTO postDTO) {

    }

    public void increaseHits(Long postId) {
        PreparedStatement pstmt = null;
        Connection conn = null;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE posts SET hits = (SELECT hits FROM posts WHERE id = ?) + 1 WHERE id = ?;");
            pstmt.setLong(1, postId);
            pstmt.setLong(2, postId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {
                conn.close();
                pstmt.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    // list.jsp
    public List<PostDTO> getCategoryList() throws SQLException, ClassNotFoundException {
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        conn = myConnection.getConnection();

        pstmt = conn.prepareStatement("select * from category");
        rs = pstmt.executeQuery();
        List<PostDTO> posts = new LinkedList<>();

        while (rs.next()) {
            String category = rs.getString("category");

            PostDTO postDto = PostDTO.builder()
                    .category(category).build();

            posts.add(postDto);
        }
        return posts;
    }
}
