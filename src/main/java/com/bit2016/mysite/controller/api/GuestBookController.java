package com.bit2016.mysite.controller.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bit2016.mysite.service.GuestBookService;
import com.bit2016.mysite.vo.GuestBook;

@Controller("guestbookAPIController")
@RequestMapping("/gb/api")
public class GuestBookController {

	@Autowired
	private GuestBookService guestBookService;

	@ResponseBody
	@RequestMapping("/list")
	public Object list(@RequestParam(value = "p", required = true, defaultValue = "1") Integer page) {

		Map<String, Object> map = new HashMap<String, Object>();
		List<GuestBook> list = guestBookService.selectByPage(page);

		map.put("result", "success");

		map.put("data", list);

		return map;

	}

	@ResponseBody
	@RequestMapping("/insert")
	public Object insert(@ModelAttribute GuestBook vo) {

		Map<String, Object> map = new HashMap<String, Object>();
		Long id = guestBookService.add(vo);

		map.put("result", "success");
		if (guestBookService.get(id) != null) {
			map.put("data", guestBookService.get(id));
		} else {
			map.put("data", "not exist");
		}
		return map;

	}

	@ResponseBody
	@RequestMapping("/delete")
	public Object delete(@ModelAttribute GuestBook vo) {

		boolean flag = guestBookService.delete(vo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", "success");
		if (flag) {
			map.put("data", "yes");
		} else {
			map.put("data", "no");
		}
		return map;

	}

}
