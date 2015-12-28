package com.quantosauros.manager.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.DeltaInfo;

@Component("deltaInfoDao")
public class MySqlDeltaInfoDao implements DeltaInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public List<DeltaInfo> selectAllList(HashMap<String, Object> paramMap) throws Exception {
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(
					"com.quantosauros.manager.dao.DeltaInfoDao.selectAllList", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
	
	public List<DeltaInfo> selectDeltaForChart(HashMap<String, Object> paramMap) throws Exception{
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(
					"com.quantosauros.manager.dao.DeltaInfoDao.selectDeltaForChart", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	
	public List<DeltaInfo> selectDeltaForChart2(HashMap<String, Object> paramMap) throws Exception{
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(
					"com.quantosauros.manager.dao.DeltaInfoDao.selectDeltaForChart2", paramMap);
		} finally {
			sqlSession.close();
		}
	}
	
	public List<String> selectMrtyCd(HashMap<String, Object> paramMap) throws Exception{
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(
					"com.quantosauros.manager.dao.DeltaInfoDao.selectMrtyCd", paramMap);
		} finally {
			sqlSession.close();
		}
	}
}
