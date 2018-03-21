package cn.weekdragon.xspider;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.weekdragon.xspider.spider.ISpider;
import cn.weekdragon.xspider.spider.PNiaoSpider;

@SpringBootApplication
public class Application {

	static final  Logger log = LoggerFactory.getLogger(Application.class);
	public static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {

			@Override
			public void onApplicationEvent(ContextRefreshedEvent arg0) {
				applicationContext = arg0.getApplicationContext();
				List<ISpider> spiders = new ArrayList<ISpider>();
				spiders.add(new PNiaoSpider());
				for(ISpider spider:spiders) {
					try {
						spider.firstGetAll();
					} catch (Exception e) {
						log.error(spider.getSpiderInfo() + " firstGetAll failed",e);
					}
				}
			}
			
		});
		springApplication.run(args);
	}

}
