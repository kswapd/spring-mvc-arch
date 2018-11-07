package com.dcits.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;

/**
 * Created by kongxiangwen on 11/6/18 w:45.
 */

public class CacheDemo {

	private final static Logger LOGGER = LoggerFactory.getLogger(CacheDemo.class);

	private String name = "cache";
	private int age = 55;
	@Cacheable(value="myCache")
	public String getName() {
		LOGGER.info("querying db");
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
