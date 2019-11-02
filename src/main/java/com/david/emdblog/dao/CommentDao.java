package com.david.emdblog.dao;

import java.util.List;
import java.util.Map;

import com.david.emdblog.entity.Comment;

/**
 * 评论管理的接口
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
public interface CommentDao {
	/**
	 * 分页查询所有的评论
	 */
	public List<Comment> list(Map<String, Object> map);

	/**
	 * 获取总记录数
	 */
	public Long getTotal(Map<String, Object> map);

	/**
	 * 根据ID删除评论
	 */
	public void delete(Integer id);

	/**
	 * 更新评论信息
	 */
	public void update(Comment comment);
	/**
	 * 添加评论
	 */
	public int add(Comment comment);

}
