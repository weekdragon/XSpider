package cn.weekdragon.xspider.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.exception.GlobalException;
import cn.weekdragon.xspider.redis.RedisService;
import cn.weekdragon.xspider.redis.UserKey;
import cn.weekdragon.xspider.repository.UserRepository;
import cn.weekdragon.xspider.result.CodeMsg;
import cn.weekdragon.xspider.util.MD5Util;
import cn.weekdragon.xspider.util.UUIDUtil;
import cn.weekdragon.xspider.vo.LoginVo;

@Service
public class UserService {

	public static final String COOKI_NAME_TOKEN = "token";
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	RedisService redisService;
	
	public String login(HttpServletResponse response, LoginVo loginVo) {
		if(loginVo == null) {
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		}
		String userName = loginVo.getUserName();
		String formPass = loginVo.getPassword();
		//判断手机号是否存在
		User user = userRepository.findUserByUserName(userName);
		if(user == null) {
			throw new GlobalException(CodeMsg.USERNAME_NOT_EXIST);
		}
		//验证密码
		String dbPass = user.getPassword();
		String saltDB = user.getSalt();
		String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
		if(!calcPass.equals(dbPass)) {
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		}
		//生成cookie
		String token	 = UUIDUtil.uuid();
		addCookie(response, token, user);
		return token;
	}
	
	private void addCookie(HttpServletResponse response, String token, User user) {
		redisService.set(UserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(UserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	
	
}
