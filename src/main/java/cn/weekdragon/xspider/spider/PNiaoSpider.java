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
public class PNiaoSpider implements ISpider{

	final Logger log = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private FilmService filmService;
	private String listBegin = "http://www.pniao.com/Mov/main/pn1.html";
	private int pageTotal = -1;
	
	//假设电影网站一次更新页面导致页面增加的数量不超过2页，每次监控前两页就可以得到所有最新的电影
	private int IncreasedPageSize = 2;

	public String getNextPageUrl(String currentPageUrl,int pageIndex) {
		int lastIndexOf = currentPageUrl.lastIndexOf("/");
		String nextPageUrl = null;
		if(pageIndex <= pageTotal) {
			nextPageUrl = currentPageUrl.substring(0, lastIndexOf)+"/pn" + pageIndex + ".html";
		}
		return nextPageUrl;
	}

	@PostConstruct
	public void firstGetAll() {
		log.info("[time:[{}, {}抓取所有任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		//fetchPage(Integer.MAX_VALUE);
		log.info("[time:[{}, {}抓取所有任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
	}

	//@Scheduled(cron="0 0/1 * * * ? ")   //每1分钟执行一次
	public void getToday() {
		log.info("[time:[{}, {}定时抓取任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		fetchPage(IncreasedPageSize);
		log.info("[time:[{}, {}定时抓取任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
	}
	
	@Override
	public void fetchPage(int pageSize) {
		int pageIndex = 1;
		String currentPageUrl = listBegin;
		while(currentPageUrl!=null && pageIndex <= pageSize) {
			log.info("访问页面{url = {}, index = {}, total = {}}",currentPageUrl,pageIndex,pageTotal);
			Document doc = null;
			try {
				doc = Jsoup.connect(currentPageUrl).get();
			} catch (IOException e2) {
				log.error("访问{}的网页[{}]失败",getSpiderInfo(),currentPageUrl);
				return;
			}
			//
			if(pageTotal == -1) {
				Element totalNum = doc.select("div.mainPage :nth-child(10)").first();
				String href = totalNum.toString();
				Pattern num = Pattern.compile("\\d+");
				Matcher matcher = num.matcher(href);
				if(matcher.find()) {
					pageTotal = Integer.parseInt(matcher.group());
				}
				System.out.println("总数："+pageTotal);
			}
			Elements moviesContainer = doc.select("div.movies");
			Elements movies = moviesContainer.select("div.eachOne");
			if( movies == null) {
				log.debug("抓取列表数据失败");
				return;
			}
			movies.parallelStream().forEach(movie->{
				Element titleAndDetail = movie.select("div.movTitle").first().select("a").first();
				if(titleAndDetail==null) {
					log.debug("抓取列项数据失败");
					return;
				}
				String title = titleAndDetail.text();
				String detailUrl = titleAndDetail.attr("href");
				Element info = movie.select("div.info").first();
				String showTime = info.select("ul:eq(5)>li:eq(0)").text();
				if(showTime.length()>5) {
					showTime = showTime.substring(5);
				}
				String categorys = info.select("ul:eq(2)>li:gt(0)").text();
				String rank = info.select("ul:eq(4)>li:eq(1)").text();
				
				String imgUrl = movie.select("div.left > div.thumb > a > img").first().attr("data-url");
				
				String briefCnt = "暂无";
				try {
					
					briefCnt = Jsoup.connect(detailUrl).get().select("div.mainContainer > div.movieOne>div.briefOuter>div.briefCnt").text();
					
				} catch (IOException e1) {
					log.info("获取简介失败:{}",e1);
				}
				
				
				Film film = new Film();
				film.setWebSiteFlag(Constants.WEB_SITE_FLAG_PANIAO);
				film.setFullTitle(title);
				film.setShortTitle(title);
				film.setDetailUrl(detailUrl);
				film.setBriefCnt(briefCnt);
				if(showTime.length()==10) {
					film.setShowTime(showTime);
				}
				film.setCategory(Arrays.asList(categorys.split(" ")));
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

	public String getSpiderInfo() {
		return "PNiaoSpider";
	}
	
}
