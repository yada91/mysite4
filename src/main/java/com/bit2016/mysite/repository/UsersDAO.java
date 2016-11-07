package com.bit2016.mysite.repository;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.exception.UserDaoException;
import com.bit2016.mysite.vo.Users;

@Repository
public class UsersDAO {
	@Autowired
	private SqlSession sqlSession;

	public void insert(Users users) {
		sqlSession.insert("user.insert", users);
	}

	// 로그인시
	public Users selectNo(String email, String password) throws UserDaoException {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("email", email);
		map.put("password", password);
		Users users = sqlSession.selectOne("user.getByEmailAndPassword", map);
		return users;

	}

	// 업데이트시
	public Users selectNo(Long no) {
		Users users = sqlSession.selectOne("user.getByNo", no);
		return users;
	}

	public Users selectNo(String email) {
		Users users = sqlSession.selectOne("user.getByEmail", email);
		return users;
	}

	public void update(Users users) {
		sqlSession.update("user.update", users);
	}
}
