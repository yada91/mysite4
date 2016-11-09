package com.bit2016.mysite.controller;

import java.util.HashMap;

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
import com.bit2016.security.Auth;
import com.bit2016.security.AuthUser;

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

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@ModelAttribute Board vo, @RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @AuthUser Users authUser,
			Model model) {
		boardService.write(vo, authUser);
		return "redirect:/board?p=" + p + "&kwd=" + kwd;

	}

	@Auth
	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		model.addAttribute("p", p);
		model.addAttribute("kwd", kwd);
		return "board/write";
	}

	@Auth
	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @AuthUser Users authUser) {
		Board board = boardService.view(no);
		if (authUser != null && board != null) {
			if (board.getUserNo() == authUser.getNo()) {
				boardService.delete(no);
			} else {
				return "redirect:/board?p=" + p;
			}
		} else if (authUser != null) {
			return "redirect:/board?p=" + p;
		} else {
			return "redirect:/board?p=" + p;
		}
		return "redirect:/board?p=" + p + "&kwd=" + "";
	}

	@Auth
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @AuthUser Users authUser,
			Model model) {
		Board board = boardService.view(no);
		if (authUser != null && board != null) {
			if (board.getUserNo() == authUser.getNo()) {
				HashMap<String, Object> hm = boardService.view(no, p);
				model.addAttribute("hm", hm);
				model.addAttribute("kwd", kwd);
				return "board/modify";
			} else {
				return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;
			}
		} else if (authUser != null) {
			return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;
		} else {
			return "redirect:/loginform";
		}

	}

	@Auth
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Board vo,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "no", required = true, defaultValue = "1") Long no,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @AuthUser Users authUser) {
		Board board = boardService.view(no);
		if (authUser != null && board != null) {
			if (board.getUserNo() == authUser.getNo()) {
				boardService.update(vo);
			} else {
				return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;
			}
		} else if (authUser != null) {
			return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;
		} else {
			return "redirect:/loginform";
		}

		return "redirect:/board/view?no=" + no + "&p=" + p + "&kwd=" + kwd;

	}

	@Auth
	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public String reply(@RequestParam(value = "rno", required = true, defaultValue = "0") Long rno,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, Model model) {
		model.addAttribute("no", rno);
		model.addAttribute("p", p);
		model.addAttribute("kwd", kwd);
		return "board/reply";
	}

	@Auth
	@RequestMapping(value = "/reply", method = RequestMethod.POST)
	public String reply(@RequestParam(value = "rno", required = true, defaultValue = "0") Long rno,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "kwd", required = true, defaultValue = "") String kwd, @ModelAttribute Board vo,
			@AuthUser Users authUser, Model model) {
		boardService.reply(vo, authUser, rno);
		return "redirect:/board?p=" + p + "&kwd=" + kwd;
	}
}
