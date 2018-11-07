package com.dcits.apps;


import com.dcits.beans.AopDemo;
import com.dcits.beans.CacheDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by kongxiangwen on 11/7/18 w:45.
 */
public class Demo {
	private final static Logger LOGGER = LoggerFactory.getLogger(Demo.class);

	public static void main(String[] args) {
		//testCache();
		testAop();
	}

	public static void testAop()
	{
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"app.xml", "classpath*:META-INF/spring/*.xml"});
		CacheDemo cacheDemo = appContext.getBean(CacheDemo.class);
		AopDemo aopDemo = appContext.getBean(AopDemo.class);

		while (true) {

			/*LOGGER.info("get cache:{},{}", cacheDemo.getName(),cacheDemo.getAge());
			try {
				Thread.sleep(300);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}*/

			LOGGER.info("get aop:{},{}", aopDemo.getName(),aopDemo.getAge());
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testCache()
	{
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"app.xml", "classpath*:META-INF/spring/*.xml"});
		CacheDemo cacheDemo = appContext.getBean(CacheDemo.class);
		AopDemo aopDemo = appContext.getBean(AopDemo.class);

		while (true) {

			LOGGER.info("get cache:{},{}", cacheDemo.getName(),cacheDemo.getAge());
			try {
				Thread.sleep(300);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}

			/*LOGGER.info("get aop:{},{}", aopDemo.getName(),aopDemo.getAge());
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}
}

