package com.chi.question.controller;

import com.chi.question.async.EventModel;
import com.chi.question.async.EventProducer;
import com.chi.question.async.EventType;
import com.chi.question.domain.Comment;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.User;
import com.chi.question.service.CommentService;
import com.chi.question.service.LikeService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {
	@Autowired
	private LikeService likeService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private CommentService commentService;

	@Autowired
	private EventProducer eventProducer;

	@PostMapping("/like")
	@ResponseBody
	public Map<String, Object> like(@RequestParam("commentId") int commentId) {
		Map<String, Object> map = new HashMap<>();
		User user = hostHolder.getUser();
		if (user == null) {
			map.put("code", 1);
			map.put("msg", "用户未登录");
			return map;
		}
		likeService.like(user.getId(), commentId, Constants.ENTITY_COMMENT);
		map.put("msg", String.valueOf(likeService.getLikeCount(commentId, Constants.ENTITY_COMMENT)));
		map.put("code", 0);

		EventModel event = new EventModel(EventType.LIKE);
		Comment comment = commentService.getCommentById(commentId);
		event.setActorId(user.getId())
				.setEntityType(Constants.ENTITY_COMMENT)
				.setEntityId(commentId)
				.setEntityOwnerId(comment.getUserId())
				.setExt("questionId", String.valueOf(comment.getEntityId()));
		eventProducer.fireEvent(event);
		return map;
	}

	@PostMapping("/dislike")
	@ResponseBody
	public Map<String, Object> dislike(@RequestParam("commentId") int commentId) {
		Map<String, Object> map = new HashMap<>();
		User user = hostHolder.getUser();
		if (user == null) {
			map.put("code", 1);
			map.put("msg", "用户未登录");
			return map;
		}
		likeService.dislike(user.getId(), commentId, Constants.ENTITY_COMMENT);
		map.put("msg", String.valueOf(likeService.getLikeCount(commentId, Constants.ENTITY_COMMENT)));
		map.put("code", 0);
		return map;
	}


}
