package dcits.apps;

/**
 * Created by kongxiangwen on 5/21/18 w:21.
 */
import dcits.models.People;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
//@ImportResource(locations={"classpath*:new-schemas.xml", "META-INF/**/*.xml"})
@ImportResource("classpath:new-schemas.xml")
@ComponentScan("dcits")
public class Application implements ApplicationContextAware {

	public static ApplicationContext context;
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);



	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			context = applicationContext;
		People p = context.getBean(People.class);
		System.out.println("---------"+p.getAge());
	}
}
