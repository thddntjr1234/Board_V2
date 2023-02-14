package com.ebstudy.board_v2.config;

import com.ebstudy.board_v2.repository.BoardMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class MybatisConnection {

    private static SqlSessionFactory instance;

    private MybatisConnection() {}

    public static SqlSessionFactory getInstance() throws IOException {

        // inputStreamReader는 한글을 읽지 못하므로 Reader 사용
        Reader reader = null;

        try {
            String resource = "/mybatis-config.xml";
            reader = Resources.getResourceAsReader(resource);
            instance = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            System.out.println("mybatis-config 설정 실패: " + e);
        } finally {
           try { if(reader != null) reader.close(); } catch (IOException e) {}
        }

        return instance;
    }
}
