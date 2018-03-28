<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.eastrobot.robotface.RobotMessageResource"%>
<%@page import="com.eastrobot.robotface.RobotMessageResourceProxy"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ng-scope" ng-app="mCommerce" lang="zh">
    <head>
    <style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide{display:none !important;}ng\:form{display:block;}.ng-animate-block-transitions{transition:0s all!important;-webkit-transition:0s all!important;}.ng-hide-add-active,.ng-hide-remove{display:block!important;}
    </style>    
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <meta charset="utf-8"/>
   
<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <title>意见收集</title>
    <link href="<c:url value='/h5/css/bootstrap.min1.css'></c:url>" rel="stylesheet"/>
	<link href="<c:url value='/h5/css/top.css'></c:url>" rel="stylesheet"/>
   
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
    <!---->
    <style>
    	#Div1{display:none;padding-bottom: 60px;}
    	#Div2{border:1px solid #E1E1E1;width:100%;text-align:center;}
		#Div2 .title{width:100%;padding-left:5px;min-height:30px;color:#717073;background-color:#F2F2F2;text-align:left;font-size:14px;line-height:30px;}
    	#input{width:100%;height:80px;color:#717073;padding:3px;border-radios:5px;}
    	#input::-webkit-input-placeholder {color:#999;-webkit-transition: color.5s;}
		#input:focus::-webkit-input-placeholder,#input:hover::-webkit-input-placeholder {color: #c2c2c2;-webkit-transition: color.5s;} 
    	
    	#submit_btn{
		    margin:5px;
		    background: #008ab0 none repeat scroll 0 0;
		    border: medium none;
		    border-radius: 3px;
		    color: white;
		    cursor: pointer;
		    display: inline-block;
		    font-family: inherit;
		    font-size: 14px;
		    font-weight: bold;
		    line-height: 1;
		    padding: 8px 25px 9px;
		    position: relative;
		    text-align: center;
		    text-decoration: none;
		    text-transform: uppercase;
		    transition: background-color 0.15s ease-in-out 0s;
		    width: auto;
		}
    </style>
    <%
    	RobotMessageResource rmrp = new RobotMessageResourceProxy();
    	String tips = rmrp.get("FaqVotePageHeaderTips","All");
    	if(tips==null || tips=="")
    	 tips = "请您填写宝贵意见，小如新会努力学习，不断优化的哦！";
    %>
    </head>
    <body>﻿ 
    <!-- ngView:  --><div class="ng-scope" ng-view="">
    
<div class="row ng-scope" style="margin-bottom: 20px;margin-left:0px;margin-right:0px;" id="header">
    <div style="text-align: center;">
        <div>
            <img src="<c:url value='/h5/images/logoTop.png'></c:url>" class="img-responsive"/>
        </div>
    </div>
</div>
<div class="container big ng-scope" id="Div1">
    <div class="row detail">
        <div class="col-md-10 col-sm-10 col-xs-10" style="border-right: 1px solid #eee;">
            <p class="ng-binding" style="margin-bottom:3px;color:#FF2A2A;"></p> 
        </div>
    </div>
</div>

<div id='Div2'>
	<div class='title'><%=tips%></div>
	<div>
		<form action="faq-vote.action" method="post">
			<!-- <input id="input" name='reason' maxlength="100" placeholder="请在这里输入，限制100字"></input> -->
			<textarea id='input' name='reason' rows="" cols=""></textarea>
			<input style='display:none;' class='msgid' type='text' name='msgid'/>
			<input style='display:none;' class='userid' type='text' name='userId'/>
			<input type="submit" id='submit_btn' value='提&nbsp;&nbsp;交'/>
		</form>
	</div>
</div>
<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>

<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
</div>
</body>
<script>
$(document).ready(function(){
	var msgid = getQueryString("msgid");
	var time = getQueryString("time");
	var currentTime = new Date().getTime();
	console.log(currentTime)
	var div1 = $("#Div1");
	var div2 = $("#Div2");
	if(currentTime-time<1200000)
	{
		$.ajax({
			url:'faq-vote!getstatus.action',
			type:'post',
			data:{
			'msgid':msgid
			},
			dataType:'json',
			success:function(data){
				if(!data)
				{
					div1.css({"display":"none"});
					div2.css({"display":"block"});
					$("#textarea").val("请在这里输入，限制100字");
				}else{
					div1.css({"display":"block"});
					div2.css({"display":"none"});
					div1.find("p").text("亲爱的用户，您已经对该问题做出了反馈，我们将尽快处理，如有问题请继续咨询小如新吧。");
				}
			}
		})
	}else{
		div1.css({"display":"block"});
		div2.css({"display":"none"});
		div1.find("p").text("抱歉，该链接已经失效，如需查询请返回聊天界面重新询问。");
	}
	
	
	function getQueryString(name)
	{  
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");  
	    var r = location.search.substr(1).match(reg);  
	    if (r != null) return unescape(decodeURI(r[2])); return null;
	}
	
	$("#submit_btn").click(function(){
		var reason = $("#input").val();
		if(reason)
		{
			$(".msgid").val(getQueryString("msgid"));
			$(".userid").val(getQueryString("userId"));
			$(this).submit();
		}else{
			alert("内容不能为空");
			return false;
		}
	})
})
</script>
</html>