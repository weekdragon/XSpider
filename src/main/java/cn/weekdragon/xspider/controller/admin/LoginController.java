package cn.weekdragon.xspider.controller.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.result.Result;
import cn.weekdragon.xspider.service.UserService;
import cn.weekdragon.xspider.vo.LoginVo;

@Controller
@RequestMapping("/admin")
public class LoginController {

	@Autowired
	UserService userService;
	
	@GetMapping({"","/","/login"})
	public String toLogin(HttpServletRequest request,HttpServletResponse response) {
		User user = userService.getUser(request, response);
		if(user!=null) {
			return "redirect:/admin/index";
		}
		
		return "admin/login";
	}
	
	@ResponseBody
	@PostMapping("/login")
	public Result<String> doLogin(HttpServletResponse response,LoginVo loginVo) {
		String token = userService.login(response, loginVo);
		return Result.success(token);
	}
	
	@GetMapping("/logout")
	public String Logout(HttpServletRequest request) {
		userService.logout(request);
		
		return "redirect:/admin/login";
	}
}
