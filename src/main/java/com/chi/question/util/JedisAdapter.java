package com.chi.question.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

@Component
public class JedisAdapter implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
	private static JedisPool pool;

	@Override
	public void afterPropertiesSet() throws Exception {
		pool = new JedisPool("redis://localhost:6379/1");
	}

	public long sadd(String key, String... values) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.sadd(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public long srem(String key, String... values) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.srem(key, values);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public long scard(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.scard(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public Boolean sismember(String key, String value) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.sismember(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<String> brpop(String key) {
		try (Jedis jedis = pool.getResource()) {

			return jedis.brpop(0, key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public long lpush(String key, String... values) {
		try (Jedis jedis = pool.getResource()) {
			Long lpush = jedis.lpush(key, values);

			return lpush;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public long zadd(String key, String value, long score) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zadd(key, score, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public long zrem(String key, String value) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zrem(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public Jedis getJedis() {
		return pool.getResource();
	}

	public Transaction mulit(Jedis jedis) {
		try {
			return jedis.multi();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<Object> exec(Transaction tx, Jedis jedis) {
		try (Transaction tx1 = tx; Jedis j1 = jedis;) {
			return tx1.exec();
		} catch (Exception e) {
			logger.error("发生异常" + e.getMessage());
			tx.discard();
		}
		return null;
	}

	public Set<String> zrange(String key, int start, int end) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}


	public Set<String> zrevrange(String key, int start, int end) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public long zcard(String key) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zcard(key);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return -1;
		}
	}

	public Double zscore(String key, String value) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.zscore(key, value);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public List<String> lrange(String timelineKey, int start, int end) {
		try (Jedis jedis = pool.getResource()) {
			return jedis.lrange(timelineKey, start, end);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	public static void main(String[] args) {

//		Logger logger1 = LoggerFactory.getLogger(JedisAdapter.class);
//		Logger logger2 = LoggerFactory.getLogger(JedisAdapter.class);
//		System.out.println(logger1);
//		System.out.println(logger2);
//		System.out.println(logger1 == logger2);
//		logger.info("hi");
//		logger.info("hi");
//		logger.info("hi");

//		JedisPool pool = new JedisPool("localhost", 6379);
//
//		Jedis resource = pool.getResource();
//		resource.close();
//
//		Jedis jedis = new Jedis("localhost", 6379);
//		jedis.select(1);
//		jedis.flushDB();
//
//		jedis.sadd("user", "a", "z", "c");
//		jedis.sadd("user", "1");
//
//		Set<String> user = jedis.smembers("user");
//		user.forEach(System.out::println);
//
//		jedis.hset("p1", "name", "peter");
//		jedis.hset("p1", "age", "22");
//
//		Map<String, String> p1 = jedis.hgetAll("p1");
//		System.out.println(p1);
//		Set<String> keys = jedis.keys("*");
//		keys.forEach(System.out::println);
//
//		jedis.rpush("l1", "a1", "a2");
//		jedis.lpush("l1", "a3", "a4");
//
//		Long l1 = jedis.llen("l1");
//		List<String> l11 = jedis.lrange("l1", 0, l1);
//		l11.forEach(System.out::println);

	}


}
