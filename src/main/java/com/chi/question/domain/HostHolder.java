package com.chi.question.domain;


import org.springframework.stereotype.Component;

@Component
public class HostHolder {
	private static ThreadLocal<User> user = new ThreadLocal<>();

	public User getUser() {
		return user.get();
	}

	public void setUser(User u) {
		user.set(u);
	}

	public void clear() {
		user.remove();
	}
}
