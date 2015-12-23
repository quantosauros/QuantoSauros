package com.quantosauros.batch.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
 
public class SqlMapClient {
    private static SqlSession session;
    
    static {
        try {
            String resource = "./mybatis/myBatisConfig.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder().build(reader);
             
            session = sqlMapper.openSession();
             System.out.println("CONNECTED");
        } catch (IOException e) {
            e.printStackTrace();
        }
         
    }
     
    public static SqlSession getSqlSession() {
        return session;
    }
}