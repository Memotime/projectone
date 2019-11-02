package com.david.emdblog.dao;

import java.util.List;
import java.util.Map;

import com.david.emdblog.entity.Blog;

/**
 * 文章的持久层
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public interface BlogDao {
	/**
	 * 分页查询文章
	 */
	public List<Blog> list(Map<String, Object> map);

	/**
	 * 获取文章总数量
	 */
	public long getTotal(Map<String, Object> map);

	/**
	 * 统计每个月写的文章数量
	 */
	public List<Blog> countList();

	/**
	 * 新增文章文章
	 */
	public int add(Blog blog);

	/**
	 * 则说明是更新文章 更新文章
	 */
	public int update(Blog blog);

	/**
	 * 根据ID查找实例
	 */
	public Blog findById(Integer id);

	/**
	 * 根据文章类型id,查找此目录下的文章
	 */
	public int getBlogByTypeId(int typeId);

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
