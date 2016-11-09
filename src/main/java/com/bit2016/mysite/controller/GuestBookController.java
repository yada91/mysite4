package com.bit2016.mysite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bit2016.mysite.repository.GuestBookDAO;
import com.bit2016.mysite.service.GuestBookService;
import com.bit2016.mysite.vo.GuestBook;
import com.bit2016.security.Auth;

@Controller
@RequestMapping("/gb")
public class GuestBookController {
	@Autowired
	private GuestBookService guestbookService;

	@Auth
	@RequestMapping("")
	public String list(Model model) {
		List<GuestBook> list = guestbookService.selectAll();
		model.addAttribute("list", list);
		model.addAttribute("br", "<br>");
		model.addAttribute("nl", "\r\n");
		return "guestbook/list";
	}

	@RequestMapping("/add")
	public String add(@ModelAttribute GuestBook vo) {
		guestbookService.add(vo);

		return "redirect:/gb";
	}

	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		guestbookService.delete(id);
		return "redirect:/gb";
	}

	@RequestMapping("/deleteform/{id}")
	public String deleteForm(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		return "guestbook/deleteform";
	}

	@RequestMapping("/ajax")
	public String ajaxList() {

		return "guestbook/list-ajax";
	}
}
