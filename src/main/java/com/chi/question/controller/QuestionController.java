package com.chi.question.controller;


import com.chi.question.domain.Comment;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.Question;
import com.chi.question.domain.User;
import com.chi.question.service.*;
import com.chi.question.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class QuestionController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private UserService userService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private LikeService likeService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private FollowService followService;

	@PostMapping(value = "/question/add")
	@ResponseBody
	public Map<String, String> add(@RequestParam("title") String title,
								   @RequestParam("content") String content) {
		Map<String, String> map = new HashMap<>();
		try {
			User user = hostHolder.getUser();
			Question q = new Question();
			q.setTitle(title);
			q.setContent(content);
			q.setCreatedDate(new Date());
			if (user == null) {
				q.setUserId(3);
			} else {
				q.setUserId(user.getId());
			}
			questionService.addQuestion(q);
			map.put("code", "0");
		} catch (Exception e) {
			map.put("code", "1");
			map.put("msg", "失败");
		}
		return map;
	}


	@GetMapping("/question/{qid}")
	public String questionDetail(Model model, @PathVariable("qid") int qid) {
		Question q = questionService.getById(qid);
		List<Comment> comments = commentService.getCommentListByEntityIdWithUser(qid, Constants.ENTITY_QUESTION);
		User user = hostHolder.getUser();

		comments.forEach(c -> {
			c.setLikeCount(likeService.getLikeCount(c.getId(), Constants.ENTITY_COMMENT));
			if (user != null) {
				c.setLike(likeService.getLikeStatus(user.getId(), c.getId(), Constants.ENTITY_COMMENT) > 0);
				c.setDislike(likeService.getLikeStatus(user.getId(), c.getId(), Constants.ENTITY_COMMENT) < 0);
			}

		});
		model.addAttribute("comments", comments);
		model.addAttribute("question", q);
		final int followerCount = (int) followService.getFollowerCount(Constants.ENTITY_QUESTION, qid);
		final List<Integer> followerIds = followService.getFollowers(Constants.ENTITY_QUESTION, qid, followerCount);

		List<User> followers = new ArrayList<>();
		for (int i = 0; i < followerIds.size(); i++) {
			followers.add(userService.getUser(followerIds.get(i)));
		}
		model.addAttribute("followerCount", followerCount);
		model.addAttribute("followers", followers);
		model.addAttribute("followed", followService.isFollower(user==null?0:user.getId(), Constants.ENTITY_QUESTION, qid));
		return "detail";
	}


}
