package com.chi.question.controller;


import com.chi.question.domain.HostHolder;
import com.chi.question.domain.Question;
import com.chi.question.domain.User;
import com.chi.question.service.FollowService;
import com.chi.question.service.QuestionService;
import com.chi.question.service.UserService;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

	@Autowired
	private UserService userService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private FollowService followService;
	@Autowired
	private HostHolder hostHolder;


	@GetMapping({"/user/{id}"})
	public String userIndex(Model model, @PathVariable("id") int id) {
		final User user = userService.getUser(id);
		final User host = hostHolder.getUser();
		if (host != null) {
			final boolean follower = followService.isFollower(host.getId(), Constants.ENTITY_USER, user.getId());
			user.setFollowed(follower);
		}
		user.setFollowerCount((int) followService.getFollowerCount(Constants.ENTITY_USER, user.getId()));
		user.setFolloweeCount((int) followService.getFolloweeCount(Constants.ENTITY_USER, user.getId()));
		model.addAttribute("profileUser", user);

		List<Map<String, Object>> items = getQuestionList(id, 0, 10);
		model.addAttribute("itmes", items);

		return "profile";
	}


	@GetMapping({"/", "/index"})
	public String index(Model model) {
		model.addAttribute("test", "ttt");
		List<Map<String, Object>> items = getQuestionList(0, 0, 10);

		model.addAttribute("itmes", items);
		return "index";
	}

	private List<Map<String, Object>> getQuestionList(int userId, int offset, int limit) {
		List<Question> questions = questionService.getLatestQuestions(userId, offset, limit);
		List<Map<String, Object>> items = new ArrayList<>();
		User user = hostHolder.getUser();

		questions.forEach((q) -> {
			int uid = q.getUserId();
			User u = userService.getUser(uid);
			if (user != null) {
				u.setFollowed(followService.isFollower(user.getId(), Constants.ENTITY_USER, u.getId()));
			}
			Map<String, Object> item = new HashMap<>();
			item.put("question", q);
			item.put("user", u);
			item.put("followerCount", followService.getFollowerCount(Constants.ENTITY_QUESTION, q.getId()));
			items.add(item);
		});
		return items;
	}

	@GetMapping({"/include"})
	public String include(HttpServletRequest request) {

		return "t1";
	}

	@GetMapping("/test")
	@ResponseBody
	public Map<String, String> test() {

		Map<String, String> map = new HashMap<>();
		map.put("name", "chi");
		map.put("code", "200");
		return map;
	}


}
