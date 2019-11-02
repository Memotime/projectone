package com.david.emdblog.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.david.emdblog.constant.ApiUrl;
import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blogger;
import com.david.emdblog.service.BloggerService;
import com.david.emdblog.utils.CryptographyUtils;
import com.david.emdblog.utils.LogUtils;

/**
 * 博主信息。站长作者信息
 * @Author ：程序员小冰
 * @新浪微博 ：http://weibo.com/mcxiaobing
 * @GitHub: https://github.com/QQ986945193
 */
@Controller
@RequestMapping("/blogger")
public class BloggerController {
	@Resource(name="bloggerService")
	private BloggerService bloggerService;
	
	/**
	 * 查找博主信息，关于博主
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView abountMe(){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("blogger",bloggerService.findBloggerInfo());
		modelAndView.addObject("mainPage",ApiUrl.FOREGROUND_BLOGGER_INFO);
		modelAndView.addObject("pageTitle","关于博主:"+Constants.BLOG_TITLE);
		modelAndView.setViewName("mainTemp");//转发的页面
		return modelAndView;
	}
	
	/**
	 * 后台的登录
	 */
	@RequestMapping("/login")
	public String login(Blogger blogger,HttpServletRequest request){
		Subject subject = SecurityUtils.getSubject();
//		LogUtils.e(blogger.getUserName());
//		LogUtils.e(blogger.getPassword());
//		LogUtils.e("s:"+CryptographyUtils.md5(blogger.getPassword(), blogger.getUserName()));
		UsernamePasswordToken token = new UsernamePasswordToken(blogger.getUserName(),CryptographyUtils.md5(blogger.getPassword(), blogger.getUserName()));
		try {
			subject.login(token);
			return "redirect:/admin/main.jsp";
		} catch (Exception e) {
			LogUtils.e(e.getMessage());
			//验证失败
			request.setAttribute("blogger",blogger);
			request.setAttribute("errorInfo", "用户名或密码错误!!");
			return "login";//转发到登录页面
		}
		
	}
}
