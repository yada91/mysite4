package com.bit2016.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.GuestBook;

@Repository
public class GuestBookDAO {
	@Autowired
	private SqlSession sqlSession;

	public Long insert(GuestBook vo) {
		sqlSession.insert("guestbook.insert", vo);
		return null;

	}

	public List<GuestBook> selectAll() {
		List<GuestBook> list = sqlSession.selectList("guestbook.getList");
		return list;
	}

	public GuestBook get(Long id) {
		GuestBook vo = sqlSession.selectOne("guestbook.getListbyId", id);
		return vo;
	}

	public boolean delete(GuestBook vo) {
		sqlSession.delete("guestbook.delete", vo);
		return false;
	}

	public void delete(Long id) {
		sqlSession.delete("guestbook.deleteById", id);
	}
}
