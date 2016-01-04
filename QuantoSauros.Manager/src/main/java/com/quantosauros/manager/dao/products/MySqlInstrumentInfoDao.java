package com.quantosauros.manager.dao.products;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.products.InstrumentInfo;

@Component("instrumentInfoDao")
public class MySqlInstrumentInfoDao implements InstrumentInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public List<InstrumentInfo> getLists() {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			return sqlSession.selectList("com.quantosauros.manager.dao.InstrumentInfo.selectInstrumentInfo");
		} finally {
			sqlSession.close();
		}
	}
	public InstrumentInfo getOne(String instrumentCd) {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {			
			HashMap<String , Object> paramMap = new HashMap<>();
			paramMap.put("instrumentCd", instrumentCd);
			return sqlSession.selectOne("com.quantosauros.manager.dao.InstrumentInfo.selectOneByInstrumentCd",
					paramMap);
		} finally {
			sqlSession.close();
		}
	}
}
