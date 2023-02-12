package com.ebstudy.board_v2.File;


import com.ebstudy.board_v2.Connection.MyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAO {
    private static FileDAO fileDAO = new FileDAO();

    MyConnection myConnection = MyConnection.getInstance();

    private Connection conn;
    private PreparedStatement pstmt;
    private ResultSet rs;

    private FileDAO() {}

    public static FileDAO getInstance() { return fileDAO; }

    public void saveFiles(Long postId, List<FileDTO> fileLists) throws SQLException, ClassNotFoundException {
        conn = myConnection.getConnection();
        PreparedStatement pstmt;

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
    }

    public List<FileDTO> getFileNames(long postid) throws SQLException, ClassNotFoundException {
        conn = myConnection.getConnection();
        List<FileDTO> files = new ArrayList<>();

        pstmt = conn.prepareStatement("SELECT * FROM files WHERE id = ?");
        pstmt.setLong(1, postid);
        rs = pstmt.executeQuery();;

        while (rs.next()) {
            FileDTO file = FileDTO.builder()
                    .fileName(rs.getString("fileName"))
                    .fileRealName(rs.getString("fileRealName"))
                    .build();

            files.add(file);
        }

        return files;
    }

}
