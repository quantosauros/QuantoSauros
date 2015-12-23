package com.quantosauros.batch.mybatis;

import java.io.IOException;
import java.io.Reader;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlMapClient {    
    private static SqlSessionFactory sessionFactory;
    
    static {
    	try {
    		String resource = "./mybatis/myBatisConfig.xml";
            Reader reader = Resources.getResourceAsReader(resource);
            sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
    public static SqlSessionFactory getSqlSessionFactory() {
        return sessionFactory;
    }
}