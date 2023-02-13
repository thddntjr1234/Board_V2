package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.config.MyConnection;
import com.ebstudy.board_v2.web.dto.PostDTO;
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
     *
     * @param pageNumber: 현재 페이지의 값
     * @throws SQLException
     * @throws ClassNotFoundException
     * @return: pageNumber * 10 이후의 게시글부터 10개를 가져온다
     */
    public List<PostDTO> getPostList(int pageNumber) {
        // TODO: conn, pstmt, rs는 지역변수들로 선언
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<PostDTO> posts = new LinkedList<>();

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM posts ORDER BY id DESC LIMIT ?, 10");
            pstmt.setInt(1, (pageNumber - 1) * 10);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("쿼리 실행후 값 가져오기");
                LocalDateTime modifiedDate = null;

                if (rs.getTimestamp("modified_date") != null) {
                    modifiedDate = rs.getTimestamp("modified_date").toLocalDateTime();
                }
                Long hits = rs.getLong("hits");

                PostDTO postDto = PostDTO.builder()
                        .id(rs.getLong("id"))
                        .category(rs.getString("category"))
                        .title(rs.getString("title"))
                        .author(rs.getString("author"))
                        .createdDate(rs.getTimestamp("created_date").toLocalDateTime())
                        .modifiedDate(modifiedDate)
                        .isHaveFile(rs.getBoolean("file_flag"))
                        .hits(rs.getLong("hits"))
                        .build();
                posts.add(postDto);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }

        // 요청이 많아지면 close()가 없어서 오류가 발생하는 것 같다. try-catch-finally로 conn, pstmt, rs close() 리팩토링하기(모든 메소드)

        return posts;
    }

    public int getPostCount() throws SQLException, ClassNotFoundException {
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(id) AS Count FROM posts");
            rs = pstmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("Count");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return count;
    }

    // view.jsp
    public PostDTO getPost(Long postId) throws SQLException, ClassNotFoundException {

        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;
        PostDTO postDTO = null;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM posts WHERE id = ?");
            pstmt.setLong(1, postId);
            rs = pstmt.executeQuery();

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return postDTO;
    }

    /**
     * 게시글 저장 메소드
     * @param postDTO: 게시글 입력 정보
     * @return: 테이블에 저장했을 때 기본키 값을 반환함
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Long savePost(PostDTO postDTO) throws SQLException, ClassNotFoundException {

        // TODO: 단방향 암호화 구현하기
        long id = 0L;
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        // TODO : sql을 String으로 따로 분리하기
        try {
            conn = myConnection.getConnection();

            pstmt = conn.prepareStatement("INSERT INTO posts(category, author, passwd, title, content, created_date) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, postDTO.getCategory());
            pstmt.setString(2, postDTO.getAuthor());
            pstmt.setString(3, postDTO.getPasswd());
            pstmt.setString(4, postDTO.getTitle());
            pstmt.setString(5, postDTO.getContent());
            pstmt.setTimestamp(6, Timestamp.from(Instant.now()));

            pstmt.executeUpdate();
            rs = pstmt.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getLong(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
        return id;
    }

    // 미구현
    public void updatePost(PostDTO postDTO) {
        Long id = 0L;
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = myConnection.getConnection();
            pstmt = conn.prepareStatement("UPDATE posts SET ");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    public void deletePost(PostDTO postDTO) {

    }

    /**
     * 조회수 증가 메소드
     *
     * @param postId = 조회수를 증가할 post의 pk값
     */
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
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 카테고리 리스트를 반환하는 메소드
     * @throws SQLException
     * @throws ClassNotFoundException
     * @return: 카테고리 리스트
     */
    public List<String> getCategoryList() throws SQLException, ClassNotFoundException {
        // TODO: 카테고리 리스트를 반환할 때 postDTO에 담는 것이 아니라 좀 더 명시적으로 반환해야 한다 = List<String>
        PreparedStatement pstmt = null;
        Connection conn = null;
        ResultSet rs = null;

        List<String> posts = new LinkedList<>();

        try {
            conn = myConnection.getConnection();

            pstmt = conn.prepareStatement("select * from category");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                String category = rs.getString("category");
                posts.add(category);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);

        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception e) {
            }
            if (pstmt != null) try {
                pstmt.close();
            } catch (Exception e) {
            }
            if (conn != null) try {
                conn.close();
            } catch (Exception e) {
            }
        }

        return posts;
    }
}
