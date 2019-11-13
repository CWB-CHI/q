package com.chi.question.async.hadler;

import com.chi.question.async.EventModel;
import com.chi.question.async.EventType;
import com.chi.question.domain.Message;
import com.chi.question.domain.User;
import com.chi.question.service.MessageService;
import com.chi.question.service.UserService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeEventHandler implements EventHandler {
	@Autowired
	private MessageService messageService;
	@Autowired
	private UserService userService;


	@Override
	public void doHandle(EventModel model) {
		int userId = model.getActorId();
		Message m = new Message();
		m.setFromId(Constants.SYSTEM_USERID);
		m.setToId(model.getEntityOwnerId());
		m.setCreatedDate(new Date());

		User user = userService.getUser(model.getActorId());
		m.setContent("用户" + user.getName() + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

		messageService.addMessage(m);
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.LIKE);
	}
}
