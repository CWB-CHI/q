package com.chi.question.controller;


import com.chi.question.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;


	@GetMapping({"/login", "/register"})
	public String login(Model model, @RequestParam(required = false) String next) {
		if (StringUtils.isNotBlank(next))
			model.addAttribute("next", next);
		return "login";
	}

	@PostMapping("/doRegister")
	public String doRegister(Model model, @RequestParam("username") String username,
							 @RequestParam("password") String password,
							 @RequestParam(value = "rememberme", defaultValue = "false", required = false) boolean rememberme,
							 @RequestParam(value = "next", defaultValue = "/", required = false) String next,
							 HttpServletResponse response) {
		Map<String, String> map = userService.register(username, password);
		if (map.containsKey("msg")) {
			model.addAttribute("msg", map.get("msg"));
			return "login";
		}
		addTicketCookie(map.get("ticket"), response, rememberme);
		return "redirect:" + next;
	}


	@PostMapping("/doLogin")
	public String doLogin(Model model, @RequestParam("username") String username,
						  @RequestParam("password") String password,
						  @RequestParam(value = "rememberme", defaultValue = "false", required = false) boolean rememberme,
						  @RequestParam(value = "next", defaultValue = "/", required = false) String next,
						  HttpServletResponse response) {
		Map<String, String> map = userService.login(username, password);
		if (map.containsKey("msg")) {
			model.addAttribute("msg", map.get("msg"));
			return "login";
		}
		addTicketCookie(map.get("ticket"), response, rememberme);
		return "redirect:" + next;
	}

	private void addTicketCookie(String ticket, HttpServletResponse response, boolean rememberme) {
		Cookie c = new Cookie("ticket", ticket);
		c.setPath("/");
		if (rememberme) {
			c.setMaxAge(3600 * 24 * 5);
		}
		response.addCookie(c);
	}


	@GetMapping("/logout")
	public String logout(@CookieValue("ticket") Cookie c, HttpServletResponse response) {
		if (StringUtils.isNotBlank(c.getValue())) {
			userService.logout(c.getValue());
			c.setMaxAge(0);
			response.addCookie(c);
		}
		return "redirect:/";
	}

}
