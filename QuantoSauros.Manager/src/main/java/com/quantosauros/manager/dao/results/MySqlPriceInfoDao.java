package com.quantosauros.manager.dao.results;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.quantosauros.manager.model.results.PriceInfo;

@Component("priceInfoDao")
public class MySqlPriceInfoDao implements PriceInfoDao {
	SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
	@Override
	public List<PriceInfo> selectAllList(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList(
					"com.quantosauros.manager.dao.PriceInfoDao.selectAllList", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
	
	@Override
	public List<PriceInfo> selectListForChart(HashMap<String, Object> paramMap){
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
			return sqlSession.selectList("com.quantosauros.manager.dao.PriceInfoDao.selectListForChart", paramMap);
		} finally {
			sqlSession.close();
		}		
	}
}
