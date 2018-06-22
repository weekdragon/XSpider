package cn.weekdragon.xspider.redis;


public class FilmKey extends BasePrefix {

	// 一天过期
	private FilmKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static FilmKey getById = new FilmKey(0, "id");

}
