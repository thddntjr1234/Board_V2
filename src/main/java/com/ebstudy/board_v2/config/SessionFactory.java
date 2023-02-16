package com.ebstudy.board_v2.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;

public class SessionFactory {

    private static SqlSessionFactory sessionFactory = null;

    private SessionFactory() {}

    public static SqlSessionFactory getSessionFactory() {

        // inputStreamReader는 한글을 읽지 못하므로 Reader 사용
        Reader reader = null;

        try {
            String resource = "/mybatis-config.xml";

            reader = Resources.getResourceAsReader(resource);
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);

        } catch (Exception e) {
            System.out.println("mybatis-config 설정 실패: " + e);
        } finally {
           try {
               if(reader != null) reader.close();
           } catch (IOException e) {}
        }

        return sessionFactory;
    }
}
