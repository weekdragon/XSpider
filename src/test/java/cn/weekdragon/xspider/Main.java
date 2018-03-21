package cn.weekdragon.xspider;

import java.util.ArrayList;
import java.util.List;

import cn.weekdragon.xspider.spider.ISpider;
import cn.weekdragon.xspider.spider.PNiaoSpider;

public class Main {
	public static void main(String[] args) {
		List<ISpider> spiders = new ArrayList<ISpider>();
		spiders.add(new PNiaoSpider());
		for(ISpider spider:spiders) {
			try {
				spider.firstGetAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	}
}
