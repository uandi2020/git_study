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
    margin-left:600px;
    margin-top:100px;
    width:800px
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

.a{
   font-size:1em;
   text-align:left;
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
/* textbox */
.b{
   width:500px;
   height:60px;
   font-size:1.5em;
   background-color:transparent;
   border:0 solid black;
   text-align:center;
}
.txt{
   width:700px;
   height:100px;
   font-size:1em;
    background-color:transparent;
   border:0 solid black;
}
.btn{
   width:100px; 
   height:40px; 
   line-height:40px; 
   border:1px gray solid;;
   margin:15px auto; 
   background-color:white; 
   text-align:center;
   cursor: pointer;
   border-radius: 5px; 
   color:#333; 
   transition:all 0.9s, color 0.3;
   font-weight:bold;
}

.btn:hover{
   color:black;
   box-shadow: 0 80px 0 0 rgba(0,0,0,0.25) inset, 0 -80px 0 0 rgba(0,0,0,0.25) inset;
}

.td1{
   text-align:center;
}
.td2{
   font-size:1.7em;
   height:60px;
   text-align:center;
}
.td3{
   weight:700px;
   height:150px;
   font-size:1.7em;

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
    <nav style='margin-top:100px;margin-bottom:200px;width:1920px'>
        <!-- ????????? ????????? ???????????? ^^-------------------------------------------------------------------------------------------------------- -->
         <!-- ????????? ???????????? TABLE??? ?????? style='margin:0 auto;' ???????????? ?????????????????? -->
         <table class='fqa_tbl' width="500" cellpadding="0" cellspacing="0">
            <input type="hidden" id=m_id name="m_id" value="${modify_view.m_id}">
            <input type="hidden" name="notice_id" id=notice_id value="${modify_view.notice_id}">
            <tr>
               <td colspan='4'><p style='margin:0 auto; font-size:3em;text-align:left;'>Notice</p></td>
           </tr>
            <tr class=table_tr>
                <th class='td1'>??????</td>
                <th style='border-left:solid 1px;border-right:solid 1px;' class='td1'>${modify_view.rownum}</td>
            </tr>
            <tr>
                <td class='td1'>?????????</td>
                <td style='border-left:solid 1px;border-right:solid 1px;' class='td1'>${modify_view.notice_name}</td>
            </tr>
            <tr>
                <td class='td1'>??????</td>
                <td style='border-left:solid 1px; border-right:solid 1px;' class='td1'><input type='text' id='notice_txt' class=td2 value='${modify_view.notice_title}' style='border:none;'></td>
            </tr>
            <tr>
                <td>??????</td>
                <td style='border-left:solid 1px;border-right:solid 1px;' class='txt'>
                <textarea id='notice_content' class=td3 style='width:700px;height:200px; border:none;' value='${modify_view.notice_content}'>${modify_view.notice_content}</textarea>
                </td>
            </tr>
            <tr>
                <td align='right' colspan="2">&nbsp;&nbsp;<input type='button' class=btn id='notice_btn1' value="??????">&nbsp;&nbsp;<input type='button' class=btn id=list value='??????' class='td1'></td>
            </tr>
    </table>
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
    
    .on('click','#notice_btn1',function(){
       $(location).attr('href','notice_mo?title='+$('#notice_txt').val()+'&notice_con='+$('#notice_content').val()+'&notice_id='+$('#notice_id').val())
    })
    
    .on('click','#list',function(){
       $(location).attr('href','notice_content_view?notice_id='+$('#notice_id').val()+'&m_id='+$('#m_id').val());
       
    })
    </script>
</html>