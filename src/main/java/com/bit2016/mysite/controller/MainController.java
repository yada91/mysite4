package com.bit2016.mysite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	@RequestMapping("")
	public String main() {
		return "main/index";
	}

	@ResponseBody
	@RequestMapping("/hello")
	public String hello() {
		return "jrebel";
	}
}
