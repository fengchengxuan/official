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
    <title>个人绩效评估</title>
    <link href="<c:url value='/h5/css/bootstrap.min1.css'></c:url>" rel="stylesheet"/>
	<link href="<c:url value='/h5/css/top.css'></c:url>" rel="stylesheet"/>
   	<link href="<c:url value='/h5/css/nuskin.css'></c:url>" rel="stylesheet"/>
    <script src="<c:url value='/h5/js/jquery.js'></c:url>"></script>
    <!---->
    
    
    </head>
    <body>﻿
     
    <!-- ngView:  -->
   	<div class="ng-scope">
    
		<div class="row ng-scope" style="margin-bottom: 20px;margin-left:0px;margin-right:0px;" id="header">
		    <div style="text-align: center;">
		        <div>
		            <img src="<c:url value='/h5/images/logoTop.png'></c:url>" class="img-responsive"/>
		        </div>
		    </div>
		</div>
		
	<!-- 内容 -->
		<div style="text-align: center;">
			<div class="row">
	        	<div class="txt_content">
					<h1 class="txt_content_h1">尊敬的<span class="txt_content_blue">${eval.distName} ${eval.currentTitle}</span>您好!</h1>
					<p>衷心地感谢您在<span class="txt_content_blue">${eval.evalYear}</span>年第<span class="txt_content_blue">${eval.evalQuater}</span>季度(<span class="txt_content_blue">${eval.evalBMonth}</span>至<span class="txt_content_blue">${eval.evalEMonth}</span>月)的努力工作，现绩效评估已经完成，公司将从<span class="txt_content_blue">${eval.adjYear}</span>年<span class="txt_content_blue">${eval.adjMonth}</span>月起开始调整您于第<span class="txt_content_blue">${eval.adjQuater}</span>季度的职位及基本工资。</p>
					<p>此次基本工资的调整是在公司对您进行综合绩效评估的基础上确定的，评估的的项目包含以下几项：</p>
					<p>
					</p><ul class="txt_content_list">
						<li>● 遵守公司规章制度的能力</li>
						<li>● 开发新顾客及与顾客维持长期关系的能力</li>
						<li>● 产品销售能力</li>
						<li>● 管理及培训其他事业经营伙伴的能力</li>
						<li>● 推广品牌及公司文化的能力</li>
						<li>● 领导与监督能力及公司策略贡献及执行的能力</li>
					</ul>
					<p></p>
					<p>您的整体评分为<span class="txt_content_blue">${eval.totalScore}</span>，为依据您的业绩表现以及上述项目评估的表现状况，若您在<span class="txt_content_blue">${eval.basepayBMonth}</span>-<span class="txt_content_blue">${eval.basepayEMonth}</span>月能按时完成公司的销售指标并没有违反公司各项规章制度的行为，您在<span class="txt_content_blue">${eval.basepayBMonth}</span>月、<span class="txt_content_blue">${eval.basepayMMonth}</span>月和<span class="txt_content_blue">${eval.basepayEMonth}</span>月的职位为<span class="txt_content_blue">${eval.evalTitle}</span>，职位工资为<span class="txt_content_blue">${eval.basepay}</span>元人民币，绩效工资为<span class="txt_content_blue">${eval.prpay}</span>元人民币，评估过后的职位/绩效工资将于<span class="txt_content_blue">${eval.basepayMMonth}</span>月10日开始发放。欢迎您于周一至周五10：00&mdash;19：00，致电400-004-5678与您的事业发展伙伴：<span class="txt_content_blue">${eval.epName}</span>联系对评估结果进行详细了解。如您有任何疑问，请在<span class="txt_content_blue">${eval.basepayMMonth}</span>月20日前以书面形式向您所在的分公司提出，否则视为您同意公司对您的评估结果。感谢您的理解和配合，我们将竭诚为您提供完善的服务。</p>
					<p>希望您能够再接再厉，不断地挑战自我，争创销售佳绩。公司下一次的季度评估将于<span class="txt_content_blue">${eval.nextEvalYear}</span>年的<span class="txt_content_blue">${eval.nextEvalMonth}</span>月进行，衷心祝愿您通过第<span class="txt_content_blue">${eval.adjQuater}</span>季度的加倍努力, 会有更丰硕的收获。</p>
					<p>再次为您加油！</p>
					<p style="margin-top:0;">祝您一切如新！</p>
					<p style="float:right;text-align:right;">
						如新（中国）日用保健品有限公司<br/>
						<span class="txt_content_blue">${eval.letterYear}</span>年<span class="txt_content_blue">${eval.basepayBMonth}</span>月<span class="txt_content_blue">${eval.basepayMonthEndDay}</span>日
					</p>
				</div>
			</div>
		</div>
	<!-- 内容结束 -->
		<a id="returnTop" style="bottom: 50px; cursor: pointer; display: none;">回到顶部</a>
		<script src="<c:url value='/h5/js/top.js'></c:url>"></script>
	</div>
</body>
</html>
