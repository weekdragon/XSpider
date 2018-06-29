package cn.weekdragon.xspider.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.alibaba.fastjson.annotation.JSONField;

@Entity
public class DownloadLinks {

	@Id  // 主键
    @GeneratedValue(generator="uuid") // 自增长策略
	@GenericGenerator(name="uuid",strategy="uuid")		
	private String id;
	@ManyToOne
	@JoinColumn(name="film_id")
	@JSONField(serialize=false,deserialize=false)  
	private Film film;
	private String type;
	private String Description;
	private String link;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Film getFilm() {
		return film;
	}
	public void setFilm(Film film) {
		this.film = film;
	}
	@Override
	public String toString() {
		return "DownloadLinks [id=" + id + ", film=" + film + ", type=" + type + ", Description=" + Description + ", link=" + link + "]";
	}
	
}
