package cn.weekdragon.xspider.controller.admin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;

import cn.weekdragon.xspider.config.XspiderConfig;
import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.result.CodeMsg;
import cn.weekdragon.xspider.result.Result;
import cn.weekdragon.xspider.vo.ServerInfo;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private XspiderConfig xspiderConfig;
	@Autowired
	private SpringResourceTemplateResolver thymeleafViewResolver;

	@GetMapping("/index")
	public String index(User user, Model model) {
		if (user == null) {
			return "admin/login";
		}
		model.addAttribute("username", user.getNickName());
		model.addAttribute("infos", wrapServerInfo());
		System.out.println(user);
		return "admin/index";
	}

	@ResponseBody
	@GetMapping("/console")
	public Result<String> console(@RequestParam(value = "async", required = false) boolean async, User user, Model model,HttpServletRequest request,HttpServletResponse response) {
		if (user == null) {
			return Result.error(CodeMsg.SESSION_ERROR);
		}
		// 首次访问页面才加载
		if (!async) {
			model.addAttribute("username", user.getNickName());
		}
		model.addAttribute("infos", wrapServerInfo());
		// 手动渲染
		WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale(), model.asMap());
		thymeleafViewResolver.setSuffix(".html");
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(thymeleafViewResolver);
		Set<String> selectors = new HashSet<>();
		if (async == true) {
			selectors.add("#content");
		}

		String html = engine.process("admin/fragment/console", selectors, ctx);
		return Result.success(html);
	}

	public List<ServerInfo> wrapServerInfo() {
		List<ServerInfo> infos = new ArrayList<>();
		infos.add(new ServerInfo("运行环境", xspiderConfig.getEnv()));
		infos.add(new ServerInfo("JavaEE 容器信息", xspiderConfig.getCinfo()));
		infos.add(new ServerInfo("运行路径", xspiderConfig.getRunPath()));
		infos.add(new ServerInfo("操作系统", xspiderConfig.getOSName()));
		infos.add(new ServerInfo("系统时区 - 地域/语言", xspiderConfig.getLocale()));
		infos.add(new ServerInfo("数据库版本", xspiderConfig.getDbVersion()));
		infos.add(new ServerInfo("系统编码", xspiderConfig.getSysCharset()));
		infos.add(new ServerInfo("程序版本", XspiderConfig.versionName));
		return infos;
	}
}
