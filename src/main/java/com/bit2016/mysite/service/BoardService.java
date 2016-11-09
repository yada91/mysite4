package com.bit2016.mysite.service;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2016.mysite.repository.BoardDAO;
import com.bit2016.mysite.vo.Board;
import com.bit2016.mysite.vo.Page;
import com.bit2016.mysite.vo.Users;

@Service
public class BoardService {

	@Autowired
	private BoardDAO boardDAO;

	final int LISTSIZE = 5;
	final int PAGESIZE = 5;

	public HashMap<String, Object> list(int p, String kwd) {
		Page page = new Page();
		page.setKwd(kwd);

		double total = boardDAO.count(page);
		int lastPage = (int) Math.ceil(total / (double) PAGESIZE);

		page.setListSize(LISTSIZE);
		page.setPageSize(PAGESIZE);
		page.setLastPage(lastPage);
		page.setKwd(kwd);
		int currentPage = p;
		page.setCurrentPage(currentPage);

		if ((currentPage / PAGESIZE) != 0) {
			if ((currentPage % PAGESIZE) != 0) {
				page.setStartPage(((currentPage / PAGESIZE) * 5) + 1);
			} else {
				page.setStartPage((currentPage) - PAGESIZE + 1);
			}
		} else {
			page.setStartPage(1);
		}
		List<Board> list = boardDAO.select(page);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("total", (int) total);
		hm.put("list", list);
		hm.put("page", page);

		return hm;

	}

	public HashMap<String, Object> view(Long no, int p) {
		Board board = boardDAO.view(no);
		board.setNo(no);
		boardDAO.updateHits(no);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("view", board);
		hm.put("p", p);
		return hm;
	}

	public Board view(Long no) {
		Board board = boardDAO.view(no);
		return board;
	}

	public void write(Board vo, Users authUser) {

		vo.setUserNo(authUser.getNo());
		boardDAO.insert(vo);
	}

	public void delete(Long no) {
		boardDAO.delete(no);
	}

	public void update(Board vo) {

		boardDAO.update(vo);

	}

	public void reply(Board vo, Users authUser, Long no) {

		vo.setUserNo(authUser.getNo());

		Board board = boardDAO.replyView(no);

		vo.setUserNo(authUser.getNo());
		vo.setGroupNo(board.getGroupNo());
		vo.setDepth(board.getDepth() + 1);
		vo.setOrderNo(board.getOrderNo() + 1);

		boardDAO.updateReply(vo);
		boardDAO.insertReply(vo);
	}
}
