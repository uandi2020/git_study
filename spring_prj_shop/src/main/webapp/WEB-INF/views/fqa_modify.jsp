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
.view_tbl input,textarea{
   font-family:bold;
   font-size:18px;
}

</style>

<body>
<div id=toptop style='position:relative;float:left'>
  <nav class=title_nav>
       <div class=title_logo > <a href="home"><img class='logos' src='resources/logoimg/logo.gif'></a></div>
       <div class=title_search><fieldset><input type="search" class=inp id=search><button type="button" id=ok class=button><i class="fa fa-search"></i></button></fieldset></div>
            <ul class=title_action>
               <li><i class="fas fa-user-circle"></i>&nbsp;${home.member_name}</li>
                <li><i class="fas fa-sign-in-alt"></i>&nbsp;<a href='logout'>๋ก๊ทธ์์</a></li>
                <li><i class="fas fa-shopping-cart">&nbsp;</i><a href='cart'>์ฅ๋ฐ๊ตฌ๋</a></li>
                <li><i class="fas fa-spa">&nbsp;</i><a href='mypage'>๋ง์ดํ์ด์ง</a></li>
                <li><i class="far fa-question-circle">&nbsp;</i><a href='fqa_list?pageNum=1'>๊ณ?๊ฐ๋ฌธ์</a></li>
            </ul>
    </nav>
    <ul class=kate>
        <li><a href='best'>์ธ๊ธฐ์๋ณด</a></li>
        <li><a href='new'>์ต์?์๋ณด</a></li>
        <li><a href='products?pageNum=1'>์๊ธฐ๋ณ</a>
            <ul class=kate_sub1>
                <li><a href='productssel?pageNum=1&products=ํผ์๋ธ'>ํผ์๋ธ</a></li>
                <li><a href='productssel?pageNum=1&products=๊ธฐํ'>๊ธฐํ</a></li>
                <li><a href='productssel?pageNum=1&products=๋ฒ?์ด์ค'>๋ฒ?์ด์ค</a></li>
                <li><a href='productssel?pageNum=1&products=๋๋ผ'>๋๋ผ</a></li>
            </ul>
        </li>
        <li><a href='genre?pageNum=1'>์ฅ๋ฅด๋ณ</a>
            <ul class=kate_sub1>
                <li><a href='genresel?pageNum=1&genre=๊ฐ์'>๊ฐ์</a></li>
                <li><a href='genresel?pageNum=1&genre=Pop'>Pop</a></li>
                <li><a href='genresel?pageNum=1&genre=OST'>OST</a></li>
                <li><a href='genresel?pageNum=1&genre=Ballad'>Ballad</a></li>
            </ul>
        </li>
        <li><a href='notice_list?pageNum=1'>๊ณต์ง์ฌํญ</a></li>
    </ul>
    <nav style='margin-top:100px;margin-bottom:300px;width:1920px'>
        <!-- ํ์ด๋ธ ๋ฃ๋๊ณณ ์์๋ถ๋ถ ^^-------------------------------------------------------------------------------------------------------- -->
         <!-- ์ฌ๊ธฐ์ ๋ฃ์ด์ฃผ๊ณ? TABLE์ธ ๊ฒฝ์ฐ style='margin:0 auto'; ๋ฃ์ด์ฃผ๋ฉด ๊ฐ์ด๋ฐ์?๋?ฌ๋จ -->
                  <table class=view_tbl >
                  <input type="hidden" name="m_id" value="${modify_view.m_id}">
                    <input type="hidden" name="fqa_id" id=fqa_id value="${modify_view.fqa_id}">
                    <tr height=50px style='background-color:#E3DBDB'>
                        <td class>${modify_view.rownum}</td><td>${modify_view.fqa_name}</td>
                    </tr>             
                    <tr>
                        <td width=200px>์?๋ชฉ</td>
                        <td width=800px colspan=2><input type='text' id='fqa_txt' value='${modify_view.fqa_title}' style='width:99%; height:40px; border:none;'></td>
                    </tr>
                    
                     <tr>
                        <td>๋ด์ฉ</td>
                        <td height=80px colspan=2><textarea id='fqa_content' style='width:99%; height:150px; border:none;' value='${modify_view.fqa_content}'>${modify_view.fqa_content}</textarea></td>
                    </tr>             
                      <tr>
                         <td align='right' colspan="2">&nbsp;&nbsp;<input type='button' class=btn id='fqa_btn1' value="์๋ฃ">&nbsp;&nbsp;<input type='button' class=btn id=list value='์ทจ์' class='td1'></td>
                     </tr>
                </table>
        <!-- ํ์ด๋ธ ๋๋๋ ์์๋ถ๋ถ ^^-------------------------------------------------------------------------------------------------------- -->
    </nav>
    <nav style='margin:100px 0 0 0;color:grey;position:absolute;bottom:0;background:#10284a;font-size:13px ;float:left;
             display: flex;justify-content: space-between;align-items: center;height:150px;width:1920px;'>
          <div style='margin-left:100px'>
             <ul style='display:flex'>
                <li>์ฃผ์ํ์ฌ ์น๋ฏธ์์์[์๋ณด]</li>
                <li>   |  ๋ํ์ : ์ด์น๋ฏธ</li>
                <li>   |  ์ถฉ์ฒญ๋จ๋ ์ฒ์์ ์๋ถ๊ตฌ ์ฑ์?๊ณต์5๋ก 35 6์ธต : @ํด๋จผ๊ต์ก์ผํฐ</li>
             </ul>
             <ul style='display:flex'>
                <li>์ฌ์์๋ฑ๋ก๋ฒํธ : 202-04-20916 </li>
                <li>   |   ํต์?ํ๋งค์์?๊ณ? : ์? 2020-์ฒ์์๋ถ๊ตฌ-0001ํธ</li>
             </ul>
             <ul style='display:flex;margin-top:10px'>
                <li>Copyright(c) WIKIWIKI Inc. All Rights Reserved. Hosting by THREEWAY.</li>
             </ul>
          </div>
          <div>
             <ul style='display:flex;margin-top:10px'>
                <li>๊ณ?๊ฐ์ผํฐ (ํ์ผ 9AM ~ 6PM)</li>
               <li>์ผ๋ฐ๋ฌธ์ 010-8590-1142  |  ์ฃผ๋ฌธ์?์/์กฐ์ฎ๊น ๋ฌธ์ 010-3356-2014</li>
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
   
})
.on('click','#list',function(){
   alert('์ทจ์๋์์ต๋๋ค.')
   $(location).attr('href','fqa_list?pageNum=1')
})
.on('click','#fqa_btn1',function(){
   $(location).attr('href','fqa_mo?fqa_id='+$('#fqa_id').val()+"&fqa_con="+$('#fqa_content').val()+"&title="+$('#fqa_txt').val())
})
.on('click','#ok',function(){
   $(location).attr('href','search_products?pageNum=1&search='+$('#search').val())
})
</script>
</html>