package com.chi.question.controller;


import com.chi.question.domain.Feed;
import com.chi.question.domain.HostHolder;
import com.chi.question.domain.User;
import com.chi.question.service.FeedService;
import com.chi.question.service.FollowService;
import com.chi.question.util.Constants;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class FeedController {

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private JedisAdapter jedisAdapter;

	@Autowired
	private FeedService feedService;

	@Autowired
	private FollowService followService;


	@RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String getPushFeeds(Model model) {
		int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
		List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
		List<Feed> feeds = new ArrayList<Feed>();
		for (String feedId : feedIds) {
			Feed feed = feedService.getById(Integer.parseInt(feedId));
			if (feed != null) {
				feeds.add(feed);
			}
		}
		model.addAttribute("feeds", feeds);
		return "feeds";
	}

	@RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String getPullFeeds(Model model) {
		final User user = hostHolder.getUser();
		int localUserId = user != null ? user.getId() : 0;
		List<Integer> followees = new ArrayList<>();
		if (localUserId != 0) {
			// 关注的人
			followees = followService.getFollowees(localUserId, Constants.ENTITY_USER, Integer.MAX_VALUE);
		}
		List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
		model.addAttribute("feeds", feeds);
		return "feeds";
	}

}
