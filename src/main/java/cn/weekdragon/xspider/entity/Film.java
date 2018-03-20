package cn.weekdragon.xspider.entity;

import java.util.List;

public class Film {

	private int id;
	
	private int webSiteFlag;
	
	private String fullTitle;
	private String shortTitle;
	private String showTime;
	private String category;
	private String rank;
	
	private String comment;
	private String imgUrl;
	private String detailUrl;
	private int commentType;
	private String prize;
	private boolean isRecomended;
	private List<String> sourceUrl;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWebSiteFlag() {
		return webSiteFlag;
	}
	public void setWebSiteFlag(int webSiteFlag) {
		this.webSiteFlag = webSiteFlag;
	}
	public String getFullTitle() {
		return fullTitle;
	}
	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}
	public String getShortTitle() {
		return shortTitle;
	}
	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}
	public String getShowTime() {
		return showTime;
	}
	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public int getCommentType() {
		return commentType;
	}
	public void setCommentType(int commentType) {
		this.commentType = commentType;
	}
	public String getPrize() {
		return prize;
	}
	public void setPrize(String prize) {
		this.prize = prize;
	}
	public boolean isRecomended() {
		return isRecomended;
	}
	public void setRecomended(boolean isRecomended) {
		this.isRecomended = isRecomended;
	}
	public List<String> getSourceUrl() {
		return sourceUrl;
	}
	public void setSourceUrl(List<String> sourceUrl) {
		this.sourceUrl = sourceUrl;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	@Override
	public String toString() {
		return "Film [id=" + id + ", webSiteFlag=" + webSiteFlag + ", fullTile=" + fullTitle + ", shortTitle="
				+ shortTitle + ", showTime=" + showTime + ", category=" + category + ", comment=" + comment
				+ ", detailUrl=" + detailUrl + ", commentType=" + commentType + ", prize=" + prize + ", isRecomended="
				+ isRecomended + ", sourceUrl=" + sourceUrl + "]";
	}
	
	
	
	
	
	
	
	
}
