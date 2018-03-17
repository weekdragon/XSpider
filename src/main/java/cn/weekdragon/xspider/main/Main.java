package cn.weekdragon.xspider.main;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weekdragon.xspider.film.ISpider;
import cn.weekdragon.xspider.film.PNiaoSpider;

public class Main {

	static final  Logger log = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
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

}
