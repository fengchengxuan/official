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
   
    <!-- ngView:  --><div class="ng-scope" ng-view="">
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
    <!---->
    
    <style class="ng-scope">
     .col-xs-10 {width: 100%;}
    
    </style>
    </head>
    <body>﻿
     
    
<div class="row ng-scope" style="margin-left:0px;margin-right:0px;" id="header">
    <div style="text-align: center;">
        <div>
            <img src="<c:url value='/h5/images/logoTop.png'></c:url>" class="img-responsive"/>
        </div>
    </div>
</div>
<div class="container big ng-scope" id="Div1" style="padding-bottom: 60px;">
    <div class="row" style="text-align: center;">
        <div class="col-md-12 col-sm-12 col-xs-12">
            <img src="<%=PropertiesUtil.getValueByKey("addr.prodimage.host")%>${product.mediumIconFileName}" style="max-width:100%;border:none;"/>
        </div>
    </div>

    <div class="row detail">
        <div class="col-md-10 col-sm-10 col-xs-10" style="border-right: 1px solid #eee;">
            <p class="ng-binding" style="margin-bottom:3px;">产品名称：${product.stockName}<span class="graylabel ng-binding" style="float: right;"><del>零售价：<span class="fa fa-rmb" style="font-size: 9px;"></span>${product.retailPrice}</del></span></p> 
            <p class="ng-binding" style="margin-bottom:3px;">英文名称：${product.stockEngName}<span class="graylabel ng-binding" style="float: right; font-size: 16px;"><span style="font-size:12px;">星级顾客价格：</span><span class="fa fa-rmb" style="font-size: 9px;"></span>${product.wholesalePrice}</span></p>
            <p class="graylabelbig ng-binding" style="margin-bottom:1px;">产品编号：${product.stockId}</p>
        </div>

    </div>
    <div class="row " style="margin-top: 15px;">
        <div class="col-md-10 col-sm-10 col-xs-10" style=" border-right: 1px solid #eee;">
            <div><span class="graylabel ng-binding">${product.stockDescription}</span></div>
             <div><span class="red ng-binding" ng-bind="product.shelfLife"></span></div>
        </div>
        <!-- <div class="col-md-2 col-sm-2 col-xs-2">
            <div style="margin-top: 5px; padding-top: 55%; margin-left: 10px;"><span style="font-size: 16px;">产品</span></div>
        </div> -->
    </div>
    </div>

<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>

<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
</body>
</html>