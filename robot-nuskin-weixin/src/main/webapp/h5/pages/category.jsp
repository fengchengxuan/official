<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.eastrobot.robotdev.utils.PropertiesUtil"%>
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
    <title>网上购物</title>

<link href="<c:url value='/h5/css/bootstrap.min1.css'></c:url>" rel="stylesheet"/>
<link href="<c:url value='/h5/css/top.css'></c:url>" rel="stylesheet"/>
<style>
        h6
        {
            height: 25px;
        }
		.description p{text-indent:26px;margin-bottom:0px;}
</style>
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
</head>
<body>﻿
<div class="row ng-scope" style="margin-left: 0px; margin-right: 0px;" id="header">
    <div style="text-align: center;">
        <div>
            <img src="<c:url value='/h5/images/logoTop.png'></c:url>" class="img-responsive"/>
        </div>
    </div>
</div>
<div class="container big ng-scope" style="padding-bottom: 60px;">


    <!-- begin:best-seller -->
    <c:if test="${!empty title}">
	    <div class="row" ng-show="showProList">
	        <div class="col-md-12">
	            <div class="page-header">
	                <h3 style="color: #007F99; margin-bottom: -5px;">${title}</h3>
	                <div style="clear: both;"></div>
	            </div>
	        </div>
	    </div>
	    <div>
	    	<div>
	    		<img src="${img}" style="max-width: 100%;"/>
	    	</div>
			<div class="description" style="margin-top:10px;">
		    	${description}
		    </div>
		</div>
	</c:if>
    <div class="row" ng-show="showProList">
        <div class="col-md-12">
            <div class="page-header">
                <h3 style="color: #007F99; margin-bottom: -5px;">分类产品</h3>
                <div style="clear: both;"></div>
            </div>
        </div>
    </div>
    <div class="row" ng-show="error.showList">
        <!-- ngRepeat: product in products -->
        <c:forEach items="${list}" var="category">
        <div class="col-md-3 col-sm-3 col-xs-6 ng-scope" name="productsquare" ng-repeat="product in products">
            <div class="thumbnail product-item">
                <a href="<c:url value='/product!show.action?id=${category.stockId}'></c:url>">
                    <img src="<%=PropertiesUtil.getValueByKey("addr.prodimage.host")%>${category.largeIconFileName}" style="max-width:100%;" alt="${category.stockName}" class="img-responsive"/>
                    <div class="caption" style="padding: 1px; margin-top: -5px;">
                        <h6 class="proname ng-binding">${category.stockName}</h6>
                        <span class="proprice pricesingle"><del class="ng-binding">零售价：<span class="fa fa-rmb" style="font-size: 9px;"></span>${category.retailPrice}</del></span><br/>
                        <span class="vipprice pricevip ng-binding">星级顾客价格：<br/>
                            <span class="pricestyle ng-binding"><span class="fa fa-rmb" style="font-size: 14px;"></span>${category.wholesalePrice}</span></span>
                    </div>
                </a>
            </div>
        </div>
        </c:forEach>
       	<!-- end ngRepeat: product in products -->
    </div>
    
</div>
<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>

<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
</body>
</html>