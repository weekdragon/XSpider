package cn.weekdragon.xspider.spider;


public interface ISpider {

	String getNextPageUrl(String currentPageUrl,int pageIndex);
	void firstGetAll() throws Exception;
	void getToday();
	void fetchPage(int pageSize);
	String getSpiderInfo();
}
