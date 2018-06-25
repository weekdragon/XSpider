package cn.weekdragon.xspider.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	/**
	 * 胖鸟网 http://www.pniao.com/
	 */
	public static final int WEB_SITE_FLAG_PANIAO = 1;
	
	/**
	 * 4K电影_bt电影 http://www.btbt4k.com/
	 */
	public static final int WEB_SITE_FLAG_BTBT4K = 2;
	
	private static Map<Integer, String> flagToString = new HashMap<>();
	static {
		flagToString.put(WEB_SITE_FLAG_PANIAO, "胖鸟");
		flagToString.put(WEB_SITE_FLAG_BTBT4K, "4K 电影");
	}
	public static String getWebSiteFlagString(Integer flag){
		if(flag == null) {
			return null;
		}
		return flagToString.get(flag);
	}
}
