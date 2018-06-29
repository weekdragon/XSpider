package cn.weekdragon.xspider.controller;

import java.util.Arrays;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.weekdragon.xspider.access.AccessLimit;
import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.exception.GlobalException;
import cn.weekdragon.xspider.result.CodeMsg;
import cn.weekdragon.xspider.service.FilmService;
import cn.weekdragon.xspider.util.Constants;

@Controller
@RequestMapping("/film")
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
		try {
			if (order.equals("new")) { // 最新查询
				Sort sort = new Sort(Direction.DESC,"createTime"); 
				Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
				page = filmService.listNewestFilms(keyword, pageable);
			}
		} catch (Exception e) {
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = filmService.listFilms(pageable);
		}  
 
		list = page.getContent();	// 当前所在页面数据列表
		model.addAttribute("order", order);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("films", list);
		return (async==true?Constants.FET + "/index :: #mainContainerRepleace" : Constants.FET + "/index");
	}
	
	@AccessLimit(needLogin = false,maxCount = 5,seconds = 5)
	@GetMapping("/{id}")
	public String detail(@PathVariable(name = "id",required = true) Long id,Model model) {
		Film film = filmService.getFilmById(id);
		if(film == null) {
			throw new GlobalException(CodeMsg.REQUEST_ILLEGAL);
		}
		film.setViews(film.getViews()+1);
		filmService.saveFilm(film);
		model.addAttribute("film", film);
		return Constants.FET + "/detail";
	}
}
