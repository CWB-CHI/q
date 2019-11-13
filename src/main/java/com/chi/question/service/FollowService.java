package com.chi.question.service;

import java.util.List;

public interface FollowService {
	boolean follow(int userId, int entityType, int entityId);

	boolean unfollow(int userId, int entityType, int entityId);

	List<Integer> getFollowers(int entityType, int entityId, int count);

	List<Integer> getFollowees(int userId, int entityType, int count);

	long getFollowerCount(int entityType, int entityId);

	long getFolloweeCount(int userId, int entityType);

	boolean isFollower(int id, int entityType, int entity);
}
