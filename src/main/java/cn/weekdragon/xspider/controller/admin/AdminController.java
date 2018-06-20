package cn.weekdragon.xspider.controller.admin;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.weekdragon.xspider.config.XspiderConfig;
import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.vo.ServerInfo;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private XspiderConfig xspiderConfig;
	
	@GetMapping("/index")
	public String index(User user,Model model) {
		if(user == null) {
			return "admin/login";
		}
		model.addAttribute("username", user.getNickName());
		model.addAttribute("infos", wrapServerInfo());
		System.out.println(user);
		return "admin/index";
	}

	public List<ServerInfo> wrapServerInfo() {
		List<ServerInfo> infos = new ArrayList<>();
		infos.add(new ServerInfo("运行环境",xspiderConfig.getEnv()));
		infos.add(new ServerInfo("JavaEE 容器信息",xspiderConfig.getCinfo()));
		infos.add(new ServerInfo("运行路径",xspiderConfig.getRunPath()));
		infos.add(new ServerInfo("操作系统",xspiderConfig.getOSName()));
		infos.add(new ServerInfo("系统时区 - 地域/语言",xspiderConfig.getLocale()));
		infos.add(new ServerInfo("数据库版本",xspiderConfig.getDbVersion()));
		infos.add(new ServerInfo("系统编码",xspiderConfig.getSysCharset()));
		infos.add(new ServerInfo("程序版本",XspiderConfig.versionName));
		return infos;
	}
}
