package cn.weekdragon.xspider.film;


public interface ISpider {

	String getNextPageUrl();
	void firstGetAll() throws Exception;
	void getToday();
	String getSpiderInfo();
}
