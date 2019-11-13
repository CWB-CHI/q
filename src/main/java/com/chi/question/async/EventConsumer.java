package com.chi.question.async;

import com.alibaba.fastjson.JSONObject;
import com.chi.question.async.hadler.EventHandler;
import com.chi.question.util.JedisAdapter;
import com.chi.question.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
	private Map<EventType, List<EventHandler>> config = new HashMap<>();
	private ApplicationContext applicationContext;
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	@Autowired
	private JedisAdapter jedisAdapter;

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, EventHandler> beansOfType = applicationContext.getBeansOfType(EventHandler.class);
		beansOfType.entrySet().forEach(entry -> {
			EventHandler handler = entry.getValue();
			List<EventType> supportEventTypes = handler.getSupportEventTypes();
			supportEventTypes.forEach(supportType -> {
				if (!config.containsKey(supportType))
					config.put(supportType, new ArrayList<>());
				config.get(supportType).add(handler);
			});
		});

		new Thread(() -> {
			while (true) {
				try {
					String key = RedisKeyUtil.getEventQueueKey();
					List<String> brpop = jedisAdapter.brpop(key);
					for (int i = 1; i < brpop.size(); i++) {
						String e = brpop.get(i);
						logger.info("解析json " + e);
						EventModel event = JSONObject.parseObject(e, EventModel.class);
						if (!config.containsKey(event.getType())) {
							logger.error("错误时间类型 " + event.getType());
						} else {
							config.get(event.getType()).forEach(handler -> {
								handler.doHandle(event);
							});
						}
					}
				} catch (Exception e) {
					logger.error("处理队列发生错误 " + e.getMessage());
				}
			}
		}).start();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
