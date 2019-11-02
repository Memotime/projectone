<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<script type="text/javascript">
	function checkData(){
		var q=document.getElementById("q").value.trim();
		if(q==null || q==""){
			alert("请输入您要查询的关键字！");
			return false;
		}else{
			return true;
		}
	}
</script>
<%--<!-- 头部tab标题的定义 --> --%>
<div class="row">
	<div class="col-md-12" style="padding-top: 10px">
		<nav class="navbar navbar-default">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed"
						data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
						aria-expanded="false">
						<span class="sr-only">Toggle navigation</span> <span
							class="icon-bar"></span> <span class="icon-bar"></span> <span
							class="icon-bar"></span>
					</button>
					<%-- 显示首页的内容，调用controller的index方法. -->--%>
					<a class="navbar-brand"
						href="${pageContext.request.contextPath}/index.html"><font
						color="black"><strong>首页</strong></font></a>
				</div>

				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav">
						<%-- <li><a href="#"><font color="black"><strong>github主页</strong></font></a></li> --%>
						<%-- <li><a href="#"><font color="black"><strong>Java学习路线图</strong></font></a></li> --%>
						<%-- <li><a href="#"><font color="red"><strong>程序员小冰</strong></font></a></li> --%>
						<%-- 显示首页的内容，调用controller的aboutMe方法. -->--%>
						<li><a
							href="${pageContext.request.contextPath}/blogger/aboutMe.html"><font
								color="black"><strong>关于博主</strong></font></a></li>
								
									<li><a
							href="https://github.com/QQ986945193" target="_blank"><font
								color="black"><strong>github</strong></font></a></li>
								
						<li><a
							href="${pageContext.request.contextPath}/download.html"><font
								color="black"><strong>本站源码下载</strong></font></a></li>
					</ul>
					<!-- 搜索查询 -->
					<form action="${pageContext.request.contextPath}/blog/q.html"
						class="navbar-form navbar-right" role="search" method="post"
						onsubmit="return checkData()">
						<div class="form-group">
							<input type="text" id="q" name="q" value="${q }"
								class="form-control" placeholder="请输入要查询的关键字...">
						</div>
						<button type="submit" class="btn btn-default">搜索</button>
					</form>
				</div>
				<!-- /.navbar-collapse -->
			</div>
			<!-- /.container-fluid -->
		</nav>
	</div>
</div>