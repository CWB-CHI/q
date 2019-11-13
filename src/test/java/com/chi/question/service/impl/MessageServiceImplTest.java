package com.chi.question.service.impl;

import com.chi.question.domain.Message;
import com.chi.question.service.MessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceImplTest {
	@Autowired
	private MessageService messageService;

	@Test
	public void addMessage() {
		Message m = new Message();
		m.setContent("欢声雷动sdf");
		m.setFromId(2);
		m.setToId(5);
		m.setCreatedDate(new Date());
		m.setHasRead(0);


		messageService.addMessage(m);
	}

	@Test
	public void getConversationDetail() {
	}

	@Test
	public void getConvesationUnreadCount() {
	}

	@Test
	public void getConversationList() {

		List<Message> conversationList = messageService.getConversationList(1009, 0, 10);
		System.out.println(conversationList);
	}
}