package com.david.emdblog.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章实体类 文章实体类
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public class Blog implements Serializable {

	private Integer id;// 编号
	private String title;// 文章标题
	private Date releaseDate;// 发布日期
	private Integer clickHit;// 查看次数
	private Integer replyHit;// 回复次数
	private String content;// 文章内容
	private String contentNoTag;// 文章内容，无网页标签 Lucene分词用
	private BlogType blogType;// 文章类型
	private String summary;// 摘要

	private Integer blogCount;// 文章数量 非文章实际属性，主要是根据发布日期归档查询文章数量用
	private String releaseDateStr;// 发布日期字符串 只去年和月
	private String keyWord;// 关键字，空格 隔开
	// 文章格式，比如是ue或者md
	private String blogFormat;
	
	

	public String getBlogFormat() {
		return blogFormat;
	}

	public void setBlogFormat(String blogFormat) {
		this.blogFormat = blogFormat;
	}

	private List<String> imagesList = new ArrayList<String>();// 文章里存在的图片
																// 主要用于列表展示显示缩略图

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Integer getClickHit() {
		return clickHit;
	}

	public void setClickHit(Integer clickHit) {
		this.clickHit = clickHit;
	}

	public Integer getReplyHit() {
		return replyHit;
	}

	public void setReplyHit(Integer replyHit) {
		this.replyHit = replyHit;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentNoTag() {
		return contentNoTag;
	}

	public void setContentNoTag(String contentNoTag) {
		this.contentNoTag = contentNoTag;
	}

	public BlogType getBlogType() {
		return blogType;
	}

	public void setBlogType(BlogType blogType) {
		this.blogType = blogType;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}

	public String getReleaseDateStr() {
		return releaseDateStr;
	}

	public void setReleaseDateStr(String releaseDateStr) {
		this.releaseDateStr = releaseDateStr;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public List<String> getImagesList() {
		return imagesList;
	}

	public void setImagesList(List<String> imagesList) {
		this.imagesList = imagesList;
	}

	public Blog(Integer id, String title, String content, String summary) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.summary = summary;
	}

	public Blog() {
		super();
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", releaseDate=" + releaseDate + ", clickHit=" + clickHit
				+ ", content=" + content + ", contentNoTag=" + contentNoTag + ", summary=" + summary + "]";
	}

}
