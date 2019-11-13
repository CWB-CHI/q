package com.chi.question.async;


import com.alibaba.fastjson.JSONObject;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventProducer {
	@Autowired
	JedisAdapter jedisAdapter;

	public boolean fireEvent(EventModel eventModel) {
		try {
			String json = JSONObject.toJSONString(eventModel);
			String key = RedisKeyUtil.getEventQueueKey();
			jedisAdapter.lpush(key, json);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {
		String s = "{\"actorId\":1009,\"entityId\":8,\"entityOwnerId\":1009,\"entityType\":2,\"exts\":{\"questionId\":\"9947\"},\"type\":\"LIKE\"}";
		EventModel eventModel = JSONObject.parseObject(s, EventModel.class);
		System.out.println(eventModel);

		JedisAdapter jedisAdapter = new JedisAdapter();
		jedisAdapter.afterPropertiesSet();
		jedisAdapter.lpush(RedisKeyUtil.getEventQueueKey(), JSONObject.toJSONString(eventModel));

		List<String> brpop = jedisAdapter.brpop(RedisKeyUtil.getEventQueueKey());
		System.out.println(brpop);
	}

}
