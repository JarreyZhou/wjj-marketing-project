package com.wjj.application.util;

import java.util.List;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

@Component("redisClient")
@Slf4j
public class RedisClient {
	
	   @Autowired
	    private JedisPool jedisPool;
	   
	   /**
		 * 设置缓存
		 * @param key 键
		 * @param value 值
		 * @param cacheSeconds 超时时间，0为不超时
		 * @return
		 * @throws Exception 
		 */
		public  String setex(String key, String value, int cacheSeconds) throws Exception {
			String result = null;
			Jedis jedis = null;
			try {
				jedis = jedisPool.getResource();
				if(cacheSeconds>0){
					result = jedis.setex(key, cacheSeconds, value);
				}else{
					result = jedis.set(key, value);
				}
				log.debug("set {} = {}", key, value);
			} catch (Exception e) {
				log.error("set {} = {}", key, value, e);
				throw e;
			} finally {
				jedis.close();
			}
			return result;
		}

	/**
	 * 设置键值和超时时间
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return 0是失败，1为成功
	 * @throws Exception
	 */
	public  Long setnx(String key, String value,int time) throws Exception {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Transaction tx = jedis.multi();
			tx.setnx(key, value);
			tx.expire(key, time);
			List<Object> result=tx.exec();
			if (result == null || result.isEmpty()) {
				return null;
			}
			Long re= (Long) result.get(0);
			return re;
		} catch (Exception e) {
			log.warn("set {} = {}", key, value, e);
			throw e;
		} finally {
			jedis.close();
		}
	}
	 
	    public void set(String key, String value) throws Exception {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.set(key, value);
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    
	    public void del(String key) throws Exception  {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.del(key);
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    
	 
	    public String get(String key) throws Exception  {
	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            return jedis.get(key);
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    
	    public Long setInc(String key) throws Exception  {
	   	 
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            Long incr = jedis.incr(key);
	            return incr;
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }

	    public void setTimes(String key, String value,int times) throws Exception {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            jedis.set(key, value);
	            jedis.expire(key, times);
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    
	    
	    public  void setMap(String mapName,String keyName,String valueName) {
	    	 Jedis jedis = null;
		        try {
		            jedis = jedisPool.getResource();
		            jedis.hsetnx(mapName, keyName, valueName);
		        } finally {
		            //返还到连接池
		            jedis.close();
		        }
	    }
	    
	    public String getMap(String mapName,String keyName) {
	    	 Jedis jedis = null;
		        try {
		            jedis = jedisPool.getResource();
		            String hget = jedis.hget(mapName, keyName);
		            return hget;
		        } finally {
		            //返还到连接池
		            jedis.close();
		        }
	    }
	    public Long setIncAndTime(String key,int times) throws Exception  {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            Long incr = jedis.incr(key);
	            jedis.expire(key, times);
	            return incr;
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    public Long setdelkeys(String key) throws Exception  {
	        Jedis jedis = null;
	        try {
	            jedis = jedisPool.getResource();
	            Long incr = jedis.decr(key);
	           
	            return incr;
	        } finally {
	            //返还到连接池
	            jedis.close();
	        }
	    }
	    
	    
	    
	    // 向list 插入
	    public void setList(String listName,String itemStr) {
	    	  Jedis jedis = null;
		        try {
		            jedis = jedisPool.getResource();
		            jedis.rpush(listName, itemStr);
		        } finally {
		            //返还到连接池
		            jedis.close();
		        }
	    }
	    
	    // 向list 取出数据
	    public List<String> getList(String listName,Long beginItem,Long lastItem) {
	    	  Jedis jedis = null;
	    	  List<String> list=null;
		        try {
		            jedis = jedisPool.getResource();
		            list = jedis.lrange(listName, beginItem, lastItem);
		            
		        } finally {
		            //返还到连接池
		            jedis.close();
		        }
		        return list;
	    }

	/**
	 *设置增长key超时时间
	 * @param key 键
	 * @return 值
	 */
	public Long setIncAndTime2(String key,int times) throws Exception  {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			Transaction tx = jedis.multi();
			tx.incr(key);
			tx.expire(key, times);
			List<Object> result=tx.exec();
			if (result == null || result.isEmpty()) {
				return null;
			}
			Long re= (Long) result.get(0);
			return re;
		} finally {
			//返还到连接池
			jedis.close();
		}
	}

	/**
	 *判断key是否存在
	 * @param key 键
	 * @return 值
	 */
	public boolean exists(String key) throws Exception  {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			boolean result = jedis.exists(key);
			return result;
		} finally {
			//返还到连接池
			jedis.close();
		}
	}
}
