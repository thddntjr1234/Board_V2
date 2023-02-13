package com.ebstudy.board_v2.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {

    private static MyConnection myConnection = new MyConnection();

    private MyConnection() {}

    public static MyConnection getInstance() {
        return myConnection;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mariadb://localhost:3306/board_v1";
        String userName = "root";
        String password = "0927";
        Connection conn = null;

        Class.forName("org.mariadb.jdbc.Driver");
        System.out.println("mariadb 드라이버 로딩 성공");
        conn = DriverManager.getConnection(url, userName, password);
        System.out.println("mariadb 연결 설공, connection 반환");
        return conn;
    }
}
