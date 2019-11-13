package com.chi.question.util;

public class RedisKeyUtil {


	private static final String SPLIT = ":";
	private static final String BIZ_LIKE = "LIKE";
	private static final String BIZ_DISLIKE = "DISLIKE";
	private static final String BIZ_EVENTQUEUE = "EVENT_QUEUE";

	private static final String BIZ_FOLLOWER = "FOLLOWER";
	private static final String BIZ_FOLLOWEE = "FOLLOWEE";
	private static final String BIZ_TIMELINE = "TIMELINE";


	public static String getFollowerKey(int entityId, int entityType) {
		return BIZ_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
	}

	public static String getFolloweeKey(int id, int entityType) {
		return BIZ_FOLLOWEE + SPLIT + id + SPLIT + entityType;
	}


	public static String getLikeKey(int entityId, int entityType) {
		return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getDisLikeKey(int entityId, int entityType) {
		return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
	}

	public static String getEventQueueKey() {
		return BIZ_EVENTQUEUE;
	}

	public static String getTimelineKey(int uid) {
		return BIZ_TIMELINE + SPLIT + uid;
	}
}
