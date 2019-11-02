package com.david.emdblog.dao;

import com.david.emdblog.entity.Blogger;

/**
 * 博主，站长dao。前端显示
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public interface BloggerDao {
	/**
	 * 查询博主信息
	 */
	public Blogger find();

	/**
	 * 根据用户名查找用户
	 */
	public Blogger findBloggerByUsername(String userName);

	/**
	 * 更新博主的信息
	 */
	public int update(Blogger blogger);
}
