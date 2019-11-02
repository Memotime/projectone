package com.david.emdblog.entity;

import java.io.Serializable;

/**
 * 分页Model类 * @Author ：程序员小冰
 * 
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */

public class PageBean implements Serializable {
	private int page; // 第几页
	private int pageSize; // 每页记录数
	private int start; // 起始页

	public PageBean(int page, int pageSize) {
		super();
		this.page = page;
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStart() {
		return (page - 1) * pageSize;
	}

}
