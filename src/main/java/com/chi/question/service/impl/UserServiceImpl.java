package com.chi.question.service.impl;

import com.chi.question.dao.LoginTicketDao;
import com.chi.question.dao.UserDao;
import com.chi.question.domain.LoginTicket;
import com.chi.question.domain.User;
import com.chi.question.service.UserService;
import com.chi.question.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private LoginTicketDao loginTicketDao;

	@Override
	public User getUser(int id) {
		return userDao.selectById(id);
	}

	@Override
	public Map<String, String> register(String username, String password) {
		Map<String, String> map = new HashMap<>();
		if (StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		if (userDao.selectByName(username) != null) {
			map.put("msg", "用户名已存在!");
			return map;
		}

		User user = new User();
		user.setName(username);
		user.setSalt(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 5));
		user.setPassword(Utils.MD5(password + user.getSalt()));
		user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
		userDao.addUser(user);

		String ticket = addTicket(user.getId());
		map.put("ticket", ticket);
		return map;
	}


	@Override
	public Map<String, String> login(String username, String password) {
		Map<String, String> map = new HashMap<>();
		if (StringUtils.isBlank(username)) {
			map.put("msg", "用户名不能为空");
			return map;
		}
		if (StringUtils.isBlank(password)) {
			map.put("msg", "密码不能为空");
			return map;
		}
		if (userDao.selectByName(username) == null) {
			map.put("msg", "用户名不存在!");
			return map;
		}

		User user = userDao.selectByName(username);
		String salt = user.getSalt();
		if (!Utils.MD5(password + salt).equals(user.getPassword())) {
			map.put("msg", "密码错误!");
			return map;
		} else {
			String ticket = addTicket(user.getId());
			map.put("ticket", ticket);
		}

		return map;
	}

	@Override
	public void logout(String ticket) {
		loginTicketDao.disable(ticket);
	}

	@Override
	public User selectByName(String toName) {
		return userDao.selectByName(toName);
	}

	private String addTicket(int userId) {
		LoginTicket ticket = new LoginTicket();
		Date date = new Date();
		date.setTime(date.getTime() + 1000 * 3600 * 24);
		ticket.setExpired(date);
		ticket.setStatus(0);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		ticket.setUserId(userId);

		loginTicketDao.addLoginTicket(ticket);
		return ticket.getTicket();
	}
}
