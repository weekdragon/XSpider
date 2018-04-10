package cn.weekdragon.xspider.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class AbstractSpider implements ISpider{

	@Value("${xspider.fetchAll}")
	public boolean fetchAll;
	
	final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String getNextPageUrl(String currentPageUrl, int pageIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	public void firstGetAll() {
		if(fetchAll){
			log.info("[time:[{}, {}抓取所有任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
			fetchPage(Integer.MAX_VALUE);
			log.info("[time:[{}, {}抓取所有任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
		}else {
			log.info("[time:[{}, {}跳过抓取所有]",System.currentTimeMillis()/1000,getSpiderInfo());
		}
	}

	@Override
	public void getToday() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetchPage(int pageSize) {
		// TODO Auto-generated method stub
		
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
