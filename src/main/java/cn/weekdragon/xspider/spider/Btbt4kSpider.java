package cn.weekdragon.xspider.spider;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.service.FilmService;
import cn.weekdragon.xspider.util.Constants;

@Component
public class Btbt4kSpider extends AbstractSpider {

	@Autowired
	private FilmService filmService;
	//http://www.btbt4k.com/home-popular-list-version2018040809-offset1976-count24.js
	//http://www.btbt4k.com/home-latest-list-version20160310004645-offset24-count24.js
	private String apiPage = "http://www.btbt4k.com/home-latest-list-version20160310004645-offset{offset}-count24.js";
	private String listBegin = apiPage.replace("{offset}", "0");
	private int pageTotal = -1;
	
	//假设电影网站一次更新页面导致页面增加的数量不超过 IncreasedPageSize 页，每次监控前 IncreasedPageSize 页就可以得到所有最新的电影
	private int IncreasedPageSize = 1;
	private String baseUrl = "http://www.btbt4k.com/";
	
	public String getNextPageUrl(String currentPageUrl, int pageIndex) {
		return  apiPage.replace("{offset}", "" + pageIndex*24);
	}

	@PostConstruct
	public void firstGetAll() throws Exception {
		log.info("[time:[{}, {}抓取所有任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		fetchPage(Integer.MAX_VALUE);
		log.info("[time:[{}, {}抓取所有任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
		
	}

	@Scheduled(cron="0 0 0/1 * * ? ")   //每1小时执行一次
	public void getToday() {
		log.info("[time:[{}, {}定时抓取任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		fetchPage(IncreasedPageSize);
		log.info("[time:[{}, {}定时抓取任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
	}

	public void fetchPage(int pageSize) {
		int pageIndex = 0;
		String currentPageUrl = listBegin;
		while(currentPageUrl!=null && pageIndex <= pageSize) {
			log.info("访问页面{url = {}, index = {}, total = {}}",currentPageUrl,pageIndex,pageTotal);
			final Document doc;
			try {
				String body = Jsoup.connect(currentPageUrl).execute().body();
				if("htla(null)" .equals(body)) {//没有下一页了
					currentPageUrl = null;
					log.info("下一页面:{},body:{}",currentPageUrl,body);
					continue;
				}
				body = body.substring(6, body.length()-2);
				doc = Jsoup.parse(body.replaceAll("\\\\(?!\\w)", "").replaceAll("&nbsp;", ""));
			} catch (IOException e2) {
				log.error("访问{}的网页[{}]失败",getSpiderInfo(),currentPageUrl);
				return;
			}

			Elements movies = doc.select("div.htl_item");
			if( movies == null) {
				log.debug("抓取列表数据失败");
				return;
			}
			movies.stream().forEach(movie->{
				Element titleAndDetail = movie.select("div.htl_item_info_hd").first();
				if(titleAndDetail==null) {
					log.debug("抓取列项数据失败");
					return;
				}
				
				String title = titleAndDetail.select("span[class='htl_item_name']").text();
				title = unicodeToString(title);
				
				Elements detailUrlA = movie.select("a");
				String detailUrl = "";
				if(detailUrlA!=null) {
					detailUrl = detailUrlA.first().attr("href");
				}
				
				String showTime =  unicodeToString(titleAndDetail.select("span[class='htl_item_year_types']").text());
				String categorys = "";
				if(showTime.length() > 6) {
					categorys = showTime.substring(6);
					showTime = showTime.substring(0, 6);
				}
				String rank = unicodeToString(titleAndDetail.select("span.htl_item_stars").text());
				String imgUrl = movie.select("div.htl_item_pic_hd > img").first().attr("src");
				if(imgUrl.startsWith("/d/file")) {
					imgUrl = baseUrl  + imgUrl;
				}
				
				
				String briefCnt = "暂无";
				try {
					if(detailUrl.startsWith("/")) {
						detailUrl = baseUrl + detailUrl;
					}
					briefCnt = Jsoup.connect(detailUrl).get().select("#storyline_val > p:nth-child(1)").text();
					if(briefCnt.length()>1024) {
						briefCnt = briefCnt.substring(0,1023);
					}
					briefCnt = briefCnt.replaceAll("【.*】", "").replaceAll("◎.*", "");
				} catch (Exception e1) {
					log.info("获取简介失败:Exception = {}, detailUrl = {}, doc = {}", e1, detailUrl, doc.toString());
				}
				
				Film film = new Film();
				film.setWebSiteFlag(Constants.WEB_SITE_FLAG_BTBT4K);
				film.setFullTitle(title);
				film.setShortTitle(title);
				film.setDetailUrl(detailUrl);
				film.setBriefCnt(briefCnt);
				if(showTime.length()==10) {
					film.setShowTime(showTime);
				}
				film.setCategory(Arrays.asList(categorys.split(" |/")));
				film.setRank(rank);
				film.setImgUrl(imgUrl);
				
				try {
					filmService.saveFilm(film);
				} catch (Exception e) {
					log.info("保存film失败:{},detail:{}",film,e);
				}
				
			});
			currentPageUrl=getNextPageUrl(currentPageUrl,++pageIndex);
			log.info("下一页面:{}",currentPageUrl);
		}
	}
}
