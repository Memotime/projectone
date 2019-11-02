package com.david.emdblog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.david.emdblog.dao.BlogTypeDao;
import com.david.emdblog.entity.BlogType;
import com.david.emdblog.service.BlogTypeService;

/**
 * 文章类型
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Service(value = "blogTypeService")
public class BlogTypeServiceImpl implements BlogTypeService {

	@Resource
	private BlogTypeDao blogTypeDao;

	/**
	 * 查询所有文章类别以及对应的数量
	 */
	@Override
	public List<BlogType> countList() {
		return blogTypeDao.countList();
	}

	/**
	 * 获取到分类数据
	 */
	@Override
	public List<BlogType> list(Map<String, Object> map) {
		return blogTypeDao.list(map);
	}

	/**
	 * 获取到分类数量
	 */
	@Override
	public Long getTotal(Map<String, Object> map) {
		return blogTypeDao.getTotal(map);
	}

	/**
	 * 添加分类
	 */
	@Override
	public int add(BlogType blogType) {
		return blogTypeDao.add(blogType);
	}

	/**
	 * 修改分类
	 */
	@Override
	public int update(BlogType blogType) {
		return blogTypeDao.update(blogType);
	}
	/**
	 * 删除分类
	 */
	@Override
	public void delete(int id) {
		blogTypeDao.delete(id);
	}

}
