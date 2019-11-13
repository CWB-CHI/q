package com.chi.question.service;

import com.chi.question.domain.Feed;

import java.util.List;

public interface FeedService {

	public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count);

	public int addFeed(Feed feed);

	public Feed getById(int id);

}
