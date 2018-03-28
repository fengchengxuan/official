<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html class="ng-scope" ng-app="mCommerce" lang="zh">
    <head>
    <style type="text/css">@charset "UTF-8";[ng\:cloak],[ng-cloak],[data-ng-cloak],[x-ng-cloak],.ng-cloak,.x-ng-cloak,.ng-hide{display:none !important;}ng\:form{display:block;}.ng-animate-block-transitions{transition:0s all!important;-webkit-transition:0s all!important;}.ng-hide-add-active,.ng-hide-remove{display:block!important;}
    </style>    
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
   	<meta charset="utf-8"/>
   	<meta name="format-detection" content="telephone=no" />
	<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge,chrome=1"/>
    <title>订单兑换详情</title>
    <link href="<c:url value='/h5/css/bootstrap.min1.css'></c:url>" rel="stylesheet"/>
	<link href="<c:url value='/h5/css/top.css'></c:url>" rel="stylesheet"/>
   	<link href="<c:url value='/h5/css/nuskin.css'></c:url>" rel="stylesheet"/>
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
    <!---->
    
	<style class="ng-scope">
     .popover-content{padding: 4px;color: white;opacity: 1;}
     .popover.top > .arrow:after{bottom: 1px;margin-left: -10px;content: " ";border-top-color: #76AEC4;border-bottom-width: 0;}
     .arrow{/*  background-color:#007F99;*/}
     .popover{background-image:url('../images/test.png');background-color:transparent;/* -moz-opacity: 0.8;opacity: 0.8;*/}
   	 div.tab{margin:10px 0 20px 0; auto;width:100%;border:1px solid #DDDDDD;padding:1px;}
	 table{font-family: "微软雅黑","Helvetica Neue",Helvetica,Arial,sans-seri;font-size:12px;width:100%;background: none repeat scroll 0 0 #fff;border: 1px solid #dddddd;border-radius: 3px;}
	 table thead{background: none repeat scroll 0 0 #F0F0F0;}
	 table thead tr th{height:30px;color: #333;font-weight: normal;padding:3px;vertical-align:middle;text-align: center;}
	 table tr th,table tr td{border:1px solid #D3D3D3; display: table-cell;font-size: 12px;line-height: 18px;text-align: left;}
	 th.header { background: none repeat scroll 0 0 rgba(0, 0, 0, 0);border-bottom: medium none;height: auto;min-height: auto;width: auto;}
	 table tbody tr td{padding-left:10px;display: table-cell;height:25px;color: #333;vertical-align:middle;}
	 table tr.co td{background-color:#F9F9F9;}
    </style>
    </head>
    <body>﻿
     
    <!-- ngView:  -->
   	<div class="ng-scope">
    
		<div class="row ng-scope" style="margin-left:0px;margin-right:0px;" id="header">
		    <div style="text-align: center;">
		        <div>
		            <img src="<c:url value='/h5/images/logoTop.png'></c:url>" class="img-responsive"/>
		        </div>
		    </div>
		</div>
		
	<!-- 内容 -->
	<div class="tab">
		<c:if test="${empty list}">
			<p class="ng-binding" style="margin-bottom:3px;color:#FF2A2A;">抱歉，没有可以查询的结果。</p> 
		</c:if>
		<c:if test="${!empty list}">
			<table class="tablesorter" id="historyOrderTable">
		    	<thead>
		    		<tr>
			         	<th style="text-align:center;" class="header">产品</th>
			         	<th style="text-align:center;">价格</th>
			         	<th style="text-align:center;">数量</th>
		        	</tr>
		     	</thead>
		     	<tbody>
		     	<c:forEach items="${list}" var="orderentry">
		     		<tr>
		     			<td>${orderentry.stockName}</td>
		     			<td>${orderentry.retailPrice}</td>
		     			<td>${orderentry.qty}</td>
		     		</tr>
		     	</c:forEach>
		     	</tbody>
		     </table>
	     </c:if>
	</div>
	<!-- 内容结束 -->
		<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>
		<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
	</div>
</body>
</html>
