package com.chi.question.controller;

import com.chi.question.async.EventModel;
import com.chi.question.async.EventProducer;
import com.chi.question.async.EventType;
import com.chi.question.domain.Comment;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.User;
import com.chi.question.service.CommentService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@Controller
public class CommentController {
	@Autowired
	private CommentService commentService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private EventProducer eventProducer;


	@PostMapping(value = {"/addComment", "/comment/add"})
	public String addComment(@RequestParam("questionId") int questionId, @RequestParam("content") String content) {
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setCreatedDate(new Date());
		comment.setEntityId(questionId);
		comment.setEntityType(Constants.ENTITY_QUESTION);
		comment.setStatus(0);
		User user = hostHolder.getUser();
		if (user == null)
			comment.setUserId(3);
		else
			comment.setUserId(user.getId());
		commentService.addComment(comment);
		EventModel e = new EventModel();
		e.setActorId(user.getId());
		e.setType(EventType.COMMENT);
		e.setEntityType(Constants.ENTITY_QUESTION);
		e.setEntityId(questionId);
		eventProducer.fireEvent(e);

		return "redirect:/question/" + String.valueOf(questionId);
	}
}
