package com.david.emdblog.web.controller.admin;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blogger;
import com.david.emdblog.service.BloggerService;
import com.david.emdblog.utils.CryptographyUtils;
import com.david.emdblog.utils.DateUtil;
import com.david.emdblog.utils.ResponseUtils;

import net.sf.json.JSONObject;

/**
 * 后台博主的信息controller
 * 
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@RequestMapping("/admin/blogger")
@Controller
public class BloggerAdminController {

	@Resource(name = "bloggerService")
	public BloggerService bloggerService;

	/**
	 * 注销账户
	 */
	@RequestMapping(value = "/logout")
	public String logout() {
		// 退出，shiro，清除session
		SecurityUtils.getSubject().logout();
		return "redirect:/login.jsp";
	}

	/**
	 * 修改账户密码
	 */
	@RequestMapping(value = "/modifyPassword")
	public String modifyPassword(String newPassword, HttpServletResponse response) throws Exception {
		Blogger blogger = (Blogger) SecurityUtils.getSubject().getSession()
				.getAttribute(Constants.SESSION_CURRENT_USER_INFO);
		// 设置新密码
		blogger.setPassword(CryptographyUtils.md5(newPassword, blogger.getUserName()));
		/**
		 * 然后更新数据库中的密码
		 */
		int resultTotal = bloggerService.update(blogger);
		JSONObject jsonObject = new JSONObject();
		if (resultTotal > 0) {
			jsonObject.put("success", true);
		} else {
			jsonObject.put("success", false);
		}
		ResponseUtils.write(response, jsonObject);
		return null;
	}

	/**
	 * 修改博主信息
	 */
	@RequestMapping("/save")
	public String save(@RequestParam("imageFile") MultipartFile imageFile, Blogger blogger,
			HttpServletResponse response, HttpServletRequest request) throws Exception {
		if (!imageFile.isEmpty()) {
			String filePath = request.getServletContext().getRealPath("/");
			// 设置文件名称
			String imageName = DateUtil.getCurrentDateStr() + "." + imageFile.getOriginalFilename().split("\\.")[1];
			// 将文件保存到指定目录下
			imageFile.transferTo(new File(filePath + "static/userImages/" + imageName));
			blogger.setImageName(imageName);
		}
		int resultTotal = 0;
		resultTotal = bloggerService.update(blogger);
		StringBuffer sb = new StringBuffer();
		if (resultTotal > 0) {
			sb.append("<script language='javascript'>alert('修改成功！')</script>");
		} else {
			sb.append("<script language='javascript'>alert('修改失败!')</script>");
		}
		ResponseUtils.write(response, sb);
		return null;
	}

	/**
	 * UE查询博主的信息
	 */
	@RequestMapping("/find")
	public String find(HttpServletResponse response) throws Exception {
		Blogger blogger = bloggerService.findBloggerInfo();
		JSONObject jsonObject = JSONObject.fromObject(blogger);
		ResponseUtils.write(response, jsonObject);
		return null;
	}
}
