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

import cn.weekdragon.xspider.domain.Film;
import cn.weekdragon.xspider.repository.FilmRepository;

@Controller
public class IndexController {

	@Autowired
	private FilmRepository filmRepository;
	
	@GetMapping("/")
	public String index(Model model) {
		Order idOrder = new Order(Direction.DESC, "id");  
        Sort sort = new Sort(idOrder);
		Pageable pageable = new PageRequest(0, 30,sort);
		Page<Film> page = filmRepository.findAll(pageable);
		
		List<Film> films = page.getContent();
		
		int index = films.size()/3;
		
		model.addAttribute("films1",films.subList(0, index));
		model.addAttribute("films2",films.subList(index+1, index*2));
		model.addAttribute("films3",films.subList(index*2, films.size()));
		return "default/index";
	}
}
