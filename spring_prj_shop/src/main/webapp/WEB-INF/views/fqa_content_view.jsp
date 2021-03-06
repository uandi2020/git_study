<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
.tbl{
margin:20px 20px;
}
 .td1{
text-align:center;
border-top:solid 1px;
margin:20px 20px;
 }
 .td2{
text-align:center;
border-top:solid 1px;
 }
 
 
.view_tbl{
font-size:18px;
   margin:0 auto;
   border-collapse:collapse;
   border-spacing: 0;
}
.view_tbl tr{
   border-bottom: 1px solid #6E6C6C;
   
}

.view_tbl tr td{
   padding:20px 20px;
   
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
.view_tbl tr:eq(0){
	background-color:#E3DBDB;

}
</style>

<body>
<div id=toptop style='position:relative;float:left'>
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
    <nav style='margin-top:50px;margin-bottom:300px;width:1920px'>
        <!-- ????????? ????????? ???????????? ^^-------------------------------------------------------------------------------------------------------- -->
         <!-- ????????? ???????????? TABLE??? ?????? style='margin:0 auto; ???????????? ?????????????????? -->
                  <input type="hidden" name="m_id" value="${content_view.m_id}">
                    <input type="hidden" name="fqa_id" id=fqa_id value="${content_view.fqa_id}">
                    <input type="hidden" name="fqa_g" id=fqa_group value="${content_view.fqa_group}">
                      <table class=view_tbl >
                    
                    <tr height=50px style='background-color:#E3DBDB'>
                        <td class>${content_view.rownum}</td><td>${content_view.fqa_date}</td><td>${content_view.fqa_name}</td>
                    </tr>             
                    <tr>
                        <td width=200px>??????</td>
                        <td width=800px colspan=2>${content_view.fqa_title}</td>
                    </tr>
                    
                     <tr>
                        <td>??????</td>
                        <td height=80px colspan=2>${content_view.fqa_content}</td>
                    </tr>             
                    <tr id='da' style='display:none;'>                
                        <td>??????</td>
                        <td height=50px colspan=2>${content_view.fqa_group}</td>    
                    </tr>
                    
                    <tr>
                        <td align='right' colspan="3"><input type='button' class=btn id=modify value='??????'>&nbsp;&nbsp;<input type='button' id=list class=btn value='????????????'>&nbsp;&nbsp;<input type='button' class=btn id='del1' value='??????'></td>
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
$(document).ready(function(){
    if($('#fqa_group').val()!=''){
       $('#da').show();
    }
 })
 .on('click','#modify',function(){
    $(location).attr('href','fqa_modify?fqa_id='+$('#fqa_id').val())
 })
 .on('click','#list',function(){
    $(location).attr('href','fqa_list?pageNum=1')
 })
 .on('click','#del1',function(){
    alert('?????????????????????.')
    $(location).attr('href',"fqa_delete?fqa_id="+$('#fqa_id').val())
 })
 .on('click','#ok',function(){
   $(location).attr('href','search_products?pageNum=1&search='+$('#search').val())
})

</script>
</html>