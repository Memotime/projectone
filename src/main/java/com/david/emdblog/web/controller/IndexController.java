package com.david.emdblog.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.david.emdblog.constant.ApiUrl;
import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.entity.PageBean;
import com.david.emdblog.lucene.BlogIndex;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.impl.BloggerServiceImpl;
import com.david.emdblog.utils.CharsetUtils;
import com.david.emdblog.utils.PageUtils;
import com.david.emdblog.utils.UtilFuns;

/**
 * 客户端主页的Controller
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */

@RequestMapping("/")
@Controller
public class IndexController {
	@Resource(name = "blogService")
	private BlogService blogService;

	/**
	 * 请求主页
	 */
	@RequestMapping("/index")
	public ModelAndView index(@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "rows", required = false) String rows,
			@RequestParam(value = "typeId", required = false) String typeId,
			@RequestParam(value = "releaseDateStr", required = false) String releaseDateStr,
			HttpServletRequest request) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		// 分页的数据
		if (UtilFuns.isEmpty(page) || UtilFuns.isEmpty(page.trim())) {
			page = "1";
		}
		if (UtilFuns.isEmpty(rows) || UtilFuns.isEmpty(rows.trim())) {
			rows = Constants.PAGE_SIZE + "";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
//		System.out.println(
//				"pageBean.getStart()" + pageBean.getStart() + "pageBean.getPageSize()" + pageBean.getPageSize());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", pageBean.getStart());// 开始第几页，
		map.put("size", pageBean.getPageSize());// 每页展示数量
		map.put("typeId", typeId);// 日志分类ID
		if (UtilFuns.isEmpty(releaseDateStr)) {
			map.put("releaseDateStr", releaseDateStr);
		} else {
			System.out.println("releaseDateStrbuweikong:"+releaseDateStr+"date;"+CharsetUtils.getGetMethodParameter(releaseDateStr));
			map.put("releaseDateStr",CharsetUtils.getGetMethodParameter(releaseDateStr));// 按日期查询
		}

		// 获取日志列表。里面有设置redis缓存
		List<Blog> blogList = blogService.list(map);
//		System.out.println(blogList.size());
		for (Blog blog : blogList) {
			List<String> imagesList = blog.getImagesList();
			String blogInfo = blog.getContent();
			Document document = Jsoup.parse(blogInfo);
			// 博主图片默认是jpg的，这样才会显示
			Elements jpgs = document.select("img[src$=.jpg]");// 查找扩展名为jpg的图片
			for (int i = 0; i < jpgs.size(); i++) {
				Element jpg = jpgs.get(i);
				imagesList.add(jpg.toString());
				if (i == 2) {
					break;
				}
			}
		}

		modelAndView.addObject("blogList", blogList);
		for (Blog blog : blogList) {
			List<String> imagesList = blog.getImagesList();
			for (String string : imagesList) {
				System.out.println(string);
			}
		}
		/**
		 * 将我们查询的参数继续追加进去
		 */
		StringBuilder sBuilder = new StringBuilder();// 查询参数
		if (UtilFuns.isNotEmpty(typeId)) {
			sBuilder.append("typeId=" + typeId + "&");
		}
		if (UtilFuns.isNotEmpty(releaseDateStr)) {
			sBuilder.append("releaseDateStr=" + releaseDateStr + "&");
		}
		modelAndView.addObject("pageCode", PageUtils.genPagination(request.getContextPath() + "/index.html",
				blogService.getTotal(map), Integer.parseInt(page), Constants.PAGE_SIZE, sBuilder.toString()));
		modelAndView.addObject("mainPage", ApiUrl.FOREGROUND_BLOG_LIST);// 文章列表
		modelAndView.addObject("pageTitle", "首页:" + Constants.BLOG_TITLE);// title标题
		modelAndView.setViewName("mainTemp");
		return modelAndView;
	}

	/**
	 * 源码下载
	 * @throws Exception 
	 */
	@RequestMapping("/download")
	public ModelAndView download() throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("mainPage", ApiUrl.FOREGROUND_SYSTEM_DOWNLOAD);
		modelAndView.addObject("pageTitle", "源码下载:" + Constants.BLOG_TITLE);
		modelAndView.setViewName("mainTemp");
		return modelAndView;
	}
}
