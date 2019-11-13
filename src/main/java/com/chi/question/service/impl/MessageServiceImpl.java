package com.chi.question.service.impl;

import com.chi.question.dao.MessageDao;
import com.chi.question.domain.Message;
import com.chi.question.service.MessageService;
import com.chi.question.service.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
	@Autowired
	private MessageDao messageDao;
	@Autowired
	private SensitiveWordFilter sensitiveWordFilter;

	@Override
	public int addMessage(Message m) {
		m.setContent(sensitiveWordFilter.filter(HtmlUtils.htmlEscape(m.getContent())));
		return messageDao.addMessage(m);
	}

	@Override
	public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
		return messageDao.getConversationDetail(conversationId, offset, limit);
	}

	@Override
	public int getConvesationUnreadCount(int userId, String conversationId) {
		return messageDao.getConvesationUnreadCount(userId, conversationId);
	}

	@Override
	public List<Message> getConversationList(int userId, int offset, int limit) {
		return messageDao.getConversationList(userId, offset, limit);
	}
}
