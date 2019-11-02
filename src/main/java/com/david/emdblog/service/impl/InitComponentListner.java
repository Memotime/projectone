package com.david.emdblog.service.impl;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.david.emdblog.entity.Blog;
import com.david.emdblog.entity.BlogType;
import com.david.emdblog.entity.Blogger;
import com.david.emdblog.entity.Link;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.BlogTypeService;
import com.david.emdblog.service.BloggerService;
import com.david.emdblog.service.LinkService;

/**
 * 初始化组件 把博主信息 根据文章类别分类信息 根据日期归档分类信息 存放到application中，用以提供页面请求性能
 *
 */
@Component
public class InitComponentListner implements ServletContextListener, ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		System.out.println("初始化组件执行了。。。。");
		ServletContext application = servletContextEvent.getServletContext();
		BloggerService bloggerService = (BloggerService) applicationContext.getBean("bloggerService");
		// 查询博主的信息
		Blogger blogger = bloggerService.findBloggerInfo();
		blogger.setPassword(null);
		application.setAttribute("blogger", blogger);

		// 查询文章类别以及文章数量
		BlogTypeService blogTypeService = (BlogTypeService) applicationContext.getBean("blogTypeService");
		List<BlogType> blogTypeCountList = blogTypeService.countList();
		application.setAttribute("blogTypeCountList", blogTypeCountList);
		// 根据日期分组查询文章
		BlogService blogService = (BlogService) applicationContext.getBean("blogService");
		List<Blog> blogCountList = blogService.countList();
		application.setAttribute("blogCountList", blogCountList);
		// 查询所有的友情链接信息
		LinkService linkService = (LinkService) applicationContext.getBean("linkService");
		List<Link> linkList = linkService.list(null);
		application.setAttribute("linkList", linkList);

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
	}

}
