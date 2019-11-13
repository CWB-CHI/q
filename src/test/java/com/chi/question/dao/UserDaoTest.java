package com.chi.question.dao;

import com.chi.question.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init.sql")
public class UserDaoTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void addUser() {
		for (int i = 0; i < 1000; i++) {
			Random random = new Random();

			User user = new User("user" + (i + 1));
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
			user.setName(String.format("USER%d", i));
			user.setPassword("");
			user.setSalt("");
			userDao.addUser(user);
			userDao.updatePassword(user.getId(), "newpassword");

			System.out.println(user.getId());
		}
	}

	@Test
	public void selectById() {
		User user = userDao.selectById(1);
		System.out.println(user);

	}

	@Test
	public void updatePassword() {
	}

	@Test
	public void deleteById() {
	}
}