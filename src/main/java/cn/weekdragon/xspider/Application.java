package cn.weekdragon.xspider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application {

	static final  Logger log = LoggerFactory.getLogger(Application.class);
	public static ApplicationContext applicationContext;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		
	}

}
