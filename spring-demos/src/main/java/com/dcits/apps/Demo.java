package com.dcits.apps;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcits.beans.AopDemo;
import com.dcits.beans.BookInfo;
import com.dcits.beans.CacheDemo;
import com.dcits.beans.DaoDemo;
import com.dcits.beans.Hint;
import com.dcits.beans.UserInfo;
import com.dcits.beans.ZkGetMasterBean;
import com.dcits.beans.ZkLockBean;
import com.dcits.cache.CacheUtils;
import com.dcits.daos.TestDao;
import com.dcits.exceptions.GalaxyException;
import java.io.IOException;
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
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

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

		//testFuture();

		//testMybatis();

		//Demo d = new Demo();
		//d.testXmlParser();

		//testApplicationListener();
		//testZk();

		testGetMaster();


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
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"app.xml", "classpath*:META-INF/spring/cache.xml"});
		CacheDemo cacheDemo = appContext.getBean(CacheDemo.class);
		int i = 0;
		while (true) {
			i ++;
			UserInfo uinfo = cacheDemo.getUserInfoById(3L);
			LOGGER.info("get user cache:{},{}", uinfo.getName(),uinfo.getAge());
			//cacheDemo.setName("kxw-"+i);

			/*BookInfo binfo = cacheDemo.getBookInfoById("33");
			LOGGER.info("get book cache:{},{}", binfo.getTitle(),binfo.getPrice());*/


			if(i == 3){


				uinfo.setName("999");
				cacheDemo.updateUserInfo(uinfo);


				/*binfo.setTitle("book999");
				cacheDemo.updateBookInfoById(binfo);*/


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



	public static void testMybatis()
	{
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"db.xml"});
		TestDao testDao = appContext.getBean(TestDao.class);
		DaoDemo dd = new DaoDemo();
		dd.setId(111);
		DaoDemo info = testDao.findById(111);
		LOGGER.info("get dao:{},{}", testDao.getCount(dd),info.toString());

		List<DaoDemo> linfo = testDao.getLimit(3);
		linfo.forEach(o -> LOGGER.info(o.toString()));
	}






//-------------------------


	private static Hint parseHint(Element element) {
		 final  String HINT_SDB = "sdb";
		 final  String HINT_TS = "ts";
		Hint hint = new Hint();
		String sdbComment = element.getAttribute(HINT_SDB);
		if (sdbComment != null && sdbComment.length() > 0) {
			hint.setSdb(sdbComment);
		}
		String tsComment = element.getAttribute(HINT_TS);
		if (tsComment != null && tsComment.length() > 0) {
			hint.setTs(tsComment);
		}
		return hint;
	}

	private static List<String> parseSqlmaps(NodeList nodes) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node instanceof Element) {
				Text text = (Text) node.getFirstChild();
				list.add(text.getData().trim());
			}
		}
		return list;
	}
	public  DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	//	new SimpleErrorHandler(logger)
		factory.setNamespaceAware(true);
		final int VALIDATION_NONE = 0;
		final int VALIDATION_DTD = 1;
		final int VALIDATION_XSD = 2;
		final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
		final String XSD_SCHEMA_LANGUAGE = "http://www.w3.org/2001/XMLSchema";
		//EntityResolver = resolver = ClasspathEntityResolver.getInstance()

		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		//docBuilder.setErrorHandler(errorHandler);
		return docBuilder;

	}
	public  void testXmlParser()
	{
		Map<String, Hint> mapping = new HashMap<>();
		 Resource[] locations = null;
		DocumentBuilder builder = null;
		 final ResourcePatternResolver resourceResolver;
		resourceResolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
		Logger logger = LoggerFactory.getLogger(Demo.class);

		try {
			locations = resourceResolver.getResources("classpath*:/hint/*.xml");
		}catch (IOException e){
		}

		if(locations != null) {
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				builder = factory.newDocumentBuilder();
			}
			catch (ParserConfigurationException e) {
				e.printStackTrace();
			}

			for (Resource resource : locations) {
				if (!resource.exists() || !resource.isReadable()) {
					throw new GalaxyException("can't read hint xml file:" + resource.toString());
				}
				try {

					Document doc = builder.parse(resource.getInputStream());
					Element root = doc.getDocumentElement();
					NodeList hints = root.getChildNodes();
					int size = hints.getLength();
					for (int i = 0; i < size; i++) {
						Node e = hints.item(i);
						if (e instanceof Element) {
							Hint hint = parseHint((Element) e);
							List<String> sqlmaps = parseSqlmaps(e.getChildNodes());
							for (String sqlmap : sqlmaps) {
								mapping.put(sqlmap, hint);
							}
						}
					}

				}
				catch (Exception e) {
					;
				}
			}
		}
			mapping.forEach((k,hint)->LOGGER.info("hint:{},{}", k,hint.toString()));

			String containStr = "com.dcits.galaxy";
			long count = mapping.entrySet().stream()
					.filter(x->x.getKey().contains(containStr))
					.count();
			LOGGER.info("count contains {}:{}",containStr,count);


		Map<String, Hint> containedMapping =
				mapping.entrySet().stream()
				.filter(x->x.getKey().contains(containStr))
				.collect(Collectors.toMap(x->x.getKey(), x->x.getValue()));


		containedMapping.forEach((k,hint)->LOGGER.info("collected hint:{},{}", k,hint.toString()));


	}





	//----------

	public static void testApplicationListener()
	{
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"app-annotations.xml"});
	}


	//--------------
	public static void testZk() {


		//zkClient.delete(path);
		//zkClient.createEphemeral(lockPath,lockValue);
		int size = 5;
		ThreadFactory namedThreadFactory = new NamedThreadFactory("BatchSplitWorker");
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 15, 60, TimeUnit.SECONDS, queue, namedThreadFactory);

		List<Future<String>> allRet = new ArrayList<>();
		ZkLockBean zk = new ZkLockBean();
		for (int i = 0; i < size; i++) {
			final int index = i;
			allRet.add(
					executor.submit(new Callable<String>() {
						@Override
						public String call() throws Exception {

							//ZkLockBean zk = new ZkLockBean();
							boolean isLock = zk.lock();
							if (isLock) {

								LOGGER.info("callable success-------------------{}", index);
								try {
									Thread.sleep(1000);
								}
								catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								zk.unlock();
							}else{
								LOGGER.info("callable failed-------------------{}", index);
							}

							return ("oo_" + index);
						}
					})
			);




		}


		/*for (int i = 0; i < size; i++) {

			new Thread(()->{
				boolean isLock = zk.lock();
				if (isLock) {

					LOGGER.info("callable can be finish-------------------{}", Thread.currentThread().getId());
					try {
						Thread.sleep(1000);
					}
					catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					zk.unlock();
				}
			}
			).start();
		}*/




	}



	public static void testGetMaster() {


		//zkClient.delete(path);
		//zkClient.createEphemeral(lockPath,lockValue);
		int size = 5;
		ThreadFactory namedThreadFactory = new NamedThreadFactory("BatchSplitWorker");
		BlockingQueue<Runnable> queue = new LinkedBlockingQueue<Runnable>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 15, 60, TimeUnit.SECONDS, queue, namedThreadFactory);

		List<Future<ZkGetMasterBean>> allRet = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			final int index = i;
			allRet.add(
					executor.submit(new Callable<ZkGetMasterBean>() {
						@Override
						public ZkGetMasterBean call() throws Exception {

							ZkGetMasterBean zk = new ZkGetMasterBean();
							boolean isLock = zk.TryMaster();
							if (isLock) {

								//LOGGER.info("callable success, master-------------------{}", index);
								/*try {
									Thread.sleep(1000);
								}
								catch (InterruptedException e1) {
									e1.printStackTrace();
								}
								zk.CancelMaster();*/
							}
							else {
								//LOGGER.info("callable failed-------------------{}", index);
							}

							return zk;
						}
					})
			);
		}

			while(true) {
				for (int j = 0; j < size; j++) {
					try {
						ZkGetMasterBean zk = allRet.get(j).get();
						if(zk.isMaster()){
							LOGGER.info("current master-------------------:{}", j);
							zk.CancelMaster();
						}
					}
					catch (InterruptedException e) {
						e.printStackTrace();
					}
					catch (ExecutionException e) {
						e.printStackTrace();
					}
				}

				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}





	}


}

