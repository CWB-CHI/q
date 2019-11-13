package com.chi.question.async.hadler;

import com.chi.question.async.EventModel;
import com.chi.question.async.EventType;
import com.chi.question.domain.Message;
import com.chi.question.domain.User;
import com.chi.question.service.MessageService;
import com.chi.question.service.QuestionService;
import com.chi.question.service.UserService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class FollowEventHandler implements EventHandler {

	@Autowired
	private UserService userService;
	@Autowired
	private MessageService messageService;
	@Autowired
	private QuestionService questionService;

	@Override
	public void doHandle(EventModel model) {
		final int actorId = model.getActorId();
		if (EventType.FOLLOW.equals(model.getType())) {
			final User actor = userService.getUser(actorId);
			if (Constants.ENTITY_USER == model.getEntityType()) {
				Message m = new Message();
				m.setContent("用户" + actor.getName() + "关注了你!");
				m.setFromId(Constants.SYSTEM_USERID);
				m.setCreatedDate(new Date());
				m.setToId(model.getEntityId());
				messageService.addMessage(m);

			} else if (Constants.ENTITY_QUESTION == model.getEntityType()) {
				Message m = new Message();
				m.setContent("用户" + actor.getName() + "关注了你的问题!" + "http://127.0.0.1:8080/question/" + model.getEntityId());
				m.setFromId(Constants.SYSTEM_USERID);
				m.setCreatedDate(new Date());
				m.setToId(questionService.getById(model.getEntityId()).getUserId());
				messageService.addMessage(m);
			}
		}

	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW, EventType.UNFOLLOW);
	}
}
