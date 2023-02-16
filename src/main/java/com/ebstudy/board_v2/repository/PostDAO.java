package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.config.SessionFactory;
import com.ebstudy.board_v2.web.dto.CategoryDTO;
import com.ebstudy.board_v2.web.dto.PostDTO;
import lombok.Getter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.LinkedList;
import java.util.List;

@Getter
public class PostDAO {

    private static final PostDAO postDAO = new PostDAO();

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
     * @param pageNumber: 입력받은 요청 페이지의 값
     * @return pageNumber * 10 이후의 게시글부터 10개를 가져온다
     */
    public List<PostDTO> getPostList(int pageNumber) {

        List<PostDTO> posts = new LinkedList<>();

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            pageNumber = (pageNumber - 1) * 10;
            posts = mapper.getPostList(pageNumber);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        for(PostDTO post : posts) {
            System.out.println(post.toString());
        }
        return posts;
    }

    /**
     * 현재 DB에 존재하는 게시글의 총 개수를 구하는 메소드
     * @return 게시글의 총 개수
     */
    public int getPostCount() {

        int count = 0;

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            count =  mapper.getPostCount();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 게시글 정보를 가져오는 메소드
     * @param postId 가져올 게시글의 PK값
     * @return DB로부터 가져온 게시글(PostDTO)
     */
    public PostDTO getPost(long postId) {

       PostDTO post = new PostDTO();

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            post = mapper.getPost(postId);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return post;
    }

    /**
     * 게시글 저장 메소드
     * @param post: 게시글 입력 정보
     * @return 테이블에 저장했을 때 자동 증가한 기본키 값을 반환함
     */
    public long savePost(PostDTO post) {

        // TODO: 단방향 암호화 구현하기
        long id = 0L;

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            id = mapper.savePost(post);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    // 미구현
    public void updatePost(PostDTO postDTO) {
        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);
            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePost(PostDTO postDTO) {

    }

    /**
     * 조회수 증가 메소드
     *
     * @param postId = 조회수를 증가할 post의 pk값
     */
    public void increaseHits(long postId) {

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            mapper.increaseHits(postId);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 카테고리 리스트를 반환하는 메소드
     * @return 카테고리 리스트
     */
    public List<CategoryDTO> getCategoryList() {

        List<CategoryDTO> categoryList = new LinkedList<>();

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            categoryList = mapper.getCategoryList();
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryList;
    }
}
