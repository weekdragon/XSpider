package cn.weekdragon.xspider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.service.FilmService;

@Controller
@RequestMapping("/films")
public class FilmController {

	@Autowired
	private FilmService filmService;
	
	@GetMapping
	public String listEsBlogs(
			@RequestParam(value="order",required=false,defaultValue="new") String order,
			@RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
			@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			Model model) {
 
		Page<Film> page = null;
		List<Film> list = null;
		boolean isEmpty = true; // 系统初始化时，没有博客数据
		try {
			if (order.equals("new")) { // 最新查询
				Sort sort = new Sort(Direction.DESC,"createTime"); 
				Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
				page = filmService.listNewestFilms(keyword, pageable);
			}
			
			isEmpty = false;
		} catch (Exception e) {
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = filmService.listFilms(pageable);
		}  
 
		list = page.getContent();	// 当前所在页面数据列表
 

		model.addAttribute("order", order);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("films", list);
		
		// 首次访问页面才加载
//		if (!async && !isEmpty) {
//			List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
//			model.addAttribute("newest", newest);
//			List<EsBlog> hotest = esBlogService.listTop5HotestEsBlogs();
//			model.addAttribute("hotest", hotest);
//			List<TagVO> tags = esBlogService.listTop30Tags();
//			model.addAttribute("tags", tags);
//			List<User> users = esBlogService.listTop12Users();
//			model.addAttribute("users", users);
//		}
		
		return (async==true?"default/index :: #mainContainerRepleace":"default/index");
	}
}
