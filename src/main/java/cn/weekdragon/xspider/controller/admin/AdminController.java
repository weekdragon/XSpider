package cn.weekdragon.xspider.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.weekdragon.xspider.domain.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

	
	@GetMapping("/index")
	public String index(User user,Model model) {
		if(user == null) {
			return "admin/login";
		}
		model.addAttribute("username", user.getNickName());
		System.out.println(user);
		return "admin/index";
	}

}
