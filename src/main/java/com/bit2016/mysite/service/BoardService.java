package com.bit2016.mysite.service;

import java.util.HashMap;

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

	public HashMap<String, Object> list(int p) {
		double total = boardDAO.count();
		int lastPage = (int) Math.ceil(total / (double) PAGESIZE);

		Page page = new Page();
		page.setListSize(LISTSIZE);
		page.setPageSize(PAGESIZE);
		page.setLastPage(lastPage);

		int currentPage = p;
		page.setCurrentPage(currentPage);

		if ((currentPage / PAGESIZE) != 0) {
			if ((currentPage % PAGESIZE) != 0) {
				page.setStartPage(((currentPage / PAGESIZE) * 5) + 1);
			} else {
				page.setStartPage(1);
			}
		} else {
			page.setStartPage(1);
		}

		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("total", (int) total);
		hm.put("list", boardDAO.select(page));
		hm.put("page", page);

		return hm;

	}

	public HashMap<String, Object> view(Long no, int p) {
		Board board = boardDAO.view(no);
		boardDAO.updateHits(no);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("view", board);
		hm.put("p", p);
		return hm;
	}

	public void write(Board vo, HttpSession session) {

		Users authUser = (Users) session.getAttribute("authUser");
		vo.setUser_no(authUser.getNo());
		boardDAO.insert(vo);
	}

	public void delete(Long no) {
		boardDAO.delete(no);
	}

	public void update(Board vo) {

		boardDAO.update(vo);

	}

	public void reply(Board vo, HttpSession session, Long no) {

		Users authUser = (Users) session.getAttribute("authUser");
		vo.setUser_no(authUser.getNo());

		Board board = boardDAO.replyView(no);

		vo.setUser_no(authUser.getNo());
		vo.setGroup_no(board.getGroup_no());
		vo.setDepth(board.getDepth() + 1);
		vo.setOrder_no(board.getOrder_no() + 1);

		boardDAO.updateReply(vo);
		boardDAO.insertReply(vo);
	}
}
