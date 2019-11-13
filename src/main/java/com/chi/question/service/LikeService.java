package com.chi.question.service;

public interface LikeService {

	int like(int userId, int entityId, int entityType);

	int dislike(int userId, int entityId, int entityType);

	int getLikeCount(int entityId, int entityType);

	int getLikeStatus(int userId, int entityId, int entityType);


}
