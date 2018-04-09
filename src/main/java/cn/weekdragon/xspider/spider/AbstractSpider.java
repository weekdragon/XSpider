package cn.weekdragon.xspider.spider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSpider implements ISpider{

	final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public String getNextPageUrl(String currentPageUrl, int pageIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void firstGetAll() throws Exception {
		// TODO Auto-generated method stub
		
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
