package com.bit2016.mysite.repository;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bit2016.mysite.vo.Board;
import com.bit2016.mysite.vo.Page;

@Repository
public class BoardDAO {

	@Autowired
	private SqlSession sqlSession;

	public void insert(Board vo) {
		sqlSession.insert("board.insert", vo);
	}

	public List<Board> select(Page page) {
		List<Board> list = sqlSession.selectList("board.selectByPage", page);
		return list;
	}

	public int count(Page page) {
		int countNum = sqlSession.selectOne("board.count", page);
		return countNum;
	}

	public Board view(Long no) {
		Board vo = sqlSession.selectOne("board.getView", no);
		return vo;
	}

	public Board replyView(Long no) {
		Board vo = sqlSession.selectOne("board.getReplyView", no);
		return vo;
	}

	public void update(Board vo) {
		sqlSession.update("board.update", vo);
	}

	public void updateHits(Long no) {
		sqlSession.update("board.updateHits", no);
	}

	public void updateReply(Board vo) {
		sqlSession.update("board.updateReply", vo);
	}

	public void insertReply(Board vo) {
		sqlSession.insert("board.insert", vo);
	}

	public void delete(Long no) {
		sqlSession.delete("board.delete", no);
	}

}
