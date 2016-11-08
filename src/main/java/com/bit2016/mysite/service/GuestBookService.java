package com.bit2016.mysite.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2016.mysite.repository.GuestBookDAO;
import com.bit2016.mysite.vo.GuestBook;

@Service
public class GuestBookService {
	@Autowired
	private GuestBookDAO guestBookDAO;

	public List<GuestBook> selectAll() {
		List<GuestBook> list = guestBookDAO.selectAll();
		return list;
	}

	public Long add(GuestBook vo) {
		Long id = guestBookDAO.insert(vo);
		return id;
	}

	public void delete(Long id) {
		guestBookDAO.delete(id);
	}

	public boolean delete(GuestBook vo) {

		return guestBookDAO.delete(vo);
	}

	public List<GuestBook> selectByPage(int page) {
		List<GuestBook> list = guestBookDAO.selectByPage(page);
		return list;
	}

	public GuestBook get(Long id) {
		GuestBook vo = guestBookDAO.get(id);
		return vo;
	}

}
