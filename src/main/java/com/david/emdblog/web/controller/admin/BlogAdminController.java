package com.david.emdblog.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.constant.JsonConstant;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.entity.Comment;
import com.david.emdblog.entity.PageBean;
import com.david.emdblog.lucene.BlogIndex;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.utils.FileUtil;
import com.david.emdblog.utils.HtmlUtil;
import com.david.emdblog.utils.ResponseUtils;
import com.david.emdblog.utils.UtilFuns;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 管理员界面。controller
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Controller
@RequestMapping("/admin/blog")
public class BlogAdminController {
	@Resource(name = "blogService")
	private BlogService blogService;
	// 文章索引
	private BlogIndex blogIndex = new BlogIndex();

	// http://localhost/Blog/static/ueditor/jsp/controller.jsp?action=config&&noCache=1505793074364
	// http://localhost:18080/Blog/static/ueditor/jsp/controller.jsp?action=config&&noCache=1505792914168
	/**
	 * UE添加或者修改文章信息。由于是ue管理，所以blogFormat为ue
	 */
	@RequestMapping("/save")
	public String save(Blog blog, HttpServletResponse response) throws Exception {
		int resultTotal = 0;// 操作的记录条数
		blog.setBlogFormat("ue");
		if (blog.getId() == null) {
			System.out.print(blog);
			// 则说明是新增
			resultTotal = blogService.add(blog);
			try {
				blogIndex.addIndex(blog);// 添加文章索引
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 则说明是更新文章 更新文章
			resultTotal = blogService.update(blog);
			try {
				blogIndex.updateIndex(blog);// 更新文章索引
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		JSONObject jsonObject = new JSONObject();
		if (resultTotal > 0) {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		} else {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.FALSE);
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * Markdown语法添加或者修改文章信息。由于是Markdown管理，所以blogFormat为md
	 */
	@RequestMapping("/saveMarkdownBlog")
	public String saveMarkdownBlog(Blog blog, HttpServletResponse response) throws Exception {
		int resultTotal = 0;// 操作的记录条数
		blog.setBlogFormat("md");
		// System.out.println("blog::::"+blog);
		if (blog.getId() == null) {

			try {
				// 则说明是新增
				resultTotal = blogService.add(blog);
				// 添加文章索引
				blogIndex.addIndex(blog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				// 则说明是更新文章
				resultTotal = blogService.update(blog);
				blogIndex.updateIndex(blog);// 更新文章索引
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		JSONObject jsonObject = new JSONObject();
		if (resultTotal > 0) {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		} else {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.FALSE);
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 分页查询所有的文章 文章列表展示时。搜索：s_blog
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "rows", required = false) String rows, Blog s_blog, HttpServletResponse response)
			throws Exception {
		if (UtilFuns.isEmpty(page) || UtilFuns.isEmpty(page.trim())) {
			page = "1";
		}
		if (UtilFuns.isEmpty(rows) || UtilFuns.isEmpty(rows.trim())) {
			rows = Constants.PAGE_SIZE + "";
		}
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 这个地方设置具体的javabean的类型，不然会报类型转换错误
		 */
		map.put("start", pageBean.getStart());// 起始页
		map.put("size", pageBean.getPageSize());
		map.put("title", UtilFuns.formatLike(s_blog.getTitle()));
		map.put("blogFormat", s_blog.getBlogFormat());
		List<Blog> blogList = blogService.list(map);// 获取文章的列表
		Long total = blogService.getTotal(map);// 获取文章的总数量
		JSONObject jsonObject = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		// 因为有日期，所以转换一下
		JSONArray jsonArray = JSONArray.fromObject(blogList, jsonConfig);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 根据ID查找实例
	 */
	@RequestMapping("/findById")
	public String findById(@RequestParam(value = "id", required = true) String id, HttpServletResponse response)
			throws Exception {
		Blog blog = blogService.findById(Integer.parseInt(id));
		JSONObject jsonObject = JSONObject.fromObject(blog);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 根据ID删除实例.因为可能批量删除，所以用数组
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids", required = true) String ids, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			// 根据ID取出文章详情。
			Blog blogInfo = blogService.findById(Integer.parseInt(idsStr[i]));
			// 得到图片在服务器的地址，然后循环删除
			List<String> imgList = HtmlUtil.getImagesByHTML(blogInfo.getContent());
			// 删除 对应的图片。减少服务器空间..必须要先删除图片，再去删除文章
			// E:\Download_workspace\emd_blog\emd_blog\src\main\webapp\
			String filePath = request.getServletContext().getRealPath("/");
			if (imgList != null && imgList.size() > 0) {
				for (int j = 0; j < imgList.size(); j++) {
					System.out.println(filePath);
					System.out.println(imgList.size());
					// 进行转换斜杠，并截取。具体看htmlutil工具类
					String imagePath = imgList.get(j).replace("/", "\\");
					imagePath = imagePath.substring(10, imagePath.length());
					System.out.println(filePath + imagePath);
					// 如果存在文件才会去删除。否则不执行删除命令
					if (FileUtil.isFileExist((filePath + imagePath))) {
						FileUtil.deleteFile(filePath + imagePath);
					}
				}
			}
			blogService.deleteById(Integer.parseInt(idsStr[i]));
			try {
				// 删除对应的文章的索引
				blogIndex.deleteIndex(idsStr[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONObject jsonObject = new JSONObject();
		// 操作成功
		jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

}
