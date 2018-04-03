package cn.weekdragon.xspider.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.repository.FilmRepository;

@Controller
public class MainController {

	@Autowired
	private FilmRepository filmRepository;
	
	@GetMapping("/")
	public String index(Model model,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize) {
		Order idOrder = new Order(Direction.DESC, "id");  
        Sort sort = new Sort(idOrder);
		Pageable pageable = new PageRequest(pageIndex, pageSize,sort);
		Page<Film> page = filmRepository.findAll(pageable);
		List<Film> films = page.getContent();
		model.addAttribute("films",films);
		model.addAttribute("page", page);
		
		return "default/index";
	}
}
