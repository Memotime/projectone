package com.david.emdblog.service;

import java.util.List;
import java.util.Map;

import com.david.emdblog.entity.Blog;

/**
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public interface BlogService {

	public List<Blog> list(Map<String, Object> map);
	
	public List<Blog> listIndex(Map<String, Object> map);

	public long getTotal(Map<String, Object> map);

	public List<Blog> countList();

	public int add(Blog blog);

	public int update(Blog blog);

	public int getBlogByTypeId(int typeId);

	/**
	 * 根据ID查找实例
	 */
	public Blog findById(Integer id);

	/**
	 * 根据ID删除实例.
	 */
	public void deleteById(Integer id);

	/**
	 * 获取上一篇文章
	 */
	public Blog getLastBlog(Integer id);

	/**
	 * 获取下一篇文章
	 */
	public Blog getNextBlog(Integer id);

}
