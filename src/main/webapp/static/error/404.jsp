<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>程序员小冰---not found error</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/error/css/style.css">
<script src="${pageContext.request.contextPath}/static/error/js/jquery.js"></script>
<script src="${pageContext.request.contextPath}/static/error/js/public.js"></script>
</head>
<body>
	<div id="content" class="cf">
		<div id="duobei_wrap">
			<div class="logo_wrap cf">
				<a href="https://github.com/QQ986945193" id="logo"></a>
				<div id="title">
					<i>S</i><i>O</i><i>R</i><i>R</i><i>Y</i>
				</div>
			</div>
			<div class="reason">
				<p class="not_found_tip">Not Found :( 很抱歉，您访问的页面不存在!</p>
				<p class="possible">可能原因：</p>
				<ul>
					<li>该数据已被删除，但却被搜索引擎收录了，通过搜索引擎访问就会看到此页面</li>
					<li>您手动输入了一个从来没有的访问url，这样系统也是无法识别您的url，报错</li>
				</ul>
			</div>
		</div>
		<!-- #duobei_wrap -->
		<div class="line"></div>

		<div class="not_found">
			<i class="ribbon"></i>
			<div class="not_found_404 cf">
				<span>4</span> <span>0</span> <span>4</span>
			</div>
			<div class="btn">
				<a href="https://github.com/QQ986945193"
					class="button button-rounded">查看github</a> <a href="#"
					class="button button-rounded cancle">返回上页</a>
			</div>
		</div>
		<!-- not_found -->

	</div>
</body>
</html>