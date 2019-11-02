package com.david.emdblog.web.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.lucene.BlogIndex;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.CommentService;
import com.david.emdblog.utils.MarkdownToHtmlUtil;
import com.david.emdblog.utils.UtilFuns;

/**
 * 文章的controller。主要是前端
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Controller
@RequestMapping("/blog")
public class BlogController {

	@Resource(name = "blogService")
	private BlogService blogService;

	@Resource(name = "commentService")
	private CommentService commentService;
	// 文章索引
	private BlogIndex blogIndex = new BlogIndex();

	/**
	 * 请求文章的详细信息。。使用resuful
	 */
	@RequestMapping("/articles/{id}")
	public ModelAndView details(@PathVariable("id") Integer id, HttpServletRequest request,HttpServletResponse response) throws IOException {
		ModelAndView modelAndView = new ModelAndView();
		// 根据id查询文章的详情
		Blog blog = blogService.findById(id);
		if (blog==null) {
			// 如果查找不到blog详情，则说明不存在，直接返回404页面
//			response.sendError(404);
			modelAndView.setViewName("static/error/404");
			return modelAndView;
		}
		// 得到关键词
		String keyWords = blog.getKeyWord();
		if (UtilFuns.isNotEmpty(keyWords)) {
			// 以空格进行分割
			String arrs[] = keyWords.split(" ");
			// 关键词
			modelAndView.addObject("keyWords", UtilFuns.filterWhite(Arrays.asList(arrs)));
		} else {
			modelAndView.addObject("keyWords", null);
		}
		modelAndView.addObject("blog", blog);
		// 文章点击次数加1
		blog.setClickHit(blog.getClickHit() + 1);
		blogService.update(blog);
		Map<String, Object> map = new HashMap();
		map.put("blogId", blog.getId());// 根据id查询
		map.put("state", 1);// 查询审核通过的评论
		modelAndView.addObject("commentList", commentService.list(map));// 查询评论
		// 设置可以获取上一篇，下一篇
		modelAndView.addObject("pageCode", this.getUpAndDownPageCode(blogService.getLastBlog(id),
				blogService.getNextBlog(id), request.getServletContext().getContextPath()));
		modelAndView.addObject("mainPage", "foreground/blog/view.jsp");
		modelAndView.addObject("pageTitle", blog.getTitle() + Constants.BLOG_TITLE);
		modelAndView.setViewName("mainTemp");
		return modelAndView;

	}

	/**
	 * 获取上一篇和下一篇文章的代码
	 */
	private String getUpAndDownPageCode(Blog lastBlog, Blog nextBlog, String contextPath) {
		StringBuffer pageCode = new StringBuffer();
		if (lastBlog == null || lastBlog.getId() == null) {
			pageCode.append("<p>上一篇：没有了</p>");
		} else {
			pageCode.append("<p>上一篇：<a href='" + contextPath + "/blog/articles/" + lastBlog.getId() + ".html'>"
					+ lastBlog.getTitle() + "</a></p>");
		}
		if (nextBlog == null || nextBlog.getId() == null) {
			pageCode.append("<p>下一篇：没有了</p>");
		} else {
			pageCode.append("<p>下一篇：<a href='" + contextPath + "/blog/articles/" + nextBlog.getId() + ".html'>"
					+ nextBlog.getTitle() + "</a></p>");
		}
		return pageCode.toString();
	}

	/**
	 * 根据关键字查询相关文章
	 */
	@RequestMapping("/q")
	public ModelAndView search(@RequestParam(value = "q", required = false) String q,
			@RequestParam(value = "page", required = false) String page, HttpServletRequest request) throws Exception {
		if (UtilFuns.isEmpty(page)) {
			page = "1";
		}
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mainPage", "foreground/blog/result.jsp");
		List<Blog> blogList = blogIndex.searchBlog(q.trim());
		System.out.println("blogList.size"+blogList.size());
		for (int i = 0; i < blogList.size(); i++) {
			System.out.println(blogList.get(i).getSummary());
			System.out.println(blogList.get(i));
		}
		// 如果大于10，则显示10个，否则显示全部
		Integer toIndex = blogList.size() >= Integer.parseInt(page) * 10 ? Integer.parseInt(page) * 10
				: blogList.size();
		modelAndView.addObject("blogList", blogList.subList((Integer.parseInt(page) - 1) * 10, toIndex));
		modelAndView.addObject("pageCode", this.getQueryUpAndDownPageCode(Integer.parseInt(page), blogList.size(), q,
				10, request.getServletContext().getContextPath()));
		modelAndView.addObject("q", q);
		modelAndView.addObject("resultTotal", blogList.size());
		modelAndView.addObject("pageTitle", "搜索关键字'" + q + "'结果页面" + Constants.BLOG_TITLE);
		modelAndView.setViewName("mainTemp");
		return modelAndView;

	}

	/**
	 * 查询文章，获取上一页，下一页代码。
	 * 
	 * @param page
	 *            当前页
	 * @param totalNum
	 *            总记录数
	 * @param q
	 *            查询关键字
	 * @param pageSize
	 *            每页数量
	 * @param contextPath
	 * @return
	 */
	private String getQueryUpAndDownPageCode(int page, int totalNum, String q, int pageSize, String contextPath) {
		// 总页数
		long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
		StringBuffer pageCode = new StringBuffer();
		if (totalPage == 0) {
			return "";
		} else {
			pageCode.append("<nav>");
			pageCode.append("<ul class='pager' >");
			if (page > 1) {
				pageCode.append("<li><a href='" + contextPath + "/blog/q.html?page=" + (page - 1) + "&q=" + q
						+ "'>上一页</a></li>");
			} else {
				pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
			}
			if (page < totalPage) {
				pageCode.append("<li><a href='" + contextPath + "/blog/q.html?page=" + (page + 1) + "&q=" + q
						+ "'>下一页</a></li>");
			} else {
				pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
			}
			pageCode.append("</ul>");
			pageCode.append("</nav>");
		}
		return pageCode.toString();
	}

}
