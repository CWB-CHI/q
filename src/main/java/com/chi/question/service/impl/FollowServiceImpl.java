package com.chi.question.service.impl;


import com.chi.question.service.FollowService;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.*;

@Service
public class FollowServiceImpl implements FollowService {
	private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

	@Autowired
	private JedisAdapter jedisAdapter;

	@Override
	public boolean follow(int userId, int entityType, int entityId) {
		try {
			final String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
			final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

			final Jedis jedis = jedisAdapter.getJedis();
			final Transaction tx = jedisAdapter.mulit(jedis);
			tx.zadd(followerKey, new Date().getTime(), String.valueOf(userId));
			tx.zadd(followeeKey, new Date().getTime(), String.valueOf(entityId));
			jedisAdapter.exec(tx, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean unfollow(int userId, int entityType, int entityId) {
		try {
			final String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
			final String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);

			final Jedis jedis = jedisAdapter.getJedis();
			final Transaction tx = jedisAdapter.mulit(jedis);
			tx.zrem(followerKey, String.valueOf(userId));
			tx.zrem(followeeKey, String.valueOf(entityId));
			jedisAdapter.exec(tx, jedis);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}


	@Override
	public List<Integer> getFollowers(int entityType, int entityId, int count) {
		final Set<String> ids = jedisAdapter.zrevrange(RedisKeyUtil.getFollowerKey(entityId, entityType), 0, count);
		List<Integer> list = new ArrayList<>();
		final Iterator<String> iterator = ids.iterator();
		while (iterator.hasNext()) {
			final String idStr = iterator.next();
			list.add(Integer.parseInt(idStr));
		}
		return list;
	}

	@Override
	public List<Integer> getFollowees(int userId, int entityType, int count) {
		final Set<String> ids = jedisAdapter.zrevrange(RedisKeyUtil.getFolloweeKey(userId, entityType), 0, count);
		List<Integer> list = new ArrayList<>();
		final Iterator<String> iterator = ids.iterator();
		while (iterator.hasNext()) {
			final String idStr = iterator.next();
			list.add(Integer.parseInt(idStr));
		}
		return list;
	}

	@Override
	public long getFollowerCount(int entityType, int entityId) {
		String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
		return jedisAdapter.zcard(followerKey);
	}

	@Override
	public long getFolloweeCount(int userId, int entityType) {
		String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
		return jedisAdapter.zcard(followeeKey);
	}

	@Override
	public boolean isFollower(int id, int entityType, int entityId) {
		final String followerKey = RedisKeyUtil.getFollowerKey(entityId, entityType);
		return jedisAdapter.zscore(followerKey, String.valueOf(id)) != null;
	}
}
