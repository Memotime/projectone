package com.david.emdblog.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.constant.JsonConstant;
import com.david.emdblog.entity.BlogType;
import com.david.emdblog.entity.PageBean;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.BlogTypeService;
import com.david.emdblog.utils.ResponseUtils;
import com.david.emdblog.utils.UtilFuns;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 管理员，文章分类，类别的管理
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@RequestMapping("/admin/blogType")
@Controller
public class BlogTypeAdminController {
	@Resource(name = "blogTypeService")
	private BlogTypeService blogTypeService;

	@Resource(name = "blogService")
	private BlogService blogService;

	/**
	 * 分页查询文章类别信息
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "rows", required = false) String rows, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
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
		// 获取到分类数据
		List<BlogType> blogTypeList = blogTypeService.list(map);
		Long total = blogTypeService.getTotal(map);// 获取到数量
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(blogTypeList);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 添加或者修改分类
	 */
	@RequestMapping("/save")
	public String save(BlogType blogType, HttpServletResponse response) throws Exception {
		int totalResult = 0;
		if (blogType.getId() == null) {
			// 则证明是新增，添加
			totalResult = blogTypeService.add(blogType);
		} else {
			// 如果有ID，则说明是修改
			totalResult = blogTypeService.update(blogType);
		}
		JSONObject jsonObject = new JSONObject();
		if (totalResult > 0) {
			// 则说明操作成功，
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		} else {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.FALSE);
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}
	/**
	 * 删除文章分类
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value="ids")String ids,
			HttpServletResponse response) throws Exception {
		String idsStr[] = ids.split(",");
		JSONObject jsonObject = new JSONObject();
		for (int i = 0; i < idsStr.length; i++) {
			if (blogService.getBlogByTypeId(Integer.parseInt(idsStr[i]))>0) {
				jsonObject.put("exist", "该分类下还有文章，不能删除！");
			}else {
				blogTypeService.delete(Integer.parseInt(idsStr[i]));
			}
		}
		jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		ResponseUtils.write(response, jsonObject);
		return null;
	}
}
