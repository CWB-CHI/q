package com.chi.question.interceptor;


import com.chi.question.domain.HostHolder;
import com.chi.question.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginedInterceptor implements HandlerInterceptor {

	@Autowired
	private HostHolder hostHolder;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		User user = hostHolder.getUser();
		if (user == null) {
			response.sendRedirect("/login?next=" + request.getRequestURI());
			return false;
		}

		return true;
	}


}
