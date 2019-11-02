package com.david.emdblog.entity;

import java.io.Serializable;

/**
 * 文章类型  文章类型，日志类型Javabean
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public class BlogType implements Serializable {
	private Integer id;// 编号
	private String typeName;// 文章类型名称
	private Integer blogCount;// 数量
	private Integer orderNo;// 排序，从小到大排序显示

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getBlogCount() {
		return blogCount;
	}

	public void setBlogCount(Integer blogCount) {
		this.blogCount = blogCount;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}
