package com.chi.question.service.impl;

import com.chi.question.service.LikeService;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
	@Autowired
	private JedisAdapter jedisAdapter;

	@Override
	public int like(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		long sadd = jedisAdapter.sadd(likeKey, Integer.toString(userId));

		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		long srem = jedisAdapter.srem(disLikeKey, Integer.toString(userId));

		return (int) jedisAdapter.scard(likeKey);
	}

	@Override
	public int dislike(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		long srem = jedisAdapter.srem(likeKey, Integer.toString(userId));

		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		long sadd = jedisAdapter.sadd(disLikeKey, Integer.toString(userId));

		return (int) jedisAdapter.scard(likeKey);
	}

	@Override
	public int getLikeCount(int entityId, int entityType) {
		return (int) jedisAdapter.scard(RedisKeyUtil.getLikeKey(entityId, entityType));
	}


	@Override
	public int getLikeStatus(int userId, int entityId, int entityType) {
		String likeKey = RedisKeyUtil.getLikeKey(entityId, entityType);
		String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId, entityType);
		boolean like = jedisAdapter.sismember(likeKey, Integer.toString(userId));
		boolean dislike = jedisAdapter.sismember(disLikeKey, Integer.toString(userId));
		return like ? 1 : dislike ? -1 : 0;
	}
}
