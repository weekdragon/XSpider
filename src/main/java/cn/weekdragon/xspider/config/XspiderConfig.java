package cn.weekdragon.xspider.config;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

@Service
public class XspiderConfig {
	public static String versionName="V0.0.1";
	public static int versionCode=1;
	
	private String env;
	private String cinfo;
	private String runPath;
	private String OSName;
	private String locale;
	private String dbVersion;
	private String sysCharset;
	
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@PostConstruct
	public void init() {
		Properties properties = System.getProperties();
		env = properties.getProperty("java.vm.name") + " " + properties.getProperty("java.runtime.version");
		cinfo = servletContext.getServerInfo();
		runPath = ClassUtils.getDefaultClassLoader().getResource("").getPath();
		OSName = properties.getProperty("os.name") + " " + properties.getProperty("os.version");
		locale = properties.getProperty("user.timezone");
		dbVersion = getMyDBVersion();
		sysCharset = properties.getProperty("file.encoding");
	}
	private String getMyDBVersion() {
		 return jdbcTemplate.queryForObject("select version()", String.class);
	}

	public String getEnv() {
		return env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	public String getCinfo() {
		return cinfo;
	}

	public void setCinfo(String cinfo) {
		this.cinfo = cinfo;
	}

	public String getRunPath() {
		return runPath;
	}

	public void setRunPath(String runPath) {
		this.runPath = runPath;
	}

	public String getOSName() {
		return OSName;
	}

	public void setOSName(String oSName) {
		OSName = oSName;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getDbVersion() {
		return dbVersion;
	}

	public void setDbVersion(String dbVersion) {
		this.dbVersion = dbVersion;
	}

	public String getSysCharset() {
		return sysCharset;
	}

	public void setSysCharset(String sysCharset) {
		this.sysCharset = sysCharset;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
}
