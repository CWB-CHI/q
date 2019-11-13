package com.chi.question.service;


import com.chi.question.domain.User;

import java.util.Map;

public interface UserService {
	public User getUser(int id);

	public Map<String, String> register(String username, String password);
	public Map<String, String> login(String username, String password);


	public void logout(String value);

	User selectByName(String toName);
}
