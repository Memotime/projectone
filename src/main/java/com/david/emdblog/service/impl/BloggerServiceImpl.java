package com.david.emdblog.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.david.emdblog.constant.RedisConstants;
import com.david.emdblog.dao.BloggerDao;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.entity.Blogger;
import com.david.emdblog.jedis.JedisClient;
import com.david.emdblog.service.BloggerService;
import com.david.emdblog.utils.JsonUtils;

/**
 * 博主，作者站长信息
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Service(value = "bloggerService")
public class BloggerServiceImpl implements BloggerService {

	@Resource
	private BloggerDao bloggerDao;
	
	@Autowired
	private JedisClient jedisClient;
	/**
	 * 查看博主。站长。作者的信息 。首先查询redis缓存中是否存在，如果存在，则直接读取，否则查询数据库.
	 * 注意：添加内容时，要更新redis数据，不然前台将看不到。
	 */
	@Override
	public Blogger findBloggerInfo() {
		try {
			String json = jedisClient.hget(RedisConstants.ABOUT_ME_INDEX,RedisConstants.ABOUT_ME_INDEX);
			if (StringUtils.isNotBlank(json)) {
				System.out.println("查询到了redis缓存");
				// 不等于空，直接返回数据
				Blogger blogger = JsonUtils.jsonToPojo(json, Blogger.class);
				return blogger;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Blogger blogger = bloggerDao.find();
		// 将查询结果存放到redis中.如果添加失败，也要返回，所以不能影响正常逻辑
		try {
			jedisClient.hset(RedisConstants.ABOUT_ME_INDEX,RedisConstants.ABOUT_ME_INDEX,JsonUtils.objectToJson(blogger));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return blogger;
	}

	/**
	 * 根据用户名查找用户
	 */
	@Override
	public Blogger findBloggerByUsername(String userName) {
		return bloggerDao.findBloggerByUsername(userName);
	}

	/**
	 * 更新博主的信息
	 */
	@Override
	public int update(Blogger blogger) {
		jedisClient.hdel(RedisConstants.ABOUT_ME_INDEX, RedisConstants.ABOUT_ME_INDEX);
		return bloggerDao.update(blogger);
	}
}
