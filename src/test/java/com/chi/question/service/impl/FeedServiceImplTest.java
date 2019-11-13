package com.chi.question.service.impl;

import com.chi.question.domain.Feed;
import com.chi.question.service.FeedService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedServiceImplTest {

	@Autowired
	private FeedService feedService;

	@Test
	public void getUserFeeds() {
		final ArrayList<Integer> userIds = new ArrayList<>();
		userIds.add(1);
		userIds.add(2);
		final List<Feed> userFeeds = feedService.getUserFeeds(Integer.MAX_VALUE, userIds, 10);
	}

	@Test
	public void addFeed() {
	}

	@Test
	public void getById() {
	}
}