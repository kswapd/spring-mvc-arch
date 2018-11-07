package com.dcits.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by kongxiangwen on 11/6/18 w:45.
 */
@CacheConfig(cacheNames="default")
public class CacheDemo {


	private final static Logger LOGGER = LoggerFactory.getLogger(CacheDemo.class);

	/*private String name = "cache";
	private int age = 55;
	@Cacheable(value="nameCache")
	public String getName() {
		LOGGER.info("querying name from db");
		return name;
	}

	//@CacheEvict(value="nameCache",key="#account.getName()")
	@CacheEvict(value="nameCache")
	public void setName(String name) {
		this.name = name;
	}


	//@Cacheable(value="myCache")
	@Cacheable
	public int getAge() {
		LOGGER.info("querying age from db");
		return age;
	}




	public void setAge(int age) {
		this.age = age;
	}*/


	@Cacheable
	public UserInfo getUserInfoById(Long id)
	{

		LOGGER.info("querying user info:{}", id);
		return new UserInfo("kxw-"+id, 55, id);
	}


	@Cacheable
	public BookInfo getBookInfoById(String id)
	{

		LOGGER.info("querying book info:{}", id);
		return new BookInfo("title-"+id, (float)22.0, id);
	}

	@CacheEvict(value="default",key="#info.getId()")
	public void updateUserInfo(UserInfo info)
	{
		return;
	}

	@CacheEvict(value="default",key="#info.getId()")
	public void updateBookInfoById(BookInfo info)
	{
		return;
	}


}
