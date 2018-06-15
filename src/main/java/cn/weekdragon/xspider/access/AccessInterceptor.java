package cn.weekdragon.xspider.access;

import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.weekdragon.xspider.domain.User;
import cn.weekdragon.xspider.redis.AccessKey;
import cn.weekdragon.xspider.redis.RedisService;
import cn.weekdragon.xspider.result.CodeMsg;
import cn.weekdragon.xspider.result.Result;
import cn.weekdragon.xspider.service.UserService;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	UserService userService;

	@Autowired
	RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			User user = userService.getUser(request, response);
			UserContext.setUser(user);
			HandlerMethod hm = (HandlerMethod) handler;
			AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
			if (accessLimit == null) {
				return true;
			}
			int seconds = accessLimit.seconds();
			int maxCount = accessLimit.maxCount();
			boolean needLogin = accessLimit.needLogin();
			String key = request.getRequestURI();
			if (needLogin) {
				if (user == null) {
					render(response, CodeMsg.SESSION_ERROR);
					return false;
				}
				key += "_" + user.getId();
			} else {
				// do nothing
			}
			AccessKey ak = AccessKey.withExpire(seconds);
			Integer count = redisService.get(ak, key, Integer.class);
			if (count == null) {
				redisService.set(ak, key, 1);
			} else if (count < maxCount) {
				redisService.incr(ak, key);
			} else {
				render(response, CodeMsg.ACCESS_LIMIT_REACHED);
				return false;
			}
		}
		return true;
	}

	private void render(HttpServletResponse response, CodeMsg cm) throws Exception {
		response.setContentType("application/json;charset=UTF-8");
		OutputStream out = response.getOutputStream();
		String str = JSON.toJSONString(Result.error(cm));
		out.write(str.getBytes("UTF-8"));
		out.flush();
		out.close();
	}
}
