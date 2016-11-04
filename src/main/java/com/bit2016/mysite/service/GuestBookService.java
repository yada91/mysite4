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

	public void add(GuestBook vo) {
		guestBookDAO.insert(vo);
	}

	public void delete(Long id) {
		guestBookDAO.delete(id);
	}

}
