package com.bit2016.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2016.mysite.service.UserService;
import com.bit2016.mysite.vo.Users;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping("/joinform")
	public String joinForm() {
		return "user/joinform";
	}

	@RequestMapping("/join")
	public String join(@ModelAttribute Users vo) {
		userService.join(vo);
		return "redirect:/user/joinsuccess";
	}

	@RequestMapping("/loginform")
	public String loginForm() {
		return "user/loginform";
	}

	@RequestMapping("/joinsuccess")
	public String joinSuccess() {
		return "user/joinsuccess";
	}

	@RequestMapping("/login")
	public String login(@RequestParam(value = "email", required = true, defaultValue = "") String email,
			@RequestParam(value = "password", required = true, defaultValue = "") String password,
			HttpSession session) {
		Users users = userService.login(email, password);
		if (users == null) {
			return "redirect:/user/loginform?result=fail";
		}
		// 성공
		session.setAttribute("authUser", users);

		return "redirect:/";
	}

	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	@RequestMapping("/modifyform")
	public String modifyForm(HttpSession session, Model model) {
		Users authUser = (Users) session.getAttribute("authUser");
		// 접근제한
		if (authUser == null) {
			return "redirect:/user/loginform";
		}
		Users users = userService.getUser(authUser.getNo());
		model.addAttribute("users", users);
		return "user/modifyform";
	}

	@RequestMapping("/modify")
	public String modify(HttpSession session, @ModelAttribute Users vo) {
		Users authUser = (Users) session.getAttribute("authUser");
		// 접근제한
		if (authUser == null) {
			return "redirect:/user/loginform";
		}
		vo.setNo(authUser.getNo());
		authUser.setName(vo.getName());
		session.setAttribute("authUser", authUser);
		userService.modify(vo);
		return "redirect:/user/modifyform?result=success";
	}

}
