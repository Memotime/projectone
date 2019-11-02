package com.david.emdblog.shiro;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.aspectj.weaver.Dump.INode;

import com.alibaba.druid.sql.parser.Token;
import com.david.emdblog.constant.Constants;
import com.david.emdblog.entity.Blogger;
import com.david.emdblog.service.BloggerService;

/**
 * 自定义Realm shiro
 */
public class MyRealm extends AuthorizingRealm {

	@Resource(name = "bloggerService")
	private BloggerService bloggerService;

	/**
	 * 为当前登陆的用户授予角色和权限 授权，当jsp页面出现shiro标签时，就会执行授权方法
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		return null;
	}

	/**
	 * 验证当前登陆的用户
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
			throws AuthenticationException {
		// 向下转型
		// UsernamePasswordToken upToken = (UsernamePasswordToken)
		// authenticationToken;
		String userName = (String) authenticationToken.getPrincipal();

		Blogger dbBlogger = bloggerService.findBloggerByUsername(userName);
		if (dbBlogger != null) {
			// 证明用户名正确。 将当前用户信息直接存到session中
			SecurityUtils.getSubject().getSession().setAttribute(Constants.SESSION_CURRENT_USER_INFO, dbBlogger);
			AuthenticationInfo info = new SimpleAuthenticationInfo(dbBlogger.getUserName(), dbBlogger.getPassword(),
					dbBlogger.getUserName());
			return info;
		}
		return null;
	}

}
