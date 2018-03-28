<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>网上购物</title>
    <link href="<c:url value='/h5/css/bootstrap.min1.css'></c:url>" rel="stylesheet"/>
	<link href="<c:url value='/h5/css/top.css'></c:url>" rel="stylesheet"/>
   
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
    <!---->
    
    
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
<div class="container big ng-scope" id="Div1" style="padding-bottom: 60px;">
    <div class="row detail">
        <div class="col-md-10 col-sm-10 col-xs-10" style="border-right: 1px solid #eee;">
            <p class="ng-binding" style="margin-bottom:3px;color:#FF2A2A;">${tips}</p> 
        </div>
    </div>
</div>

<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>

<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
</div>
</body>
</html>