package com.chi.question.service.impl;

import com.chi.question.dao.FeedDao;
import com.chi.question.domain.Feed;
import com.chi.question.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedServiceImpl implements FeedService {
	@Autowired
	private FeedDao feedDao;

	@Override
	public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count) {
		return feedDao.selectUserFeeds(maxId, userIds, count);
	}

	@Override
	public int addFeed(Feed feed) {
		return feedDao.addFeed(feed);
	}

	@Override
	public Feed getById(int id) {
		return feedDao.getFeedById(id);
	}
}
