package cn.weekdragon.xspider.spider;


public interface ISpider {

	String getNextPageUrl();
	void firstGetAll() throws Exception;
	void getToday();
	String getSpiderInfo();
}
