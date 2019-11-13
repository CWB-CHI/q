package com.chi.question.controller;


import com.chi.question.async.EventModel;
import com.chi.question.async.EventProducer;
import com.chi.question.async.EventType;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.User;
import com.chi.question.service.CommentService;
import com.chi.question.service.FollowService;
import com.chi.question.service.QuestionService;
import com.chi.question.service.UserService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FollowController {

	@Autowired
	FollowService followService;

	@Autowired
	CommentService commentService;

	@Autowired
	QuestionService questionService;

	@Autowired
	UserService userService;

	@Autowired
	HostHolder hostHolder;

	@Autowired
	EventProducer eventProducer;


	@RequestMapping(path = {"/followUser"}, method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> followUser(@RequestParam("userId") int userId) {
		User user = hostHolder.getUser();
		Map<String, Object> map = new HashMap<>();

		if (user == null) {
			map.put("code", 1);
			return map;
		}
		final User f = userService.getUser(userId);
		if (f == null) {
			map.put("code", 1);
			map.put("msg", "用户不存在");
			return map;
		}
		EventModel e = new EventModel();
		e.setActorId(user.getId());
		e.setEntityType(Constants.ENTITY_USER);
		e.setEntityId(userId);
		e.setType(EventType.FOLLOW);
		eventProducer.fireEvent(e);
		final boolean follow = followService.follow(user.getId(), Constants.ENTITY_USER, f.getId());
		map.put("code", follow ? 0 : 1);

		map.put("msg", followService.getFollowerCount(Constants.ENTITY_USER, user.getId()));
		return map;
	}

	@RequestMapping(path = {"/unfollowUser"}, method = {RequestMethod.POST, RequestMethod.GET})
	@ResponseBody
	public Map<String, Object> unfollowUser(@RequestParam("userId") int userId) {
		User user = hostHolder.getUser();
		Map<String, Object> map = new HashMap<>();

		if (user == null) {
			map.put("code", 1);
			return map;
		}

		final User f = userService.getUser(userId);

		if (f == null) {
			map.put("code", 1);
			map.put("msg", "用户不存在");
			return map;
		}

		final boolean follow = followService.unfollow(user.getId(), Constants.ENTITY_USER, f.getId());
		map.put("code", follow ? 0 : 1);
		map.put("msg", followService.getFollowerCount(Constants.ENTITY_USER, user.getId()));
		return map;
	}


	@PostMapping("/followQuestion")
	@ResponseBody
	public Map<String, Object> followQuestion(@RequestParam("questionId") int questionId) {
		Map<String, Object> map = new HashMap<>();
		final User user = hostHolder.getUser();
		if (user == null) {
			map.put("code", 999);
			return map;
		}
		EventModel e = new EventModel();
		e.setActorId(user.getId());
		e.setEntityType(Constants.ENTITY_QUESTION);
		e.setEntityId(questionId);
		e.setType(EventType.FOLLOW);
		eventProducer.fireEvent(e);

		followService.follow(user.getId(), Constants.ENTITY_QUESTION, questionId);
		map.put("headUrl", user.getHeadUrl());
		map.put("name", user.getName());
		map.put("id", user.getId());
		map.put("count", followService.getFollowerCount(Constants.ENTITY_QUESTION, questionId));
		map.put("code", 0);
		return map;
	}

	@PostMapping("/unfollowQuestion")
	@ResponseBody
	public Map<String, Object> unfollowQuestion(@RequestParam("questionId") int questionId) {
		Map<String, Object> map = new HashMap<>();
		final User user = hostHolder.getUser();
		if (user == null) {
			map.put("code", 999);
			return map;
		}
		followService.unfollow(user.getId(), Constants.ENTITY_QUESTION, questionId);
		map.put("code", 0);
		map.put("id", user.getId());
		map.put("count", followService.getFollowerCount(Constants.ENTITY_QUESTION, questionId));
		return map;
	}


	@RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
	public String followers(Model model, @PathVariable("uid") int userId) {
		List<Integer> followerIds = followService.getFollowers(Constants.ENTITY_USER, userId, 10);
		if (hostHolder.getUser() != null) {
			model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
		} else {
			model.addAttribute("followers", getUsersInfo(0, followerIds));
		}
		model.addAttribute("followerCount", followService.getFollowerCount(Constants.ENTITY_USER, userId));
		model.addAttribute("curUser", userService.getUser(userId));
		return "followers";
	}

	@RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
	public String followees(Model model, @PathVariable("uid") int userId) {
		List<Integer> followeeIds = followService.getFollowees(userId, Constants.ENTITY_USER, 10);

		if (hostHolder.getUser() != null) {
			model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
		} else {
			model.addAttribute("followees", getUsersInfo(0, followeeIds));
		}
		model.addAttribute("followeeCount", followService.getFolloweeCount(userId, Constants.ENTITY_USER));
		model.addAttribute("curUser", userService.getUser(userId));
		return "followees";
	}

	private List<User> getUsersInfo(int localUserId, List<Integer> userIds) {
		List<User> list = new ArrayList<>();
		for (int uid : userIds) {
			User user = userService.getUser(uid);
			if (user == null) {
				continue;
			}
			user.setCommentCount(commentService.getUserCommentCount(uid));
			user.setFollowerCount((int) followService.getFollowerCount(Constants.ENTITY_USER, uid));
			user.setFolloweeCount((int) followService.getFolloweeCount(uid, Constants.ENTITY_USER));
			if (localUserId != 0)
				user.setFollowed(followService.isFollower(localUserId, Constants.ENTITY_USER, uid));
			list.add(user);
		}
		return list;

	}
}
