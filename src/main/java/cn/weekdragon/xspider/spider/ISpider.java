package cn.weekdragon.xspider.spider;


public interface ISpider {
	void firstGetAll() throws Exception;
	void getToday();
	String getSpiderInfo();
}
