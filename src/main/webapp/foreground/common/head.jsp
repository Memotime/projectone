<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%--<!-- 头部logo的定义 --> --%>
<div class="row">
	<div class="col-md-4">
		<img alt="程序员小冰技术博客主页" src="${pageContext.request.contextPath}/static/images/logo.png">
	</div>
	<%--<!-- 引入天气的框架 --> --%>
	<div class="col-md-8">
		<iframe style="float: right;" width="420" scrolling="no" height="60" frameborder="0" allowtransparency="true" src="http://i.tianqi.com/index.php?c=code&id=12&icon=1&num=5"></iframe>
	</div>
</div>