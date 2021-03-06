<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>SCORE</title>
</head>
<script src="https://code.jquery.com/jquery-3.5.0.js"></script>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.8.2/css/all.min.css"/>
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@700&display=swap" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="resources/base/base.css?after">
<link href="https://fonts.googleapis.com/css2?family=Lobster&display=swap" rel="stylesheet">
<style>
    .bottom_img{
        font-family: 'Lobster', cursive;
    }
    .bottom_img img{
        margin:0 auto;
        width:50px;
        height:50px;
    }
    .fqa_tbl tr td{
        padding :9px 10px;
        
    }
    .fqa_tbl{
        background-color:white;
        border-spacing: 0;
        border-collapse: collapse;
        text-align:center;
        margin-left:465px;
        margin-top:100px;
    }
    .fqa_tbl tr{
        border-bottom: 1px solid #6E6C6C;
    }
    .fqa_tbl th{
        background-color:#E3DBDB;
        font-size:20px;
        font-family:bold;
    }
    .fqa_tbl_title1{
        height:80px;
        
    }
    .a:hover{
      text-decoration:underline; 
      color:black;
    }
    a:visited{
         color:black;
    }
    a:link{
     color:black;
    }
    </style>

<body>
<div style='position:relative;float:left'>
  <nav class=title_nav>
       <div class=title_logo > <a href="home"><img class='logos' src='resources/logoimg/logo.gif'></a></div>
         <div class=title_search><fieldset><input type="search" class=inp id=search><button type="button" id=ok class=button><i class="fa fa-search"></i></button></fieldset></div>
            <ul class=title_action>
                <li><i class="fas fa-user-circle"></i>&nbsp;${home.member_name}</li>
                <li><i class="fas fa-sign-in-alt"></i>&nbsp;<a href='logout'>????????????</a></li>
                <li><i class="fas fa-shopping-cart">&nbsp;</i><a href='cart'>????????????</a></li>
                <li><i class="fas fa-spa">&nbsp;</i><a href='mypage'>???????????????</a></li>
                <li><i class="far fa-question-circle">&nbsp;</i><a href='fqa_list?pageNum=1'>????????????</a></li>
            </ul>
    </nav>
    <ul class=kate>
        <li><a href='best'>????????????</a></li>
        <li><a href='new'>????????????</a></li>
        <li><a href='products?pageNum=1'>?????????</a>
            <ul class=kate_sub1>
                <li><a href='productssel?pageNum=1&products=?????????'>?????????</a></li>
                <li><a href='productssel?pageNum=1&products=??????'>??????</a></li>
                <li><a href='productssel?pageNum=1&products=?????????'>?????????</a></li>
                <li><a href='productssel?pageNum=1&products=??????'>??????</a></li>
            </ul>
        </li>
        <li><a href='genre?pageNum=1'>?????????</a>
            <ul class=kate_sub1>
                <li><a href='genresel?pageNum=1&genre=??????'>??????</a></li>
                <li><a href='genresel?pageNum=1&genre=Pop'>Pop</a></li>
                <li><a href='genresel?pageNum=1&genre=OST'>OST</a></li>
                <li><a href='genresel?pageNum=1&genre=Ballad'>Ballad</a></li>
            </ul>
        </li>
        <li><a href='notice_list?pageNum=1'>????????????</a></li>
    </ul>
    <nav style='margin-top:150px;margin-bottom:400px;width:1920px'>
        <!-- ????????? ????????? ???????????? ^^-------------------------------------------------------------------------------------------------------- -->
         <!-- ????????? ???????????? TABLE??? ?????? style='margin:0 auto;' ???????????? ?????????????????? -->
         <div style='font-size:0.5em;'>
            <table class="fqa_tbl" id='lists' style='width:1000px; align=center;text-align:center; '>
            <tr style='text-align:left;font-size:2em'>
             <td colspan='3'><p style='margin:0 auto; font-size:2em;text-align:left;'>Notice</p></td>
            <tr>
                <tr class=table_tr>
                <th class='fqa_tbl_title1' align=center style='width:50px'>??????</th>
                <th style='width:100px'>?????????</th>
                <th style='width:1000px'>??????</th>
                </tr>
            <c:forEach items="${notice}" var="dao">
            <tr onmouseover="this.style.background='#EAE6E6';" onmouseout="this.style.background='white';">
                <td style='font-size:2em;align=center'>${dao.rownum}</td>
                <td style='font-size:2em;'>${dao.notice_name}</td>
                <td><a style='color:black; font-size:2em' href="notice_content_view?notice_id=${dao.notice_id}&m_id=${dao.m_id}">${dao.notice_title}</a></td>
            </tr>
            </c:forEach>
            <tr >
                <td style='text-align:right;'></td>
              <td style='align:right;font-size:2em'>
                  <c:if test="${startyes eq 1}">
                  <a  style='color:black;' href="notice_list?pageNum=${back}">[??????]</a>
                  </c:if>
                  <ul class=pageNum>
                  <c:forEach items='${num}' var='n'>
                	<li><a href='notice_list?pageNum=${n}' id="${n}">${n}</a></li>
                </c:forEach>
                </ul>
                <c:if test="${endno eq 1}">
                <a  style='color:black;' href="notice_list?pageNum=${nexts}">[??????]</a>
               </c:if>
               </td>

            </tr>
            </table>
        </div>
        <!-- ????????? ????????? ???????????? ^^-------------------------------------------------------------------------------------------------------- -->
    </nav>
    <nav style='margin:100px 0 0 0;color:grey;position:absolute;bottom:0;background:#10284a;font-size:13px ;float:left;
             display: flex;justify-content: space-between;align-items: center;height:150px;width:1920px;'>
          <div style='margin-left:100px'>
             <ul style='display:flex'>
                <li>???????????? ???????????????[??????]</li>
                <li>   |  ????????? : ?????????</li>
                <li>   |  ???????????? ????????? ????????? ????????????5??? 35 6??? : @??????????????????</li>
             </ul>
             <ul style='display:flex'>
                <li>????????????????????? : 202-04-20916 </li>
                <li>   |   ????????????????????? : ??? 2020-???????????????-0001???</li>
             </ul>
             <ul style='display:flex;margin-top:10px'>
                <li>Copyright(c) WIKIWIKI Inc. All Rights Reserved. Hosting by THREEWAY.</li>
             </ul>
          </div>
          <div>
             <ul style='display:flex;margin-top:10px'>
                <li>???????????? (?????? 9AM ~ 6PM)</li>
               <li>???????????? 010-8590-1142  |  ????????????/????????? ?????? 010-3356-2014</li>
             </ul>
          </div>
          <div class='bottom_img' style='margin-right:100px'>
             <img src='resources/image/bottom1.gif'> <img src='resources/image/bottom2.jpg'> <img src='resources/image/bottom3.jpg'>
          </div>
    </nav>
   </div>
</body>
<script>
    $(document)
    .on('click','#ok',function(){
        $(location).attr('href','search_products?pageNum=1&search='+$('#search').val())
    })

</script>
</html>