package com.chi.question.interceptor;


import com.chi.question.dao.LoginTicketDao;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.LoginTicket;
import com.chi.question.domain.User;
import com.chi.question.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class PassportInterceptor implements HandlerInterceptor {

	@Autowired
	private LoginTicketDao loginTicketDao;
	@Autowired
	private UserService userService;

	@Autowired
	private HostHolder hostHolder;


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie[] cookies = request.getCookies();
		LoginTicket ticket = null;
		if (cookies != null)
			for (Cookie c : cookies) {
				if ("ticket".equals(c.getName())) {
					ticket = loginTicketDao.selectByTicket(c.getValue());
					break;
				}
			}

		if (ticket != null && ticket.getStatus() == 0 && new Date().before(ticket.getExpired())) {
			User user = userService.getUser(ticket.getUserId());
			hostHolder.setUser(user);
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView != null && hostHolder.getUser() != null) {
			modelAndView.addObject("user", hostHolder.getUser());
		}
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		hostHolder.clear();
	}
}
