package com.david.emdblog.web.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.constant.JsonConstant;
import com.david.emdblog.entity.Comment;
import com.david.emdblog.entity.PageBean;
import com.david.emdblog.service.CommentService;
import com.david.emdblog.utils.ResponseUtils;
import com.david.emdblog.utils.UtilFuns;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 评论管理的controller
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Controller
@RequestMapping("/admin/comment")
public class CommentAdminController {
	@Resource(name = "commentService")
	private CommentService commentService;

	/**
	 * 分页查询所有的评论 page 第几页开始。 rows:每页记录数 state:评论的状态
	 */
	@RequestMapping("/list")
	public String list(@RequestParam(value = "page", required = false) String page,
			@RequestParam(value = "rows", required = false) String rows,
			@RequestParam(value = "state", required = false) String state, HttpServletResponse response)
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
		map.put("state", state);// 评论的状态
		List<Comment> commentList = commentService.list(map);// 获取评论的列表
		Long total = commentService.getTotal(map);// 获取评论的总数量
		JSONObject jsonObject = new JSONObject();
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		// 因为有日期，所以转换一下
		JSONArray jsonArray = JSONArray.fromObject(commentList, jsonConfig);
		jsonObject.put("rows", jsonArray);
		jsonObject.put("total", total);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 删除评论
	 */
	@RequestMapping("/delete")
	public String delete(@RequestParam(value = "ids") String ids, HttpServletResponse response) throws Exception {
		// 这个ids是评论的id，由于可能批量操作，所以可能是数组
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			commentService.delete(Integer.parseInt(idsStr[i]));// 删除评论，根据id
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 评论审核，更改评论的状态
	 */
	@RequestMapping("/review")
	public String review(@RequestParam(value = "ids") String ids, @RequestParam(value = "state") Integer state,
			HttpServletResponse response) throws Exception {
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			Comment comment = new Comment();
			comment.setState(state);
			comment.setId(Integer.parseInt(idsStr[i]));
			commentService.update(comment);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
		ResponseUtils.write(response, jsonObject);
		return null;
	}
}
