package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.DetailInfo;

@Component("detailInfoDao")
public class MySqlDetailInfoDao implements DetailInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
		
	@Override
	public List<DetailInfo> selectList(HashMap<String, Object> paramMap)
			throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList("com.quantosauros.manager.dao.DetailInfoDao.selectList", paramMap);
		} finally {
			sqlSession.close();
		}		
	}

	@Override
	public DetailInfo selectOne(HashMap<String, Object> paramMap) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectOne("com.quantosauros.manager.dao.DetailInfoDao.selectList", paramMap);
		} finally {
			sqlSession.close();
		}	
	}

	
}
