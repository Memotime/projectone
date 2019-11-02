<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh">
   
   <head>
        <meta charset="utf-8" />
		<!--引入样式文件-->
        <link rel="stylesheet" href="editormdmaster/examples/css/style.css" />
        <link rel="stylesheet" href="editormdmaster/css/editormd.css" /> 
		<!--引入js文件-->
        <script src="editormdmaster/examples/js/jquery.min.js"></script>
        <script src="editormdmaster/editormd.js"></script>     
        <link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/themes/icon.css">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.min.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/static/jquery-easyui-1.3.3/jquery.easyui.min.js"></script> 
    </head>
	
    <body>
	
    
        
	   <!-- 	写一个隐藏的div,存放需要初始化的内容,用作假设后台传回的数据演示
		<div id="dateDemo" style="display:none;">
    hadoop命令需在hadoop目录下使用
**常用命令**
    
    HDFS格式化:
    bin/hadoop namenode -format    格式化namenode
    bin/hadoop datanode -format    格式化datanode
    
    *如不是第一次格式化，为了保证clusterID一致性,
    *需先删除hdfs临时目录,
    *即在配置文件中配置的hadoop.tmp.dir参数路径
		

`图片展示1`
[![这里是描述](http://pic41.nipic.com/20140529/18243620_101015342117_2.gif "这里是描述")](这里是链接地址 "这里是描述")

[========]
图片展示2
![](http://pic41.nipic.com/20140529/18243620_101015342117_2.gif)
	</div> -->
	<div id="dateDemo" style="display:none;"></div> 
	<body style="margin: 10px">
	<div id="p" class="easyui-panel" title="编写文章" style="padding: 10px">
		<table cellspacing="20px">
			<tr>
				<td width="80px">文章标题：</td>
				<td><input type="text" id="title" name="title"
					style="width: 400px;" /></td>
			</tr>
			<tr>
				<td>所属类别：</td>
				<td><select class="easyui-combobox" style="width: 154px"
					id="blogTypeId" name="blogType.id" editable="false"
					panelHeight="auto">
						<option value="">请选择文章类别...</option>
						<c:forEach var="blogType" items="${blogTypeCountList }">
							<option value="${blogType.id }">${blogType.typeName }</option>
						</c:forEach>
				</select></td>
			</tr>
			
			<tr >
				<td valign="top">文章内容：</td>
				<td >
		            <div id="test-editormd"></div>
		            <div class="btns"  nowrap><nobr>
	                <button id="show-btn">显示编辑器</button>
	                <button id="hide-btn">隐藏编辑器</button>
	                <button id="get-md-btn">获取Markdown内容</button>
	                <button id="get-html-btn">获取HTML内容</button>
	                <button id="watch-btn">显示在线预览</button>
	                <button id="submit_blog"  style="background-color:#FF0000">提交文章</button>
	                <button id="unwatch-btn">隐藏在线预览</button>
	                <button id="preview-btn">预览HTML（按Shift + ESC取消）</button>
	                <button id="fullscreen-btn">全屏（按ESC取消）</button>
	                <button id="show-toolbar-btn">显示工具栏</button>
	                <button id="close-toolbar-btn">隐藏工具栏</button>
	            </nobr></div>
				</td>
			</tr>
			
			<tr>
				<td>关键字：</td>
				<td><input type="text" id="keyWord" name="keyWord"
					style="width: 400px;" />&nbsp;(多个关键字中间用空格隔开)</td>
			</tr>
		</table>
	</div>
		
		<script type="text/javascript">
			var testEditor;//用来保存实例化的编辑器对象
			
            $(function() {
                
				//====================实例化化编辑器开始====================
				//该get方法是jquery的,需要请求后台返回test.md的文件内容
				//为了方便演示,我们直接将初始化数据写死吧
                //$.get('请求地址', function(md){
                   testEditor = editormd("test-editormd", {
                        width: "100%",
                        height: 940,
                        path : 'editormdmaster/lib/', //指定lib目录
						//theme : "dark",					//工具栏使用dark主题,默认白色
                        //previewTheme : "dark",			//预览区使用dark主题,默认白色
                        //editorTheme : "pastel-on-dark", //编辑预览区使用dark主题,默认白色
                        //markdown : md,				//这个参数是请求后台返回的数据,为了方便演示就写死吧,不从后台取了
						markdown : $("#dateDemo").html(),//获取div中的内容作为初始化数据
                        codeFold : true,
                        //syncScrolling : false,
                        saveHTMLToTextarea : true,   	// 保存 HTML 到 Textarea
                        searchReplace : true,
                        //watch : false,                // 关闭实时预览
                        htmlDecode : "style,script,iframe|on*", // 开启HTML标签解析,为了安全性,默认不开启    
                        //toolbar  : false,             //关闭工具栏
                        //previewCodeHighlight : false, //关闭预览HTML的代码块高亮,默认开启
                        emoji : true,
                        taskList : true,
                        tocm            : true,       	// Using [TOCM]
                        tex : true,                  	// 开启科学公式TeX语言支持，默认关闭
                        flowChart : true,             	// 开启流程图支持，默认关闭
                        sequenceDiagram : true,      	// 开启时序/序列图支持，默认关闭,
                        //dialogLockScreen : false,  	// 设置弹出层对话框不锁屏，全局通用，默认为true
                        //dialogShowMask : false,    	// 设置弹出层对话框显示透明遮罩层，全局通用，默认为true
                        //dialogDraggable : false,    	// 设置弹出层对话框不可拖动，全局通用，默认为true
                        //dialogMaskOpacity : 0.4,   	// 设置透明遮罩层的透明度，全局通用，默认值为0.1
                        //dialogMaskBgColor : "#000",	// 设置透明遮罩层的背景颜色，全局通用，默认为#fff
                        imageUpload : true,
                        imageFormats : ["jpg", "jpeg", "gif", "png", "bmp", "webp"],
                        imageUploadURL : "./mdFileUploadImage/uploadMarkdownImage.do",//文件上传的后台请求地址
						toolbarIcons : function() {//设置需要的工具图标,这个可以不写使用默认的
							var tool=["undo", "redo", "|", "bold","del","italic", "quote","ucwords","uppercase","lowercase","|",
							"h1","h2","h3","h4","h5","h6","|","list-ul","list-ol","hr","|","link","reference-link","image","code",
							"preformatted-text","code-block","table","datetime","emoji","html-entities","pagebreak","|","goto-line",
							"watch","preview","fullscreen","clear","search"]
                        return tool;
						},
                        onload : function() {
                            console.log('onload', this);
                            //this.fullscreen();
                            //this.unwatch();
                            //this.watch().fullscreen();

                            //this.setMarkdown("#PHP");
                            //this.width("100%");
                            //this.height(480);
                            //this.resize("100%", 640);
                        }
                    });
                //});
				//====================实例化化编辑器结束====================
                
				//================== 功能开始=======================
                
				//显示编辑器
                $("#show-btn").bind('click', function(){
                    testEditor.show();
                });
                
				//隐藏编辑器
                $("#hide-btn").bind('click', function(){
                    testEditor.hide();
                });
                
				//获取Markdown内容
                $("#get-md-btn").bind('click', function(){
                    alert(testEditor.getMarkdown());
                });
                
				//获取HTML内容
                $("#get-html-btn").bind('click', function() {
                    alert(testEditor.getHTML());
                });                
                
				//显示在线预览
                $("#watch-btn").bind('click', function() {
                    testEditor.watch();
                });                 
                
				//隐藏在线预览
                $("#unwatch-btn").bind('click', function() {
                    testEditor.unwatch();
                });              
                
				//预览HTML（按Shift + ESC取消）
                $("#preview-btn").bind('click', function() {
                    testEditor.previewing();
                });
                
				//全屏（按ESC取消）
                $("#fullscreen-btn").bind('click', function() {
                    testEditor.fullscreen();
                });
                
				//显示工具栏
                $("#show-toolbar-btn").bind('click', function() {
                    testEditor.showToolbar();
                });
                
				//隐藏工具栏
                $("#close-toolbar-btn").bind('click', function() {
                    testEditor.hideToolbar();
                });
                
            	//提交日志
                $("#submit_blog").bind('click', function() {
                	submitData();
                	
                });
				//================== 功能结束=======================
				
            });
						
        </script>
        
<script type="text/javascript">
	function submitData(){
		var title=$("#title").val();
		var blogTypeId=$("#blogTypeId").combobox("getValue");
		//获取Markdown内容
		var content=testEditor.getHTML();
		var keyWord=$("#keyWord").val();
		
		if(title==null || title==''){
			alert("请输入文章标题！");
		}else if(blogTypeId==null || blogTypeId==''){
			alert("请选择文章类别！");
		}else if(content==null || content==''){
			alert("请输入内容！");
		}else{
			$.post("${pageContext.request.contextPath}/admin/blog/saveMarkdownBlog.do",{'title':title,'blogType.id':blogTypeId,'content':content,'contentNoTag':testEditor.getMarkdown(),'summary':testEditor.getMarkdown().substr(0,155),'keyWord':keyWord},function(result){
				if(result.success){
					alert("文章发布成功！");
					resetValue();
				}else{
					alert("文章发布失败！");
				}
			},"json");
		}
	}
	
	// 重置数据
	function resetValue(){
		$("#title").val("");
		$("#blogTypeId").combobox("setValue","");
		$("#dateDemo").html()
		$("#keyWord").val("");
	}
	
</script>
    </body>
</html>