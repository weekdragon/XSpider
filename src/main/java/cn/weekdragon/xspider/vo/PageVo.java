package cn.weekdragon.xspider.vo;

import java.util.List;

import cn.weekdragon.xspider.domain.Film;

public class PageVo {
	private Integer draw;
	private Integer recordsTotal;
	private Integer recordsFiltered;
	private List<Film> data;
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	public List<Film> getData() {
		return data;
	}
	public void setData(List<Film> data) {
		this.data = data;
	}
	
	
}
