package com.chi.question.async.hadler;

import com.alibaba.fastjson.JSONObject;
import com.chi.question.async.EventModel;
import com.chi.question.async.EventType;
import com.chi.question.domain.Feed;
import com.chi.question.domain.Question;
import com.chi.question.domain.User;
import com.chi.question.service.FeedService;
import com.chi.question.service.FollowService;
import com.chi.question.service.QuestionService;
import com.chi.question.service.UserService;
import com.chi.question.util.Constants;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FeedEventHandler implements EventHandler {
	@Autowired
	private FeedService feedService;

	@Autowired
	private UserService userService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private FollowService followService;
	@Autowired
	private JedisAdapter jedisAdapter;

	@Override
	public void doHandle(EventModel model) {
		final User actor = userService.getUser(model.getActorId());
		if (actor == null)
			return;

		Feed feed = new Feed();
		feed.setUserId(model.getActorId());
		feed.setType(model.getType().getValue());
		feed.setCreatedDate(new Date());

		Map<String, String> mapData = new HashMap<>();
		mapData.put("userId", String.valueOf(actor.getId()));
		mapData.put("userHead", actor.getHeadUrl());
		mapData.put("userName", actor.getName());
		if (EventType.COMMENT.equals(model.getType())
				|| (EventType.FOLLOW.equals(model.getType()) && Constants.ENTITY_QUESTION == model.getEntityType())) {
			final Question q = questionService.getById(model.getEntityId());
			mapData.put("questionId", "" + q.getId());
			mapData.put("questionTitle", q.getTitle());
		}

		feed.setData(JSONObject.toJSONString(mapData));
		feedService.addFeed(feed);
		final int followerCount = (int) followService.getFollowerCount(Constants.ENTITY_USER, actor.getId());
		final List<Integer> followers = followService.getFollowers(Constants.ENTITY_USER, actor.getId(), followerCount);
		for (int i = 0; i < followers.size(); i++) {
			final Integer uid = followers.get(i);
			final String timelineKey = RedisKeyUtil.getTimelineKey(uid);
			jedisAdapter.lpush(timelineKey, "" + feed.getId());
		}
	}

	@Override
	public List<EventType> getSupportEventTypes() {
		return Arrays.asList(EventType.FOLLOW, EventType.COMMENT);
	}
}
