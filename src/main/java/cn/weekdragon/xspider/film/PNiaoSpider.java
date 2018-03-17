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
			nextPageUrl = currentPageUrl.substring(0, lastIndexOf)+"pn" + ++pageIndex + ".html";
		}
		return nextPageUrl;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void firstGetAll() throws Exception {
		currentPageUrl = listBegin;
		while(currentPageUrl!=null) {
			Document doc = Jsoup.connect(currentPageUrl).get();
			//
			Elements totalNum = doc.select("div.mainPage > li(10)");
			System.out.println(totalNum);
			String href = totalNum.attr("href");
			Pattern num = Pattern.compile("\\d.");
			Matcher matcher = num.matcher(href);
			if(matcher.find()) {
				pageTotal = Integer.parseInt(matcher.group());
				System.out.println(pageTotal);
			}
			
			Elements select = doc.select("#main_outer > div.mainContainer > div.movies");
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
				Film film = new Film();
				film.setFullTile(title);
				film.setDetailUrl(detailUrl);
				System.out.println(film);
			});
			currentPageUrl=getNextPageUrl();
			System.out.println(currentPageUrl);
		}
	}


	public void getToday() {
		// TODO Auto-generated method stub
		
	}


	public String getSpiderInfo() {
		return "PNiaoSpider";
	}
	
}
