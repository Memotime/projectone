package com.david.emdblog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.david.emdblog.dao.CommentDao;
import com.david.emdblog.entity.Comment;
import com.david.emdblog.service.CommentService;

/**
 * 评论管理的业务
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Service(value = "commentService")
public class CommentServiceImpl implements CommentService {

	@Resource
	private CommentDao commentDao;

	/**
	 * 分页查询所有的评论
	 */
	@Override
	public List<Comment> list(Map<String, Object> map) {
		return commentDao.list(map);
	}

	/**
	 * 获取总记录数
	 */
	@Override
	public Long getTotal(Map<String, Object> map) {
		return commentDao.getTotal(map);
	}

	/**
	 * 根据ID删除评论
	 */
	@Override
	public void delete(Integer id) {
		commentDao.delete(id);
	}

	/**
	 * 更新评论信息
	 */
	@Override
	public void update(Comment comment) {
		commentDao.update(comment);
	}
	/**
	 * 添加评论
	 */
	@Override
	public int add(Comment comment) {
		return commentDao.add(comment);
	}

}
