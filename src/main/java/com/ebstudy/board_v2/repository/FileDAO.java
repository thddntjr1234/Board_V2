package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.config.SessionFactory;
import com.ebstudy.board_v2.web.dto.FileDTO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private static FileDAO fileDAO = new FileDAO();

    private FileDAO() {}

    public static FileDAO getInstance() { return fileDAO; }

    /**
     * 게시글 저장 시 입력받은 파일들을 files 디렉토리에 저장
     * @param fileLists: 게시글에 등록한 파일(FileDTO)의 리스트
     */
    public void saveFiles(List<FileDTO> fileLists) {

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            for (FileDTO file : fileLists) {
                mapper.saveFile(file);
            }
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 데이터베이스 상에 저장된 파일의 이름과 실제 이름을 dto로 반환
     * @param postid: 가져올 파일의 게시글 인덱스
     * @return 게시글 id값을 포함한 파일의 이름과 실제 이름 리스트
     */
    public List<FileDTO> getFileList(long postid) {

        List<FileDTO> files = new ArrayList<>();

        try {
            SqlSessionFactory sessionFactory = SessionFactory.getSessionFactory();
            SqlSession session = sessionFactory.openSession(true);
            BoardMapper mapper = session.getMapper(BoardMapper.class);

            files = mapper.getFileList(postid);
            session.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;
    }

}
