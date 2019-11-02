package com.david.emdblog.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Link;
import com.david.emdblog.entity.PageBean;
import com.david.emdblog.service.LinkService;
import com.david.emdblog.utils.LogUtils;
import com.david.emdblog.utils.ResponseUtils;
import com.david.emdblog.utils.UtilFuns;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 友情链接管理类
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@RequestMapping("/admin/link")
@Controller
public class LinkAdminController {

	@Resource(name = "linkService")
	private LinkService linkService;

	/**
	 * 分页查询友情链接信息 page:当前页。rows:每页记录数
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "rows", required = false) String rows, HttpServletResponse response)
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
		// LogUtils.e("start"+pageBean.getStart());
		// LogUtils.e("size"+pageBean.getPageSize());
		List<Link> linkList = linkService.list(map);// 查询到所有链接集合
		Long total = linkService.getTotal(map);// 查询到所有数量
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = JSONArray.fromObject(linkList);// 将集合转换为json
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		System.out.println(jsonObject);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 添加或者修改友情链接的信息
	 */
	@RequestMapping(value = "/save")
	public String save(Link link, HttpServletResponse response) throws Exception {
		int resultTotal = 0;// 操作的记录数
		if (link.getId() == null) {
			// 则说明是新增链接
			resultTotal = linkService.add(link);
		} else {
			// 则说明是修改友情链接的信息
			resultTotal = linkService.update(link);
		}
		JSONObject jsonObject = new JSONObject();
		if (resultTotal > 0) {
			jsonObject.put("success", true);// 操作成功
		} else {
			jsonObject.put("success", false);// 操作失败
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 删除友情链接
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
		String idsStr[] = ids.split(",");// 以逗号进行分割参数
		for (int i = 0; i < idsStr.length; i++) {
			linkService.delete(Integer.parseInt(idsStr[i]));// 递归进行删除
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", true);
		ResponseUtils.write(response, jsonObject);
		return null;
	}
}
