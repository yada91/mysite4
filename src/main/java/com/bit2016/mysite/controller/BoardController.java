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

@Controller
@RequestMapping("/board")
public class BoardController {
	@Autowired
	private BoardService boardService;

	@RequestMapping("")
	public String list(@RequestParam(value = "p", required = true, defaultValue = "1") int p, Model model) {
		HashMap<String, Object> hm = boardService.list(p);

		model.addAttribute("hm", hm);
		return "board/list";
	}

	@RequestMapping("/view")
	public String view(@RequestParam(value = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p, Model model) {
		HashMap<String, Object> hm = boardService.view(no, p);
		model.addAttribute("hm", hm);
		model.addAttribute("p", hm.get("p"));
		return "board/view";
	}

	@RequestMapping(value = "/write", method = RequestMethod.POST)
	public String write(@ModelAttribute Board vo, @RequestParam(value = "p", required = true, defaultValue = "1") int p,
			HttpSession session, Model model) {
		boardService.write(vo, session);
		return "redirect:/board?p=" + p;

	}

	@RequestMapping(value = "/write", method = RequestMethod.GET)
	public String write(@RequestParam(value = "p", required = true, defaultValue = "1") int p, Model model) {
		model.addAttribute("p", p);
		return "board/write";
	}

	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p) {
		boardService.delete(no);
		return "redirect:/board?p=" + p;
	}

	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(@RequestParam(value = "no", required = true, defaultValue = "0") Long no,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p, Model model) {
		HashMap<String, Object> hm = boardService.view(no, p);
		model.addAttribute("hm", hm);
		model.addAttribute("p", hm.get("p"));
		return "board/modify";
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modify(@ModelAttribute Board vo,
			@RequestParam(value = "p", required = true, defaultValue = "1") int p,
			@RequestParam(value = "no", required = true, defaultValue = "1") int no) {
		boardService.update(vo);
		return "redirect:/board/view?no=" + no + "&p=" + p;
	}
}
