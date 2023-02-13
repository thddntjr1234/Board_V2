package com.ebstudy.board_v2.repository;


import com.ebstudy.board_v2.config.MyConnection;
import com.ebstudy.board_v2.web.dto.FileDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private static FileDAO fileDAO = new FileDAO();

    MyConnection myConnection = MyConnection.getInstance();

    private FileDAO() {}

    public static FileDAO getInstance() { return fileDAO; }

    /**
     * 게시글 저장 시 입력받은 파일들을 File 디렉토리에 저장
     * @param postId: 입력한 게시글의 인덱스 값
     * @param fileLists: 게시글에 등록한 파일의 리스트
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void saveFiles(Long postId, List<FileDTO> fileLists) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = myConnection.getConnection();

            for (FileDTO fileDTO : fileLists) {
                pstmt = conn.prepareStatement("INSERT INTO files VALUES (?, ?, ?)");
                pstmt.setLong(1, postId);
                pstmt.setString(2, fileDTO.getFileName());
                pstmt.setString(3, fileDTO.getFileRealName());
                pstmt.executeUpdate();
            }
            pstmt = conn.prepareStatement("UPDATE posts SET file_flag = 1 WHERE id = ?");
            pstmt.setLong(1, postId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt != null) try {pstmt.close();} catch (Exception e) {}
            if (conn != null) try {conn.close();} catch (Exception e) {}
        }
    }

    /**
     * 데이터베이스 상에 저장된 파일의 이름과 실제 이름을 dto로 반환
     * @param postid: 가져올 파일의 게시글 인덱스
     * @return: 파일의 이름과 실제 이름 리스트
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<FileDTO> getFileNames(long postid) throws SQLException, ClassNotFoundException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<FileDTO> files = new ArrayList<>();

        try {
            conn = myConnection.getConnection();

            pstmt = conn.prepareStatement("SELECT * FROM files WHERE id = ?");
            pstmt.setLong(1, postid);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                FileDTO file = FileDTO.builder()
                        .fileName(rs.getString("fileName"))
                        .fileRealName(rs.getString("fileRealName"))
                        .build();

                files.add(file);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rs != null) try {rs.close();} catch (Exception e) {}
            if (pstmt != null) try {pstmt.close();} catch (Exception e) {}
            if (conn != null) try {conn.close();} catch (Exception e) {}
        }
        return files;
    }

}
