package com.david.emdblog.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.david.emdblog.dao.LinkDao;
import com.david.emdblog.entity.Link;
import com.david.emdblog.service.LinkService;

/**
 * 友情链接管理
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Service(value = "linkService")
public class LinkServiceImpl implements LinkService {

	@Resource
	private LinkDao linkDao;

	/**
	 * 获取到友情链接的列表
	 */
	public List<Link> list(Map<String, Object> map) {
		return linkDao.list(map);
	};

	/**
	 * 获取友情链接的数量
	 */
	public Long getTotal(Map<String, Object> map) {
		return linkDao.getTotal(map);
	}

	/**
	 * 添加友情链接
	 */
	@Override
	public int add(Link link) {
		return linkDao.add(link);
	}

	/**
	 * 修改更新友情链接的信息
	 */
	@Override
	public int update(Link link) {
		return linkDao.update(link);
	}
	/**
	 * 删除友情链接
	 */
	@Override
	public void delete(Integer id) {
		linkDao.delete(id);
	};
}
