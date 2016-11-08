package com.bit2016.mysite.controller;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2016.mysite.service.BoardService;
import com.bit2016.mysite.vo.Board;
import com.bit2016.mysite.vo.Users;

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String list(@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		HashMap<String, Object> hm = boardService.list(p, kwd);
		model.addAttribute("hm", hm);
		return "board/list";
	}

	@RequestMapping("/view")
	public String view(@RequestParam(value = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		HashMap<String, Object> hm = boardService.view(no, p);
		model.addAttribute("hm", hm);
		model.addAttribute("kwd", kwd);
		return "board/view";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@ModelAttribute Board vo, @RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, HttpSession session,
			Model model) {
		boardService.write(vo, session);
		return "redirect:/board?p=" + p + "&kwd=" + kwd;

	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		model.addAttribute("p", p);
		model.addAttribute("kwd", kwd);
		return "board/write";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, HttpSession session) {
		Users authUser = (Users) session.getAttribute("authUser");

		Board board = boardService.view(no);
		if (authUser != null) {
			if (board.getUserNo() == authUser.getNo()) {
				boardService.delete(no);
			} else {
				return "redirect:/user/loginform";
			}
		} else {
			return "redirect:/user/loginform";
		}

		return "redirect:/board?p=" + p + "&kwd=" + 1;
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		HashMap<String, Object> hm = boardService.view(no, p);
		model.addAttribute("hm", hm);
		model.addAttribute("kwd", kwd);
		return "board/modify";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Board vo,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, HttpSession session) {
		Users authUser = (Users) session.getAttribute("authUser");
		Board board = boardService.view(no);
		if (authUser != null) {
			if (board.getUserNo() == authUser.getNo()) {
				boardService.update(vo);
			} else {
				return "redirect:/user/loginform";
			}
		} else {
			return "redirect:/user/loginform";
		}

		return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;

	}

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(@RequestParam(value = "rno", required = true, defaultValue = "0") Long rno,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		model.addAttribute("no", rno);
		model.addAttribute("p", p);
		model.addAttribute("kwd", kwd);
		return "board/reply";
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(@RequestParam(value = "rno", required = true, defaultValue = "0") Long rno,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @ModelAttribute Board vo,
			HttpSession session, Model model) {
		boardService.reply(vo, session, rno);
		return "redirect:/board?p=" + p + "&kwd=" + kwd;
	}
}
