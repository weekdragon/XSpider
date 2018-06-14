package cn.weekdragon.xspider.vo;

import javax.validation.constraints.NotNull;

public class LoginVo {
	
	@NotNull
	private String userName;
	@NotNull
	private String password;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginVo [userName=" + userName + ", password=" + password + "]";
	}
}
