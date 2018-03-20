package cn.weekdragon.xspider.film;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.weekdragon.xspider.entity.Film;

public class PNiaoSpider implements ISpider{

	final Logger log = LoggerFactory.getLogger(PNiaoSpider.class);
	//http://www.pniao.com/Mov/main/pn8.html
	//http://www.pniao.com/Mov/one/44543.html
	
	private String listBegin = "http://www.pniao.com/Mov/main/pn1.html";
	private int pageIndex = 1;
	private int pageTotal = -1;
	private String currentPageUrl;

	public String getNextPageUrl() {
		int lastIndexOf = currentPageUrl.lastIndexOf("/");
		String nextPageUrl = null;
		if(pageIndex<pageTotal) {
			//nextPageUrl = currentPageUrl.substring(0, lastIndexOf)+"/pn" + ++pageIndex + ".html";
		}
		return nextPageUrl;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void firstGetAll() throws Exception {
		currentPageUrl = listBegin;
		while(currentPageUrl!=null) {
			log.info("访问页面{url = {}, index = {}, total = {}}",currentPageUrl,pageIndex,pageTotal);
			Document doc = Jsoup.connect(currentPageUrl).get();
			//
			if(pageTotal == -1) {
				Element totalNum = doc.select("div.mainPage :nth-child(10)").first();
				String href = totalNum.toString();
				System.out.println("总数："+href);
				Pattern num = Pattern.compile("\\d+");
				Matcher matcher = num.matcher(href);
				if(matcher.find()) {
					pageTotal = Integer.parseInt(matcher.group());
					System.out.println(pageTotal);
				}
			}
			Elements moviesContainer = doc.select("#main_outer > div.mainContainer > div.movies");
			List movies = moviesContainer.select("div.eachOne");
			if( movies == null) {
				log.debug("抓取列表数据失败");
				return;
			}
			int size = movies.size();
			System.out.println(size);
			movies.parallelStream().forEach(movieL->{
				Element movie = (Element) movieL;
				Element titleAndDetail = movie.select("div.titleInnner").first().select("a").first();
				if(titleAndDetail==null) {
					log.debug("抓取列项数据失败");
					return;
				}
				String title = titleAndDetail.text();
				String detailUrl = titleAndDetail.attr("href");
				Element info = movie.select("div.info").first();
				String showTime = info.select(":nth-child(2)").first().select("a").first().text();
				
				String categorys = info.select("li[class!=year]:has(a[rel])").text();
				Element bottomInfo = movie.select("div.bottomInfo").first();
				String rank = bottomInfo.select("div.leftInfo > li:eq(0)").text();
				
				String imgUrl = movie.select("div.left > div.thumb > a > img").first().attr("data-url");
				
				if(true)return;
				Film film = new Film();
				film.setFullTitle(title);
				film.setShortTitle(title);
				film.setDetailUrl(detailUrl);
				if(showTime.length()==11) {
					film.setShowTime(showTime);
				}
				film.setCategory(categorys);
				film.setRank(rank);
				film.setImgUrl(imgUrl);
				
				System.out.println(film);
			});
			currentPageUrl=getNextPageUrl();
			log.info("下一页面:{}",currentPageUrl);
		}
	}


	public void getToday() {
		// TODO Auto-generated method stub
		
	}


	public String getSpiderInfo() {
		return "PNiaoSpider";
	}
	
}
