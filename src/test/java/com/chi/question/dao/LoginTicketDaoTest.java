package com.chi.question.dao;

import com.chi.question.domain.LoginTicket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;


@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTicketDaoTest {

	@Autowired
	private LoginTicketDao loginTicketDao;
	@Test
	public void addLoginTicket() {

		LoginTicket ticket = new LoginTicket();
		ticket.setExpired(new Date());
		ticket.setStatus(0);
		ticket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
		ticket.setUserId(1);

		loginTicketDao.addLoginTicket(ticket);
	}

	@Test
	public void selectByTicket() {
	}
}