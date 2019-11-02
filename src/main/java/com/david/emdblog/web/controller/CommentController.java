package com.david.emdblog.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.david.emdblog.constant.JsonConstant;
import com.david.emdblog.entity.Blog;
import com.david.emdblog.entity.Comment;
import com.david.emdblog.service.BlogService;
import com.david.emdblog.service.CommentService;
import com.david.emdblog.utils.ResponseUtils;
import com.david.emdblog.utils.UtilFuns;

import net.sf.json.JSONObject;

/**
 * 评论的功能。
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

	@Resource(name="commentService")
	private CommentService commentService;
	
	@Resource(name="blogService")
	private BlogService blogService;
	
	/**
	 * 添加或者修改评论
	 */
	@RequestMapping("/save")
	public String save(Comment comment,@RequestParam("imageCode")String imageCode,
			HttpServletRequest request,HttpServletResponse response,HttpSession session) throws Exception{
		String codeImg = (String) session.getAttribute("sRand");//获取系统生成的验证码
		JSONObject jsonObject = new JSONObject();
		int resultNum = 0;//操作的记录数
		if (UtilFuns.isNotEmpty(imageCode) && imageCode.equals(codeImg)) {
			//如果相等则正确，否则错误
			String userIp = request.getRemoteAddr();//获取用户ip
			comment.setUserIp(userIp);
			if (comment.getId() == null) {
				resultNum = commentService.add(comment);
				//添加完评论之后，我们需要将文章的回复次数加1
				Blog blog = blogService.findById(comment.getBlog().getId());
				blog.setReplyHit(blog.getReplyHit()+1);
				blogService.update(blog);
			}
			if (resultNum>0) {
				//操作成功
				jsonObject.put(JsonConstant.SUCCESS, JsonConstant.TRUE);
			}else {
				jsonObject.put(JsonConstant.SUCCESS, JsonConstant.FALSE);
			}
		}else {
			jsonObject.put(JsonConstant.SUCCESS, JsonConstant.FALSE);
			jsonObject.put("errorInfo", "验证码填写错误");
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}
	
}
