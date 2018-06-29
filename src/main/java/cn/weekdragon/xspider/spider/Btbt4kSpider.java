package cn.weekdragon.xspider.spider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.domain.DownloadLinks;
import cn.weekdragon.xspider.util.Constants;

@Component
public class Btbt4kSpider extends AbstractSpider {

	//http://www.btbt4k.com/home-popular-list-version2018040809-offset1976-count24.js
	//http://www.btbt4k.com/home-latest-list-version20160310004645-offset24-count24.js
	private String apiPage = "http://www.btbt4k.com/home-latest-list-version20160310004645-offset{offset}-count24.js";
	private String listBegin = apiPage.replace("{offset}", "0");
	private int pageTotal = -1;
	
	
	@Override
	public void init() {
		increasedPageSize = 1;
		host = "http://www.btbt4k.com/";
	}
	

	
	public String getNextPageUrl(String currentPageUrl, int pageIndex) {
		return  apiPage.replace("{offset}", "" + pageIndex*24);
	}

	@Scheduled(cron="0 0 0/1 * * ? ")   //每1小时执行一次
	public void getToday() {
		log.info("[time:[{}, {}定时抓取任务开始]",System.currentTimeMillis()/1000,getSpiderInfo());
		fetchPage(increasedPageSize);
		log.info("[time:[{}, {}定时抓取任务结束]",System.currentTimeMillis()/1000,getSpiderInfo());
	}

	public void fetchPage(int pageSize) {
		int pageIndex = 0;
		String currentPageUrl = listBegin;
		while(currentPageUrl!=null && pageIndex <= pageSize) {
			log.info("访问页面{url = {}, index = {}, total = {}}",currentPageUrl,pageIndex,pageTotal);
			final Document doc;
			try {
				String body = Jsoup.connect(currentPageUrl).timeout(10000).execute().body();
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
				log.info("抓取列表数据失败");
				return;
			}
			movies.stream().forEach(movie->{
				Element titleAndDetail = movie.select("div.htl_item_info_hd").first();
				if(titleAndDetail==null) {
					log.info("抓取列项数据失败");
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
					int b = showTime.indexOf("(");
					int e = showTime.indexOf(")");
					if(b>=0&&e>=0) {
						categorys = showTime.substring(e+1);
						showTime = showTime.substring(b,e+1);
					}else {
						showTime = "未知";
						categorys = "未知";
					}
				}
				String rank = unicodeToString(titleAndDetail.select("span.htl_item_stars").text());
				String imgUrl = movie.select("div.htl_item_pic_hd > img").first().attr("src");
				if(imgUrl.startsWith("/d/file")) {
					imgUrl = host  + imgUrl;
				}
				Film film = new Film();
				String briefCnt = "暂无";
				List<DownloadLinks> sourceUrls = new ArrayList<>();
				int maxTryCount = 3;
				while(maxTryCount>0) {
					try {
						if(detailUrl.startsWith("/")) {
							detailUrl = host + detailUrl;
						}
						detailUrl = detailUrl.replace("www.bbtt4k.com", "www.btbt4k.com");
						Document detailsInfo = Jsoup.connect(detailUrl).timeout(10000).get();
						briefCnt = detailsInfo.select("#storyline_val > p:nth-child(1)").text();
						Elements downloads = detailsInfo.select(".dl_item_cell.dl_item_cell_dld a");
						if(downloads!=null) {
							for(Element element:downloads) {
								String href = element.attr("href");
								if(!"javascript:;".equals(href)) {
									continue;
								}
								String ref = element.attr("data-ref");
								DownloadLinks downloadLinks = new DownloadLinks();
								downloadLinks.setType("BT");
								downloadLinks.setDescription(element.parent().nextElementSibling().text());
								downloadLinks.setLink(host+"down/" + ref + ".html");
								downloadLinks.setFilm(film);
								sourceUrls.add(downloadLinks);
							}
						}
						if(briefCnt.length()>1024) {
							briefCnt = briefCnt.substring(0,1023);
						}
						briefCnt = briefCnt.replaceAll("【.*】", "").replaceAll("◎.*", "");
						break;
					} catch (Exception e1) {
						maxTryCount--;
						try {
							log.error("第{}次获取详情失败，暂停 1 分钟后重试 :Exception = {}, detailUrl = {}",3-maxTryCount, e1, detailUrl);
							Thread.sleep(60000);
						} catch (InterruptedException e) {}
					}
				}
				
				film.setWebSiteFlag(Constants.WEB_SITE_FLAG_BTBT4K);
				film.setFullTitle(title);
				film.setShortTitle(title);
				film.setDetailUrl(detailUrl);
				film.setBriefCnt(briefCnt);
				film.setShowTime(showTime);
				film.setCategory(Arrays.asList(categorys.split(" |/")));
				film.setSourceUrl(sourceUrls);
				film.setRank(rank);
				film.setImgUrl(imgUrl);
				
				try {
					filmService.insertDistinctFilm(film);
				} catch (Exception e) {
					log.error("保存film失败:{},detail:{}",film,e);
				}
				
			});
			currentPageUrl=getNextPageUrl(currentPageUrl,++pageIndex);
			log.info("下一页面:{}",currentPageUrl);
		}
	}
}
