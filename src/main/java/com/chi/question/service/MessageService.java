package com.chi.question.service;

import com.chi.question.domain.Message;

import java.util.List;

public interface MessageService {

	int addMessage(Message m);

	List<Message> getConversationDetail(String conversationId, int offset, int limit);

	int getConvesationUnreadCount(int userId, String conversationId);

	List<Message> getConversationList(int userId, int offset, int limit);


}
