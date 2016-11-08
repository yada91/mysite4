package com.bit2016.mysite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bit2016.mysite.exception.UserDaoException;
import com.bit2016.mysite.repository.UsersDAO;
import com.bit2016.mysite.vo.Users;

@Service
public class UserService {
	@Autowired
	private UsersDAO userDao;

	public void join(Users vo) {
		userDao.insert(vo);
	}

	public Users login(String email, String password) {
		Users userVo = null;

		userVo = userDao.selectNo(email, password);

		return userVo;
	}

	public Users getUser(Long no) {
		Users userVo = userDao.selectNo(no);

		return userVo;
	}

	public void modify(Users vo) {
		userDao.update(vo);
	}

	public boolean emailExists(String email) {

		return (userDao.selectNo(email) != null);

	}
}
