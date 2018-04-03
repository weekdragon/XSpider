package cn.weekdragon.xspider.domain;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Film {
	@Id  // 主键
    @GeneratedValue(strategy=GenerationType.IDENTITY) // 自增长策略
	private Long id;
	private int webSiteFlag;
	@Column(nullable = false) //全名
	private String fullTitle;
	@Column
	private String shortTitle;
	@Column
	private String showTime;
	@ElementCollection
	private List<String> category;
	@Column
	private String imgUrl;
	@Column
	private String rank;
	@Column
	private String comment;
	@Column
	private String detailUrl;
	@Column(length=1024)
	private String briefCnt;
	@Column
	private int commentType;
	@Column
	private String prize;
	@Column
	private boolean isRecomended;
	@ElementCollection
	private List<String> sourceUrl;
	
	@Column(nullable = false) // 映射为字段，值不能为空
	@org.hibernate.annotations.CreationTimestamp  // 由数据库自动创建时间
	private Timestamp createTime;
	
	
	public List<String> getCategory() {
		return category;
	}
	public void setCategory(List<String> category) {
		this.category = category;
	}
	public String getBriefCnt() {
		return briefCnt;
	}
	public void setBriefCnt(String briefCnt) {
		this.briefCnt = briefCnt;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getFullTitle() {
		return fullTitle;
	}
	public void setFullTitle(String fullTitle) {
		this.fullTitle = fullTitle;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getWebSiteFlag() {
		return webSiteFlag;
	}
	public void setWebSiteFlag(int webSiteFlag) {
		this.webSiteFlag = webSiteFlag;
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
	
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "Film [id=" + id + ", webSiteFlag=" + webSiteFlag + ", fullTile=" + fullTitle + ", shortTitle="
				+ shortTitle + ", showTime=" + showTime + ", category=" + category + ", comment=" + comment
				+ ", detailUrl=" + detailUrl + ", commentType=" + commentType + ", prize=" + prize + ", isRecomended="
				+ isRecomended + ", sourceUrl=" + sourceUrl + "]";
	}
}
