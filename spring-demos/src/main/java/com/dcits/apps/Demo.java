package com.dcits.apps;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.beans.AopDemo;
import com.dcits.beans.BookInfo;
import com.dcits.beans.CacheDemo;
import com.dcits.beans.UserInfo;
import com.dcits.cache.CacheUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
		//testAop();
		//testCacheScript();

		//testFastjson();

		//testThreadLocal();

		testFuture();
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
		int i = 0;
		while (true) {
			i ++;
			UserInfo uinfo = cacheDemo.getUserInfoById(3L);
			LOGGER.info("get user cache:{},{}", uinfo.getName(),uinfo.getAge());
			//cacheDemo.setName("kxw-"+i);

			BookInfo binfo = cacheDemo.getBookInfoById("33");

			LOGGER.info("get book cache:{},{}", binfo.getTitle(),binfo.getPrice());


			if(i == 3){
				uinfo.setName("999");
				binfo.setTitle("book999");
				cacheDemo.updateBookInfoById(binfo);
				cacheDemo.updateUserInfo(uinfo);

			}
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
	public static void testCacheScript()
	{
		CacheUtils cu = new CacheUtils("uu");
		cu.testScript();
	}


	public static class JsonClass{
		private  String key1;

		public String getKey1() {
			return key1;
		}

		public void setKey1(String key1) {
			this.key1 = key1;
		}

		public String getKey2() {
			return key2;
		}

		public void setKey2(String key2) {
			this.key2 = key2;
		}

		private String key2;

	}
	public static void testFastjson()
	{
		JSONObject myJson = new JSONObject();
		myJson.put("key1", "val1");
		myJson.put("key2", "val2");

		LOGGER.info("get json:{},{}", myJson.toString(),myJson.toJSONString());
		String jstr = myJson.toJSONString();
		JsonClass jc = JSON.parseObject(jstr,JsonClass.class);

		LOGGER.info("get class:{},{}", jc.getKey1(),jc.getKey2());



	}

	static ThreadLocal<Map<String, Object>> thdData = new ThreadLocal<Map<String, Object>>(){

		@Override
		protected Map<String, Object> initialValue(){

			return new HashMap<String,Object>();

		}

	};


	public static void testThreadLocal()
	{
		thdData.get().put("key1", "val1");
		Stack<String> st = new Stack<String>();
		thdData.get().put("stk1", st);
		st.push("v1");
		st.push("v2");
		st.push("v3");
		Thread th  = new Thread(()->
		{
			LOGGER.info("in thread,{},{}", thdData.get().get("key1"), thdData.get().get("stk1"));

		});
		th.start();
		Stack<?> stf = (Stack<String>)thdData.get().get("stk1");
		LOGGER.info("out thread,{},{},{}, {}", thdData.get().get("key1"), stf.pop(),stf.peek(),stf.size());

		try {
			th.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	public static void testFuture()
	{
		ThreadFactory namedThreadFactory = new NamedThreadFactory("BatchSplitWorker");
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 60, TimeUnit.SECONDS, queue, namedThreadFactory);
		List<Future<String>> allRet = new ArrayList<>();
		List<Integer> ind = new ArrayList<>();
		ind.add(1);
		ind.add(2);
		ind.add(3);
		ind.add(4);
		ind.add(5);
		int size = 5;
		/*for(int i = 0; i < size; i ++) {
			final int index = i;
			allRet.add(
					executor.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					LOGGER.info("callable finish,{}", index);
					return ("oo_"+index);
				}
			})
			);
		}*/


		ind.forEach(o->{
			final int index = o.intValue();
			allRet.add(
					executor.submit(()->{
							try {
								Thread.sleep(1000);
							}
							catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							LOGGER.info("callable finish,{}", index);
							return ("oo_"+index);
					})
			);
		});




		LOGGER.info("main cointinue");

		allRet.forEach(o -> {
			try {
				LOGGER.info("get:{}", o.get());
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
			catch (ExecutionException e) {
				e.printStackTrace();
			}
		});

		LOGGER.info("return;");


	}
}

