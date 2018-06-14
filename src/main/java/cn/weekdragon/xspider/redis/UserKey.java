package cn.weekdragon.xspider.redis;


public class UserKey extends BasePrefix {

	// 一天过期
	public static final int TOKEN_EXPIRE = 3600*24 * 1;
	private UserKey(int expireSeconds, String prefix) {
		super(expireSeconds, prefix);
	}
	public static UserKey token = new UserKey(TOKEN_EXPIRE, "tk");
	public static UserKey getById = new UserKey(0, "id");

}
