<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试页面</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="h5/js/jquery.js"></script>
  </head>
  
  <body>
  	<div>
  		<select class='host'>
  			<option value="http://xiaoitest.cn.nuskin.com:8000/dev/product.action">test测试环境</option>
  			<option value="http://xiaoistagetest.cn.nuskin.com:8001/dev/product.action">stage环境</option>
  			<option value="http://xiaoiprodtest.cn.nuskin.com:8001/dev/product.action">生产环境</option>
  		</select><br/>
  		系列id:<input type="text" id="id"/><br/>
  		标题：<input type="text" id="title"/><br/>
  		摘要名：<input type="text" id="des"/><br/>
  		图片链接：<input type="text" id="img"/><br/>
  		<input id='submit' type="submit" value="获取链接"/><br/>
  		
  		<a target="_blank" id='url-a' href="#"></a>
  		
  		<script type="text/javascript">
  			$(document).ready(function(){
  				$("#submit").click(function(){
  					var host = $("select.host").val();
  					var id= $("input#id").val();
  					var title = $("input#title").val();
  					var des = $("input#des").val();
  					var img = $("input#img").val();
  					$.ajax({
  						url:'url',
  						data:{
  							"host":host,
  							"id":id,
  							"title":title,
  							"des":des,
  							"img":img
  						},
  						type:'GET',
  						dataType:'text',
  						success:function(data){
  							$("a").attr("href",data);
  							$("a").html(data);
  						}
  					})
  				})
  			})
  		</script>
  	</div>
  </body>
</html>
