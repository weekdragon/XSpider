package cn.weekdragon.xspider.controller.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.weekdragon.xspider.access.AccessLimit;
import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.exception.GlobalException;
import cn.weekdragon.xspider.redis.FilmKey;
import cn.weekdragon.xspider.redis.RedisService;
import cn.weekdragon.xspider.result.CodeMsg;
import cn.weekdragon.xspider.service.FilmService;
import cn.weekdragon.xspider.vo.PageVo;

@Controller
@RequestMapping("/admin")
public class AdminFilmController {

	@Autowired
	private RedisService redisService;
	@Autowired
	private FilmService filmService;

	@AccessLimit(needLogin = true, maxCount = 5, seconds = 5)
	@RequestMapping("/film")
	public String listFilms(@RequestParam(value = "order", required = false, defaultValue = "new") String order,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize, HttpServletRequest request, HttpServletResponse response,
			Model model, User user) {
		Page<Film> page = null;
		List<Film> list = null;
		boolean isEmpty = true; // 系统初始化时，没有电影数据
		try {
			if (order.equals("new")) { // 最新查询
				Sort sort = new Sort(Direction.DESC, "createTime");
				Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
				page = filmService.listNewestFilms(keyword, pageable);
			}

			isEmpty = false;
		} catch (Exception e) {
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = filmService.listFilms(pageable);
		}

		list = page.getContent(); // 当前所在页面数据列表

		model.addAttribute("test", "test");
		model.addAttribute("order", order);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("films", list);

		// 首次访问页面才加载
		if (!async && !isEmpty) {
			model.addAttribute("username", user.getNickName());
		}
		return async == true ? "admin/fragment/film::#content" : "admin/fragment/film";
	}

	@ResponseBody
	@RequestMapping("/films")
	public String listJsonFilms(@RequestParam(name = "draw") Integer draw, @RequestParam(name = "start") Integer start,
			@RequestParam(name = "length") Integer length) {
		Pageable pageable = new PageRequest(start/length, length);
		Page<Film> listFilms = filmService.listFilms(pageable);
		Long totalElements = listFilms.getTotalElements();
		PageVo pageVo = new PageVo();
		pageVo.setDraw(draw);
		pageVo.setRecordsFiltered(totalElements.intValue());
		pageVo.setRecordsTotal(totalElements.intValue());
		pageVo.setData(listFilms.getContent());
		return RedisService.beanToString(pageVo);
	}

	@RequestMapping("/film/add")
	public String toFilmAdd(Model model) {
		Film film = new Film();
		model.addAttribute("film", film);
		return "admin/fragment/film-edit :: #content";
	}

	@RequestMapping("/film/{id}/to_edit")
	public String toFilmEdit(@PathVariable("id") Long id, Model model) {
		if (id == null) {
			throw new GlobalException(CodeMsg.REQUEST_ILLEGAL);
		}
		Film film = redisService.get(FilmKey.getById, id + "", Film.class);
		if (film == null) {
			film = filmService.getFilmById(id);
			if (film != null) {
				redisService.set(FilmKey.getById, id + "", film);
			}
		}
		model.addAttribute("film", film);
		return "admin/fragment/film-edit :: #content";
	}

	@ResponseBody
	@RequestMapping("/film/edit")
	public CodeMsg filmEdit(@RequestBody String json) {
		System.out.println(json);
		Film film = RedisService.stringToBean(json, Film.class);
		filmService.saveFilm(film);
		redisService.delete(FilmKey.getById, film.getId() + "");
		return CodeMsg.SUCCESS;
	}

	@ResponseBody
	@AccessLimit(needLogin = true, maxCount = 5, seconds = 5)
	@RequestMapping("/film/{id}/to_delete")
	public CodeMsg toFilmDelete(@PathVariable(name = "id", required = true) String ids, Model model) {
		String[] split = ids.split("_");
		for (String id : split) {
			try {
				filmService.deleteFilm(Long.parseLong(id));
			} catch (Exception e) {
				throw new GlobalException(CodeMsg.REQUEST_ILLEGAL);
			}
		}
		return CodeMsg.SUCCESS;
	}
}
