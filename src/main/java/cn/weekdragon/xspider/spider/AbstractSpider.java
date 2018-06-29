package cn.weekdragon.xspider.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import cn.weekdragon.xspider.service.FilmService;

public abstract class AbstractSpider implements ISpider{
	
	@Autowired
	public FilmService filmService;
	public int increasedPageSize = 5;
	@Value("${xspider.fetchAll}")
	public boolean fetchAll;
	public String host="";
	
	final Logger log = LoggerFactory.getLogger(this.getClass());
	
	public abstract void init();
	public abstract String getNextPageUrl(String currentPageUrl, int pageIndex);
	public abstract void fetchPage(int pageSize);

	@Async
	@PostConstruct
	public void firstGetAll() {
		try {
			init();
			if(fetchAll){
				log.info("[time:[{}, {}抓取所有任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
				fetchPage(Integer.MAX_VALUE);
				log.info("[time:[{}, {}抓取所有任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
			}else {
				getToday();
				log.info("[time:[{}, {}跳过抓取所有]",System.currentTimeMillis()/1000,getSpiderInfo());
			}
		}catch (Exception e) {
			log.error("[抓取失败:{}]",e);
		}
		
	}
	
	@Scheduled(cron="0 0 0/1 * * ? ")   //每1小时执行一次
	public void getToday() {
		log.info("[time:[{}, {}定时抓取任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		fetchPage(increasedPageSize);
		log.info("[time:[{}, {}定时抓取任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
	}

	@Override
	public String getSpiderInfo() {
		return this.getClass().getName();
	}

	public static String unicodeToString(String str) {  
	    Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");  
	    Matcher matcher = pattern.matcher(str);  
	    char ch;  
	    while (matcher.find()) {  
	        String group = matcher.group(2);  
	        ch = (char) Integer.parseInt(group, 16);  
	        String group1 = matcher.group(1);  
	        str = str.replace(group1, ch + "");  
	    }  
	    return str;  
	}  
}
