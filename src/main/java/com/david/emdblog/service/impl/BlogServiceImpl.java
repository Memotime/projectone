package com.david.emdblog.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.json.JSONUtils;
import com.david.emdblog.constant.RedisConstants;
import com.david.emdblog.dao.BlogDao;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.jedis.JedisClient;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.utils.JsonUtils;
import com.david.emdblog.utils.LogUtils;

/**
 * 文章，日志的业务层
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */

@Service(value = "blogService")
public class BlogServiceImpl implements BlogService {

	@Resource
	private BlogDao blogDao;
	
	@Autowired
	private JedisClient jedisClient;

	/**
	 * 查看文章内容列表。首先查询redis缓存中是否存在，如果存在，则直接读取，否则查询数据库.
	 * 注意：添加内容时，要更新redis数据，不然前台将看不到。
	 */
	@Override
	public List<Blog> list(Map<String, Object> map) {
		Integer page = (Integer) map.get("start");
		String typeId = (String) map.get("typeId");
		String releaseDateStr = (String) map.get("releaseDateStr");
//		if (StringUtils.isEmpty(typeId)) {
//			LogUtils.e("weikognletypeid");
//		}
//		if (StringUtils.isEmpty(releaseDateStr)) {
//			LogUtils.e("weikognlereleaseDateStr");
//		}
		// 先查询redis缓存
		if ((page==1||page==0)&&StringUtils.isBlank(typeId)&&StringUtils.isBlank(releaseDateStr)) {
			try {
				String json = jedisClient.hget(RedisConstants.CONTENT_INDEX,RedisConstants.CONTENT_INDEX);
				if (StringUtils.isNotBlank(json)) {
					System.out.println("查询到了redis缓存");
					// 不等于空，直接返回数据
					List<Blog> list = JsonUtils.jsonToList(json, Blog.class);
					return list;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// redis中没有存在，则查询数据库。
		List<Blog> list = blogDao.list(map);
//		System.out.println("查询到了数据库");
		if ((page==1||page==0)&&StringUtils.isBlank(typeId)&&StringUtils.isBlank(releaseDateStr)) {
			// 将查询结果存放到redis中.如果添加失败，也要返回，所以不能影响正常逻辑
			try {
				jedisClient.hset(RedisConstants.CONTENT_INDEX,RedisConstants.CONTENT_INDEX,JsonUtils.objectToJson(list));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	/**
	 * 这个不要参数，查询所有文章，方便构建索引库
	 */
	@Override
	public List<Blog> listIndex(Map<String, Object> map) {
		return blogDao.list(map);
	}
	/**
	 * 查询文章的总数量
	 */
	@Override
	public long getTotal(Map<String, Object> map) {
		return blogDao.getTotal(map);
	}

	/**
	 * 根据日期分组查询文章,所有文章以及对应的数量
	 */
	@Override
	public List<Blog> countList() {
		return blogDao.countList();
	}

	/**
	 * 新增文章。因为更新后需要更新redis
	 */
	@Override
	public int add(Blog blog) {
		jedisClient.hdel(RedisConstants.CONTENT_INDEX, RedisConstants.CONTENT_INDEX);
		return blogDao.add(blog);
	}

	/**
	 * 说明是更新文章 
	 */
	@Override
	public int update(Blog blog) {
		jedisClient.hdel(RedisConstants.CONTENT_INDEX, RedisConstants.CONTENT_INDEX);
		return blogDao.update(blog);
	}

	/**
	 * 根据文章类型id,查找此目录下的文章
	 */
	@Override
	public int getBlogByTypeId(int typeId) {
		return blogDao.getBlogByTypeId(typeId);
	}

	/**
	 * 根据ID查找实例
	 */
	@Override
	public Blog findById(Integer id) {
		return blogDao.findById(id);
	}

	/**
	 * 根据ID删除实例.
	 */
	@Override
	public void deleteById(Integer id) {
		jedisClient.hdel(RedisConstants.CONTENT_INDEX, RedisConstants.CONTENT_INDEX);
		blogDao.deleteById(id);
	}

	/**
	 * 获取上一篇文章
	 */
	@Override
	public Blog getLastBlog(Integer id) {
		return blogDao.getLastBlog(id);
	}

	/**
	 * 获取下一篇文章
	 */
	@Override
	public Blog getNextBlog(Integer id) {
		return blogDao.getNextBlog(id);
	}

}
