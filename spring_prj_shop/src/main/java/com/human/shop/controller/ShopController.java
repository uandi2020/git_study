package com.human.shop.controller;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.human.shop.dao.IDao;
import com.human.shop.dto.loginDto;
import com.human.shop.dto.notice;
import com.human.shop.function.Email;
import com.human.shop.function.Paging;
import com.human.shop.dto.Cart;
import com.human.shop.dto.Heart;
import com.human.shop.dto.Member;
import com.human.shop.dto.Mileage;
import com.human.shop.dto.Newlist;
import com.human.shop.dto.Sales;
import com.human.shop.dto.Score;
import com.human.shop.dto.fqaDto;


@Controller
public class ShopController {
	@Autowired
	private SqlSession sqlSession;
	
	//테스트용
		@RequestMapping(value="/base_html")
		public String base_html(Model model) {
			System.out.println("agree()");
			return "base_html";
		}
	
//메인홈페이지
	@RequestMapping("/homes")
	public String homes(HttpServletRequest request,Model model) {
		System.out.println("homes()");
		HttpSession session=request.getSession();
		//
		if(session.getAttribute("loginuser")!=null) {		
			return "redirect:home";
		}else {
			return "redirect:login";
		}
	}
		
		
	@RequestMapping("/home")
	   public String home(HttpServletRequest request,Model model) {
	      System.out.println("home()");
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      ArrayList<Score> newScore=dao.newScore();
	      ArrayList<Sales> bestScore=dao.bestScore();
	      ArrayList<Sales> gayoBestScore=dao.gayoBestScore();
	      System.out.println(gayoBestScore);
	      
	      if(dto!=null) {
		      System.out.println("dto.getM_id()["+dto.getM_id()+"]");
		      
		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      System.out.println("sideImg["+sideImg.size()+"]");
		      model.addAttribute("home",dto);
		      model.addAttribute("newScore",newScore);
		      model.addAttribute("bestScore",bestScore);
		      model.addAttribute("gayoBestScore",gayoBestScore);
		      model.addAttribute("sideImg",sideImg);
	         return "loginMain";
	      }else {
	    	  if (session.getAttribute("notc")==null) {
	    		  StringBuffer te = new StringBuffer(); //문자열을 추가하거나 변경 할 때 주로 사용하는 타입
	    		  Random rnd = new Random(); //랜덤함수
	    		  for (int i = 0; i < 20; i++) {
	    		      int rIndex = rnd.nextInt(3);
	    		      switch (rIndex) {
	    		      case 0:
	    		          // a-z
	    		          te.append((char) ((int) (rnd.nextInt(26)) + 97));
	    		          break;
	    		      case 1:
	    		          // A-Z
	    		          te.append((char) ((int) (rnd.nextInt(26)) + 65));
	    		          break;
	    		      case 2:
	    		          // 0-9
	    		          te.append((rnd.nextInt(10)));
	    		          break;
	    		      }
	    		  }
	    		  String temp=te.toString(); //String 변환
	  			session.setAttribute("notc",temp); //session에 값저장
	  			System.out.println("notc=["+temp+"]");
	  		}
	         model.addAttribute("newScore",newScore);
	         model.addAttribute("bestScore",bestScore);
	         model.addAttribute("gayoBestScore",gayoBestScore);
	         return "main";
	      }
	      
	   }
	//-------------------------------------------------------------------------------------------//
//로그인페이지
	@RequestMapping(value="/login")
	public String login(Model model) {
		System.out.println("login()");
		return "login";
	}
	@RequestMapping(value="/logins",method=RequestMethod.POST)
	   public @ResponseBody String login(HttpServletRequest request,HttpServletResponse response,Model model) {
	      System.out.println("logins()");  
	      String id=request.getParameter("ids");//아이디
	      String pw=request.getParameter("pwd");//비밀번호
	      HttpSession session=request.getSession();
	      IDao dao=sqlSession.getMapper(IDao.class);
	      loginDto dto=dao.login(id,pw);//DB[select]
	      if(dto==null) {
	    	  //없으면 0을 반환시켜 done부분에 alert 띄움
	         return "0";
	      }else {
	    	 //세션에 아이디를 저장
	         session.setAttribute("loginuser",dto);
	      }
	      Gson gson=new Gson();
	      String log=gson.toJson(dto);
	      return "redirect:homes";
	   }
//로그인 아이디 찾기
	@RequestMapping(value="/login_idFind")
	public String login_idFind(Model model) {
		System.out.println("login_idFind");
		return "login_idFind";
	}

//아이디 찾기
	@RequestMapping(value="/idFind",method=RequestMethod.POST) 
	public @ResponseBody String idFind(HttpServletRequest request,Model model) {
      System.out.println("idFind()");
      String mail1=request.getParameter("emailId");
      String mail2=request.getParameter("emailAdress");
      String mail=mail1+'@'+mail2;
      System.out.println(mail);
      String name=request.getParameter("name");
      System.out.println(name);
      
      IDao dao = sqlSession.getMapper(IDao.class);
      
      Member idFind=dao.idFind(name,mail);
      if(idFind==null) {
         return "0";
      }
      String a=idFind.getM_id();
      return idFind.m_id;
   }
//아이디 정보 화면
   @RequestMapping(value="/idInfo",method=RequestMethod.POST) 
   public  String idInfo(HttpServletRequest request,Model model) {
      System.out.println("idInfo()");
      
      model.addAttribute("id", request.getParameter("hide"));
      return "idInfo";
   }
   
//로그인 비밀번호 찾기
	@RequestMapping(value="/login_pwFind")
	public String login_pwFind(Model model) {
		System.out.println("login_pwFind()");
		return "login_pwFind";
	}
//비밀번호찾기(login_idFind) -> 인증보내기
	@RequestMapping(value="/pwFind",method=RequestMethod.POST)
	public @ResponseBody String pwFind(HttpServletRequest request,Model model) throws AddressException, MessagingException {
		IDao dao=sqlSession.getMapper(IDao.class);
		System.out.println("pwFind()");
		String id=request.getParameter("loginId");
		String name=request.getParameter("loginName");
		String mail1=request.getParameter("loginMail");
		String mail2=request.getParameter("loginMail2");
		String mail=mail1+'@'+mail2; //메일
		
		System.out.println(mail);
		Member member=dao.member_pwSelect(mail,id,name);
		if(member==null) {
			return "0";
		}
		model.addAttribute("member",member);
		//비밀번호찾아주기
		String pass="당신의 비밀번호는 "+member.member_pw+" 입니다.";
	    Email emails = new Email();
	    emails.EmailSend(mail,pass);
		return "pwInfo";
	}
//로그인 비밀번호 찾기
	@RequestMapping(value="/pwInfo",method=RequestMethod.POST)
	public String pwInfo(HttpServletRequest request,Model model) {
		System.out.println("pwInfo()");
		IDao dao=sqlSession.getMapper(IDao.class);
		String id=request.getParameter("loginId");
		String name=request.getParameter("loginName");
		String mail1=request.getParameter("loginMail");
		String mail2=request.getParameter("loginMail2");
		String mail=mail1+'@'+mail2; //메일
		model.addAttribute("member",dao.member_pwSelect(mail,id,name));
		return "pwInfo";
	}
//-------------------------------------------------------------------------------------------//	
//로그아웃
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,Model model) {
		System.out.println("logout()");
		HttpSession session=request.getSession();
		session.removeAttribute("loginuser");
//		session.invalidate();
		return "redirect:home";
		
	}
		
	//-------------------------------------------------------------------------------------------//		
	   //최신악보
    @RequestMapping(value="/new")
    public String newnew(HttpServletRequest request,Model model) {
    System.out.println("new()");
    IDao dao=sqlSession.getMapper(IDao.class);
    ArrayList<Score> newScore=dao.newnew();
     model.addAttribute("newScore",newScore);
     HttpSession session=request.getSession();
     loginDto dto=(loginDto)session.getAttribute("loginuser");
       if(session.getAttribute("loginuser")!=null) {
          loginDto dto2=(loginDto) session.getAttribute("loginuser");
          model.addAttribute("home",dto);
          ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
          model.addAttribute("sideImg",sideImg);

          return "new_login";
       }else {
          return "new";
       }
    }
    //베스트악보
    @RequestMapping(value="/best")
    public String best(HttpServletRequest request,Model model) {
    System.out.println("best()");
    IDao dao=sqlSession.getMapper(IDao.class);
     ArrayList<Sales> bestScore=dao.bestbest();
     model.addAttribute("bestScore",bestScore);
     HttpSession session=request.getSession();
     loginDto dto=(loginDto)session.getAttribute("loginuser");
    if(session.getAttribute("loginuser")!=null) {
       loginDto dto2=(loginDto) session.getAttribute("loginuser");
       model.addAttribute("home",dto);
       ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
       model.addAttribute("sideImg",sideImg);

       return "best_login";
    }else {
       return "best";
    }
 }

//-------------------------------------------------------------------------------------------//	
//회원가입 전 동의하기
	@RequestMapping(value="/agree")
	public String agree(Model model) {
		System.out.println("agree()");
		return "agree";
	}
//회원가입
	@RequestMapping(value="/join")
	public String join(Model model) {
		System.out.println("join()");
		return "join";
	}
//회원가입 후 축하
	@RequestMapping(value="/join_welcome",method=RequestMethod.POST)
	public String join_welcome(HttpServletRequest request,Model model) {
		System.out.println("join_welcome()");
		model.addAttribute("memberName",request.getParameter("joinName"));
		return "join_welcome";
	}
//아이디중복체크
   @RequestMapping(value="/join_idchk",method=RequestMethod.POST)
   public @ResponseBody String join_idchk(HttpServletRequest request,Model model) {
      String id=request.getParameter("id");
      IDao dao=sqlSession.getMapper(IDao.class);
      Member member=dao.join_idchk(id);
      if(member==null) {
         return "0";
      }
      System.out.println("member["+member.m_id+"]");
      Gson gson=new Gson(); //json type으로 반환
      String member_id=gson.toJson(member.m_id);
      return member_id;//done에 return값이 들어감
      }
	   
//이메일 중복체크
   @RequestMapping(value="/join_emailchk",method=RequestMethod.POST)
   public @ResponseBody String join_emailchk(HttpServletRequest request,Model model) {
      String mail=request.getParameter("email");
      IDao dao=sqlSession.getMapper(IDao.class);
      Member member=dao.join_emailchk(mail);    
      if(member==null) {
         return "0";
      }
      Gson gson=new Gson(); 
      String member_mail=gson.toJson(member.member_mail);
      return member_mail;
   }

//장바구니
	@RequestMapping(value="/cart")
	public String cart(HttpServletRequest request,Model model) {
		System.out.println("cart()");
		HttpSession session=request.getSession();
		if(session.getAttribute("loginuser")!=null) {
			loginDto dto2=(loginDto) session.getAttribute("loginuser");
			IDao dao=sqlSession.getMapper(IDao.class);
			ArrayList<Cart> cart = dao.cart_online(dto2.getM_id());
			model.addAttribute("cart",cart);
			model.addAttribute("home",dto2);
			
			ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());

			model.addAttribute("sideImg",sideImg);
			return "cart_online";
		}else {
			String temp=(String) session.getAttribute("notc");
			IDao dao=sqlSession.getMapper(IDao.class);
			ArrayList<Cart> cart2 = dao.cart(temp);
			model.addAttribute("cart2",cart2);
			return "cart";
		}
		
	}
//장바구니에 단독상품넣을때 insert여러번 안되게
	@RequestMapping(value="/cart_move")
	public String cart_move(HttpServletRequest request,Model model) {
		System.out.println("cart_move()");
		HttpSession session=request.getSession();
			loginDto dto2=(loginDto) session.getAttribute("loginuser");
			IDao dao=sqlSession.getMapper(IDao.class);
			String id=request.getParameter("s_id");
			String count=request.getParameter("count");
			String p=request.getParameter("price");
			String bool=request.getParameter("bool");
			System.out.println("bool["+bool+"]");
			int cnt=Integer.parseInt(count);
			System.out.println(p);
			int price=Integer.parseInt(p);
			int sid=Integer.parseInt(id);
			int total=cnt * price;
			Score score=dao.product_confirm(sid);
			dao.product_cart(dto2.getM_id(),sid,score.getScore_name(),score.getScore_parts(),score.getScore_genre(),cnt,price,total);
			
			if(bool.equals("b")){
				System.out.println("안녕하세요");
				return "redirect:home";
			} else  {
				return "redirect:cart";
			}	
	}
	@RequestMapping(value="/cart_move2")
	public String cart_move2(HttpServletRequest request,Model model) {
		System.out.println("cart_move2()");
		IDao dao=sqlSession.getMapper(IDao.class);
		HttpSession session=request.getSession();
		String temp=(String) session.getAttribute("notc");
			
			String id=request.getParameter("s_id");
			String count=request.getParameter("count");
			String p=request.getParameter("price");
			String bool=request.getParameter("bool");
			int cnt=Integer.parseInt(count);
			int price=Integer.parseInt(p);
			int sid=Integer.parseInt(id);
			int total=cnt * price;
			Score score=dao.product_confirm(sid);
			dao.product_cart(temp,sid,score.getScore_name(),score.getScore_parts(),score.getScore_genre(),cnt,price,total);
			
			if(bool.equals("b")){
				System.out.println("안녕하세요");
				return "redirect:home";
			} else  {
				return "redirect:cart";
			}	
	}
//장바구니 선택삭제	
	@RequestMapping(value="/cart_delete")
	public String cart_Delete(HttpServletRequest request,Model model) {
		System.out.println("cart_delete()");
		String del=request.getParameter("delete");
		String[] ad=del.split(",");
		String delete = request.getParameter("delete");
		HttpSession session=request.getSession();
		IDao dao=sqlSession.getMapper(IDao.class);
		for(int i=0;i<ad.length;i++) {
			dao.cart_delete(ad[i]);
		}
		
			return "redirect:cart";	
	}

//장바구니모두비우기
	@RequestMapping(value="/cart_empty")
	public String cart_empty(HttpServletRequest request,Model model) {
		System.out.println("cart_empty()");
		HttpSession session=request.getSession();
		loginDto dto2=(loginDto) session.getAttribute("loginuser");
		IDao dao=sqlSession.getMapper(IDao.class);
		dao.cart_alldelete(dto2.getM_id());
		return "redirect:cart";
	}
	@RequestMapping(value="/cart_empty2")
	   public String cart_empty2(HttpServletRequest request,Model model) {
	      System.out.println("cart_empty2()");
	      HttpSession session=request.getSession();
	      String temp=(String) session.getAttribute("notc");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      dao.cart_alldelete(temp);
	      return "redirect:cart";
	   }
	

//-------------------------------------------------------------------------------------------//	
//주문결제창
	@RequestMapping(value="/pay",produces="text/plain;charset=UTF-8")
	public String pay(HttpServletRequest request,Model model) {
		System.out.println("pay()");
		String cart_id=request.getParameter("cart_id");
		HttpSession session=request.getSession();
		if(session.getAttribute("loginuser")!=null) {
			loginDto dto2=(loginDto) session.getAttribute("loginuser");
			model.addAttribute("home",dto2);
			model.addAttribute("ci",cart_id);
			IDao dao=sqlSession.getMapper(IDao.class);
			Member member=dao.order_Info(dto2.getM_id());
			model.addAttribute("order",member);
			String[] ad=cart_id.split(",");
			System.out.println(ad[0]);
			ArrayList<Cart> car = new ArrayList<Cart>();
			for(int i=0;i<ad.length;i++) {
				int a=Integer.parseInt(ad[i]);
				Cart tmp=dao.cart3(a);
				car.add(tmp);
			}
			model.addAttribute("cart3",car);
			model.addAttribute("mile",dao.mileage_basemile(dto2.getM_id()));
			ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
		       model.addAttribute("sideImg",sideImg);

			return "pay";
		}else {
			model.addAttribute("ci",cart_id);
			IDao dao=sqlSession.getMapper(IDao.class);
			String[] ad=cart_id.split(",");
			System.out.println(ad[0]);
			ArrayList<Cart> car = new ArrayList<Cart>();
			for(int i=0;i<ad.length;i++) {
				int a=Integer.parseInt(ad[i]);
				Cart tmp=dao.cart3(a);
				car.add(tmp);
			}
			model.addAttribute("cart3",car);
			return "pay2";
		}
}
	
	//주문결제 완료
	   @RequestMapping(value="/pay_complete",produces="text/plain;charset=UTF-8")
	   public String pay_complete(HttpServletRequest request,Model model) {
	      System.out.println("pay_complete()");
	      String del = request.getParameter("cart_id");//카트 아이디
	      String addr = request.getParameter("addr");//주소 배송지
	      String point = request.getParameter("point"); //적립금
	      HttpSession session=request.getSession();
	      loginDto dto2=(loginDto) session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      String[] ad=del.split(",");
	      for(int i=0;i<ad.length;i++) {
	         Cart ca=dao.carts(ad[i]);
	         System.out.println();
	         dao.sales_insert(ca.getM_id(),ca.getS_id(),ca.getCart_name(),ca.getCart_parts(),ca.getCart_genre(),ca.getCart_total(),addr);//sales에 넣
	      }
	      for(int j=0;j<ad.length;j++) {
	         dao.cart_delete(ad[j]);
	      }
	      
	       dao.mileage_sales_insert(dto2.getM_id(),point);
	       
	       String basemile =request.getParameter("basemile");
	       String usingmile = request.getParameter("usingmile"); 
	       int a= Integer.parseInt(basemile);//기존 마일리지
	       int b= Integer.parseInt(point);//적립될 마일리지
	       int c= Integer.parseInt(usingmile);//기존마일리지에서 사용할 마일리지
	      
	       
	       int mymileage = a + b;//최종 내 마일리지
	       
	       dao.mileage_update(mymileage,dto2.getM_id());
	      
	       ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	       model.addAttribute("sideImg",sideImg);
	         return "pay_complete";

	}
	   
	@RequestMapping(value="/pay_complete3",produces="text/plain;charset=UTF-8")
	public String pay_complete3(HttpServletRequest request,Model model) {
		System.out.println("pay_complete3()");
		String del = request.getParameter("cart_id");
		System.out.println(del);
		String addr = request.getParameter("addr");
		System.out.println(addr);
		IDao dao=sqlSession.getMapper(IDao.class);
		String[] ad=del.split(",");
		for(int i=0;i<ad.length;i++) {
			Cart ca=dao.carts(ad[i]);
			System.out.println();
			dao.sales_insert("",ca.getS_id(),ca.getCart_name(),ca.getCart_parts(),ca.getCart_genre(),ca.getCart_total(),addr);
		}
		for(int j=0;j<ad.length;j++) {
			dao.cart_delete(ad[j]);
		}
			return "pay_complete2";


}
//-------------------------------------------------------------------------------------------//
	//마이페이지 결제내역(기본)
	   @RequestMapping(value="/mypage" )
	   public String mypage_payHistory(HttpServletRequest request,Model model) {
	      System.out.println("mypage_payHistory()");
	      HttpSession session=request.getSession();
	      if(session.getAttribute("loginuser")!=null) {
	         loginDto dto2=(loginDto) session.getAttribute("loginuser");
	         model.addAttribute("home",dto2);
	         IDao dao=sqlSession.getMapper(IDao.class);
	         ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	         model.addAttribute("sideImg",sideImg);

	         return "mypage";
	      }else {
	         return "redirect:login";
	      }
	      
	}
	   
	//마이페이지 날짜별로 조회   
	   @RequestMapping(value="/mypage1" ,method= RequestMethod.POST , produces = "application/text; charset=utf8")
	   public @ResponseBody String mypage(HttpServletRequest request,Model model) {
	      System.out.println("mypage_payHistory()");
	      String date1= request.getParameter("date1");
	      String date2= request.getParameter("date2");
	      String a=date1.replace("-", "");
	      String b=date2.replace("-", "");
	      System.out.println(a);
	      HttpSession session=request.getSession();
	      
	      loginDto dto2=(loginDto) session.getAttribute("loginuser");
	      model.addAttribute("home",dto2);
	      IDao dao=sqlSession.getMapper(IDao.class);
	      ArrayList<Sales> sales=dao.mypage_select(dto2.getM_id(),a,b);
	 
	      Gson gson=new Gson();
	      String log=gson.toJson(sales);

	      return log;

	      
	}
	   //마이페이지 마일리지
	      @RequestMapping(value="/mypage_mileage")
	      public String mypage_mileage(HttpServletRequest request,Model model) {
	         System.out.println("mypage_mileage()");
	         HttpSession session=request.getSession();
	         IDao dao=sqlSession.getMapper(IDao.class);
	         if(session.getAttribute("loginuser")!=null) {
	            loginDto dto2=(loginDto) session.getAttribute("loginuser");
	            model.addAttribute("home",dto2);
	            model.addAttribute("mileage",dao.mypage_mileage_select_all(dto2.getM_id()));
	            model.addAttribute("basemile",dao.mypage_mileage_select_base(dto2.getM_id()));
	            
	            
	            int ab=dao.mypage_mileage_select_base(dto2.getM_id());
	            Mileage cd=dao.mypage_mileage_it(dto2.getM_id());
	            int paid =ab-cd.mileage;
	            
	            model.addAttribute("haha",paid);
	            ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	            model.addAttribute("sideImg",sideImg);
	            
	            return "mypage_mileage";
	         }else {
	            return "redirect:login";
	         }
	   }
	   
	//마이페이지 내정보
	   @RequestMapping(value="/mypage_myInfo")
	   public String mypage_myInfo(HttpServletRequest request,Model model) {
	      System.out.println("mypage_myInfo()");
	      HttpSession session=request.getSession();
	      if(session.getAttribute("loginuser")!=null) {
	         loginDto dto2=(loginDto) session.getAttribute("loginuser");
	         model.addAttribute("home",dto2);
	         IDao dao=sqlSession.getMapper(IDao.class);
	         ArrayList<Member> member=dao.mypage_info(dto2.getM_id());
	         model.addAttribute("member",member);
	         ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	         model.addAttribute("sideImg",sideImg);
	         
	         return "mypage_myInfo";
	      }else {
	         return "redirect:login";
	      }
	}
	//마이페이지 이전 비밀번호 확인방법
	   @RequestMapping(value="/mypage_pwconfirm",method= RequestMethod.POST )
	   public @ResponseBody String mypage_pwconfirm(HttpServletRequest request,Model model) {
	      System.out.println("mypage_pwconfirm()");
	      HttpSession session=request.getSession();   
	      String pw =request.getParameter("member_pw");
	      String id =request.getParameter("member_id");
	      System.out.println("pw["+pw+"]");
	      System.out.println("id["+id+"]");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      Member memberpw=dao.mypage_pwconfirm(pw,id);
	      
	      
	      System.out.println("member["+memberpw+"]");
	      if(memberpw==null) {
	         return "0";
	      } else {
	         return "1";
	      }
	      
	}
	//내정보수정   
	   @RequestMapping(value="/mypage_update",method= RequestMethod.POST )
	   public @ResponseBody String mypage_update(HttpServletRequest request,Model model) {
	      System.out.println("mypage_mileage()");
	      HttpSession session=request.getSession();
	      String id =request.getParameter("m_id");
	      String pw =request.getParameter("member_pw");
	      String mobile=request.getParameter("mobile");
	      String adress=request.getParameter("adress");
	      System.out.println("adresa["+adress+"]");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      dao.mypage_update(id,pw,mobile,adress);
	      
	      if(session.getAttribute("loginuser")!=null) {
	         loginDto dto2=(loginDto) session.getAttribute("loginuser");
	         model.addAttribute("home",dto2);
	         ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	         model.addAttribute("sideImg",sideImg);
	         
	         return "mypage_mileage";
	      }else {
	         return "redirect:login";
	      }
	}
	   
	   
	   //마이페이지 찜한내역 불러오기
	      @RequestMapping(value="/mypage_heart" )
	      public String mypage_heart(HttpServletRequest request,Model model) {
	         System.out.println("mypage_heart()");
	         HttpSession session=request.getSession();
	         if(session.getAttribute("loginuser")!=null) {
	            loginDto dto2=(loginDto) session.getAttribute("loginuser");
	            model.addAttribute("home",dto2);
	            IDao dao=sqlSession.getMapper(IDao.class);
	            
	            ArrayList<Heart> mypage_heart = dao.mypage_heart(dto2.getM_id());
	            model.addAttribute("heart", mypage_heart);
	            ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	            model.addAttribute("sideImg",sideImg);
	            return "mypage_heart";
	         }else {
	            return "redirect:login";
	         }
	         
	   }
	      
	   //찜한내역 삭제
	      @RequestMapping(value="/heart_delete")
	      public String heart_delete(HttpServletRequest request,Model model) {
	         System.out.println("heart_delete()");
	         String del=request.getParameter("delete");
	         String[] ad=del.split(",");
	         String delete = request.getParameter("delete");
	         HttpSession session=request.getSession();
	         IDao dao=sqlSession.getMapper(IDao.class);
	         for(int i=0;i<ad.length;i++) {
	            dao.heart_delete(ad[i]);
	         }
	         
	            return "redirect:mypage_heart";   
	      }
	    //찜한내역모두비우기
	      @RequestMapping(value="/heart_empty")
	      public String heart_empty(HttpServletRequest request,Model model) {
	         System.out.println("heart_empty()");
	         HttpSession session=request.getSession();
	         loginDto dto2=(loginDto) session.getAttribute("loginuser");
	         IDao dao=sqlSession.getMapper(IDao.class);
	         dao.heart_alldelete(dto2.getM_id());
	         
	         return "redirect:mypage_heart";
	      }
	    

//-------------------------------------------------------------------------------------------//		
//상품 페이지
	@RequestMapping("/product")
	public String product(HttpServletRequest request,Model model) {
	   System.out.println("product()");
	   HttpSession session=request.getSession();
	   String s_id=request.getParameter("s_id");
	   System.out.println("s_id"+s_id);
	   IDao dao=sqlSession.getMapper(IDao.class);
	   if(session.getAttribute("loginuser")!=null) {
	      loginDto dto2=(loginDto) session.getAttribute("loginuser");
	      model.addAttribute("home",dto2);
	      model.addAttribute("score",dao.product_select(s_id));
	      model.addAttribute("s_id",s_id);
	      
	      //찜되어있나 확인
	      String mid = dto2.getM_id();
	      int sid = Integer.parseInt(s_id);
	      System.out.println("s_id["+sid+"] m_id["+mid+"]");
	      Heart product_heart = dao.product_heart(mid,sid);
	      model.addAttribute("heart",product_heart);
	      
	      //최신클릭한거
	      ArrayList<Newlist> newlist = dao.newlist_compare(dto2.getM_id(),s_id);
	      if(newlist.size()==0) {
	    	  //테이블의 첫번째 레코드 고유아이디 가져오기
	    	  ArrayList<String> rowid = dao.newlist_rowid(dto2.getM_id());
	    	  //안겹치면 첫번째 레코드 삭제
	    	  dao.newlist_delete(dto2.getM_id(),rowid.get(0));
	      } else {
	    	  //겹치면삭제
	    	  dao.newlist_delete2(dto2.getM_id(),s_id);
	      }
	      //추가
	      dao.newlist_insert(dto2.getM_id(),s_id); 
	      //최신목록 출력
	      ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
	      model.addAttribute("sideImg",sideImg);
	      
          model.addAttribute("vedio",dao.product_vedio(s_id));
	      return "product_login";
	   }else {
		  model.addAttribute("score",dao.product_select(s_id));
		  model.addAttribute("s_id",s_id);
		  model.addAttribute("vedio",dao.product_vedio(s_id));
	      return "product";
	   }
	}

	//단독상품 주문페이지
    @RequestMapping(value="/pay_product" ,produces="text/plain;charset=UTF-8")
    public String pay_product(HttpServletRequest request,Model model) {
       System.out.println("pay_product()");
       HttpSession session=request.getSession();
       if(session.getAttribute("loginuser")!=null) {
          loginDto dto2=(loginDto) session.getAttribute("loginuser");
          model.addAttribute("home",dto2);
          IDao dao=sqlSession.getMapper(IDao.class);
          String sid = request.getParameter("s_id");
          int s_id = Integer.parseInt(sid);
          String cnt = request.getParameter("cnt");
          int count = Integer.parseInt(cnt);
          model.addAttribute("s_id",s_id);
          Score pay_product = dao.pay_product(s_id);
          int total = count*pay_product.getScore_price();
          model.addAttribute("pay",pay_product);
          model.addAttribute("cnt",count);
          model.addAttribute("total",total);
          Member member=dao.order_Info(dto2.getM_id());
          model.addAttribute("order",member);
          model.addAttribute("mile",dao.mileage_basemile(dto2.getM_id()));
          ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
          model.addAttribute("sideImg",sideImg);
          return "pay_product";
       }else {
           IDao dao=sqlSession.getMapper(IDao.class);
           String sid = request.getParameter("s_id");
           System.out.println(sid);
           int s_id = Integer.parseInt(sid);
           String cnt = request.getParameter("cnt");
           int count = Integer.parseInt(cnt);
           model.addAttribute("s_id",s_id);
           Score pay_product = dao.pay_product(s_id);
           int total = count*pay_product.getScore_price();
           model.addAttribute("pay",pay_product);
           model.addAttribute("cnt",count);
           model.addAttribute("total",total);
          return "pay_product2";
       }
       
 }
    
    @RequestMapping(value="/pay_complete2",method = RequestMethod.GET ,produces="text/plain;charset=UTF-8")
    public String pay_complete2(HttpServletRequest request,Model model) {
       System.out.println("pay_complete2()");
       String del = request.getParameter("s_id"); //상품번호
       String count = request.getParameter("total"); //갯수
       String point = request.getParameter("point"); //적립금
       int total = Integer.parseInt(count); //
       int dels = Integer.parseInt(del); //상품번호 숫자로 변환
       String addr = request.getParameter("addr"); //주소(스크립트에서 ','로 있는 더함)
       System.out.println(addr);
       HttpSession session=request.getSession();
       loginDto dto2=(loginDto) session.getAttribute("loginuser");
       IDao dao=sqlSession.getMapper(IDao.class);
       Score pay = dao.pay_product(dels);
       dao.sales_insert(dto2.getM_id(),dels,pay.getScore_name(),pay.getScore_parts(),pay.getScore_genre(),total,addr);
       
       dao.mileage_sales_insert(dto2.getM_id(),point);
       
       String basemile =request.getParameter("basemile");
       String usingmile = request.getParameter("usingmile"); 
       int a= Integer.parseInt(basemile);//기존 마일리지
       int b= Integer.parseInt(point);//적립될 마일리지

      
       
       int mymileage = a + b;//최종 내 마일리지
       
       dao.mileage_update(mymileage,dto2.getM_id());
       ArrayList<Newlist> sideImg=dao.sideImg(dto2.getM_id());
       model.addAttribute("sideImg",sideImg);
           
          return "pay_complete";    
 }   	
    
    @RequestMapping(value="/pay_complete4",produces="text/plain;charset=UTF-8")
    public String pay_complete4(HttpServletRequest request,Model model) {
       System.out.println("pay_complete4()");
       String del = request.getParameter("s_id");
       String count = request.getParameter("total");
       int total = Integer.parseInt(count);
       int dels = Integer.parseInt(del);
       String addr = request.getParameter("addr");
       System.out.println(addr);
       IDao dao=sqlSession.getMapper(IDao.class);
       Score pay = dao.pay_product(dels);
       String ad="";
       dao.sales_insert(ad,dels,pay.getScore_name(),pay.getScore_parts(),pay.getScore_genre(),total,addr);
       
          return "pay_complete2";
       
 }	
	//찜하기
    @RequestMapping(value="/heart_insert",method= RequestMethod.POST )
    public @ResponseBody String heart_insert(HttpServletRequest request,Model model) {
       System.out.println("heart_insert()");
       HttpSession session=request.getSession();
       if(session.getAttribute("loginuser")!=null) {
          loginDto dto2=(loginDto) session.getAttribute("loginuser");
          model.addAttribute("home",dto2);
          IDao dao=sqlSession.getMapper(IDao.class);
          String sid = request.getParameter("s_id");
          int s_id = Integer.parseInt(sid);
          String m_id = request.getParameter("m_id");
          String s_name = request.getParameter("s_name");
          System.out.println("s_id["+s_id+"] m_id["+m_id+"] s_name ["+s_name+"]");
          dao.heart_insert(m_id,s_id,s_name);
          return "redirect:product?s_id="+sid;
       }else {
    	   return "reditrct:login";
       }
     
 }
    //찜하기 취소
    @RequestMapping(value="/heart_cancel",method= RequestMethod.POST)
    public @ResponseBody String heart_cancel(HttpServletRequest request,Model model) {
       System.out.println("heart_cancel()");
       HttpSession session=request.getSession();
       if(session.getAttribute("loginuser")!=null) {
          loginDto dto2=(loginDto) session.getAttribute("loginuser");
          model.addAttribute("home",dto2);
          IDao dao=sqlSession.getMapper(IDao.class);
          String sid = request.getParameter("s_id");
          int s_id = Integer.parseInt(sid);
          String m_id = request.getParameter("m_id");
          
          System.out.println("s_id["+s_id+"] m_id["+m_id+"]");
          dao.heart_cancel(m_id,s_id);
          return "redirect:product?s_id="+sid;
       }else {
          return "reditrct:login";
       }
 }
//-------------------------------------------------------------------------------------------//		
	

//회원가입 (join) -> 가입시 - DB[insert]
	@RequestMapping(value="/member_insert",method=RequestMethod.POST)
	public @ResponseBody void member_insert(HttpServletRequest hsr,Model model) {
	   System.out.println("member_insert");
	   IDao dao=sqlSession.getMapper(IDao.class);
	   String name=hsr.getParameter("name");
	   String gene=hsr.getParameter("gene");
	   String birth=hsr.getParameter("birth");
	   String id=hsr.getParameter("id");
	   String pw=hsr.getParameter("pw");
	   String mail=hsr.getParameter("mail");
	   String mobile=hsr.getParameter("mobile");
	   String adress=hsr.getParameter("adress");
	   int mileage=1000;
	   for(int i=916;i<923;i++) {
	   dao.side_join_insert(id,i);
	   }
	   dao.member_insert(id,pw,name,gene,birth,mail,mobile,adress);
	   dao.mileage_insert(id,mileage);
	   dao.sales_mileage_insert(id,mileage);
	}
	
	
//회원가입(join) -> 메일보내기(인증번호)
		@RequestMapping(value = "/mailSender",method = RequestMethod.POST)
		public @ResponseBody void mailSender(HttpServletRequest request,HttpServletResponse response,ModelMap mo) throws AddressException,MessagingException{
		   String email=request.getParameter("email");
		   String pass=request.getParameter("pass");
		   pass="인증번호는 "+pass+" 입니다.";
		   Email emails = new Email();
		   emails.EmailSend(email,pass);
		}
//fqa고객센터----------------------------------------------------------------------------------------------------------------------------------------------
	   
		
		@RequestMapping("/fqa_delete")
		public String fqa_delete(HttpServletRequest request,Model model) {
			System.out.println("fqa_delete()");
			HttpSession session=request.getSession();
			IDao dao=sqlSession.getMapper(IDao.class);
			dao.fqa_delete(request.getParameter("fqa_id"));
			
			return "redirect:fqa_list?pageNum=1";
		}
		
		
		@RequestMapping("/fqa_write_view")
		public String write_view(HttpServletRequest request,Model model) {
			System.out.println("write_view()");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			
			if(session.getAttribute("loginuser")!=null) {
			IDao dao=sqlSession.getMapper(IDao.class);
			loginDto dto2 = dao.fqa_name_write(dto.getM_id());
			model.addAttribute("home",dto2);
			return "fqa_write_view";
			}else {
				return "redirect:login";
			}
		}
		
		
		@RequestMapping(value="/fqa_write",method=RequestMethod.POST)
		public String write(HttpServletRequest request,Model model) {
			System.out.println("write()");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			IDao dao=sqlSession.getMapper(IDao.class);
			
			String m_id=dto.getM_id();
			String title=request.getParameter("fqa_title");
			String name=request.getParameter("fqa_name");
			String sel=request.getParameter("ca");
			System.out.println(sel);
			String con=request.getParameter("content");
			
			dao.fqa_insert(m_id,name,title,sel,con);
			
			
			return"redirect:fqa_list?pageNum=1";
		}
		
		
		@RequestMapping("/fqa_content")
		public String content(HttpServletRequest request,Model model) {
			System.out.println("content()");
			HttpSession session=request.getSession();
			if(session.getAttribute("loginuser")!=null) {
				return "fqa_content_view";
			}else {
				return "redirect:login";
			}
		}
		
		
		@RequestMapping(value="/fqa_content_view",produces="text/plain;charset=UTF-8")
		public String content_view(HttpServletRequest request,Model model) {
			System.out.println("content_view()");
			String id=request.getParameter("m_id");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			String mid=dto.getM_id();
			IDao dao=sqlSession.getMapper(IDao.class);
			model.addAttribute("content_view",dao.fqacontent(request.getParameter("fqa_id")));
			model.addAttribute("m_id",mid);
			model.addAttribute("home",dto);
			if("seungmi".equals(dto.getM_id())) {
				return "fqa_content_view3";
			}else if(!id.equals(dto.getM_id())) {
				ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
			       model.addAttribute("sideImg",sideImg);
					return "fqa_content_view2";
			}else if(session.getAttribute("loginuser")==null){
					return "redirect:login";
			}else{
					return "fqa_content_view";
			}
		}
		
		
		
		@RequestMapping("/fqa_update")
		public String fqa_update(HttpServletRequest request,Model model) {
			System.out.println("fqa_update()");
			String fqa_id=request.getParameter("fqa_id");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			
			IDao dao=sqlSession.getMapper(IDao.class);
			dao.fqa_group_update(request.getParameter("group"),fqa_id);
			return"redirect:fqa_list?pageNum=1";
		}
		
		
		@RequestMapping("/fqa_modify")
		public String fqa_modify(HttpServletRequest request,Model model) {
			System.out.println("fqa_modify()");
			HttpSession session=request.getSession();
			if(session.getAttribute("loginuser")!=null) {
				IDao dao=sqlSession.getMapper(IDao.class);
				model.addAttribute("modify_view",dao.fqacontent(request.getParameter("fqa_id")));
				
				return"/fqa_modify";
			}else {
				return "redirect:login";
			}
			
		}
		
		
		@RequestMapping("/fqa_mo")
		public String fqa_mo(HttpServletRequest request,Model model) {
			System.out.println("fqa_modify()");
			String fqa_id=request.getParameter("fqa_id");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			
			IDao dao=sqlSession.getMapper(IDao.class);
			dao.fqa_update(request.getParameter("title"),request.getParameter("fqa_con"),fqa_id);
			return"redirect:fqa_content_view?fqa_id="+fqa_id+"&m_id="+dto.getM_id();
		}
//---------------------------------------------------------------------------------------------------
//페이징 fqalist
		@RequestMapping("/fqa_list")
		public String list(HttpServletRequest request,Model model) {
			System.out.println("fqa_list()");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			if(session.getAttribute("loginuser")!=null) {
			IDao dao=sqlSession.getMapper(IDao.class);
			ArrayList<fqaDto> list=dao.list();
			model.addAttribute("list",list);
			//페이징
			String st=request.getParameter("pageNum");
			Paging paging = new Paging();
			paging.page(dao.fqa_total(),st,model);	
			ArrayList<fqaDto> fqa=dao.fqa_page(paging.endRow,paging.startRow);
			
			model.addAttribute("fqa",fqa);
			model.addAttribute("home",dto);
			return "fqa_list";
		}else {
			return "redirect:login";
		}
		}
		
//상품페이지
		//-----------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/products")
	   public String products(HttpServletRequest request,Model model) {
	      System.out.println("products()");
	      
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      
	    if(session.getAttribute("loginuser")!=null) {
		      //페이징하기
			  String st=request.getParameter("pageNum"); //몇번째 페이지인지
			  Paging paging = new Paging(); //Paging객체를 인스턴스
			  paging.page(dao.score_total(),st,model); //Paging 메소드 호출
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow); //한페이지당 보여줄 데이터 조회
		      model.addAttribute("score",scores);
		      
		      //사용자 이름
		      model.addAttribute("home",dto);
		      //최신목록
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      //최근 복 목록
		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      model.addAttribute("sideImg",sideImg);
		      
		      return "loginproducts";
	   }else {
			  System.out.println("페이징함수");
			  //페이징하기
			  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.score_total(),st,model);
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow);
		      model.addAttribute("score",scores);
		      
		      //최신목록
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      
		      return "products";
	   }
	}
		
//productssel
//악기별  카테고리 --------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/productssel")
	public String productssel(HttpServletRequest request,Model model) {
	   System.out.println("productssel()");
	   HttpSession session=request.getSession();
	   loginDto dto=(loginDto)session.getAttribute("loginuser");
	   IDao dao=sqlSession.getMapper(IDao.class);
	   String products=request.getParameter("products");
	   model.addAttribute("pros",products);
	   if(session.getAttribute("loginuser")!=null) {
		 //페이징하기
		  String st=request.getParameter("pageNum");
		  Paging paging = new Paging();
		  paging.page(dao.parts_total(products),st,model);
		  ArrayList<Score> scores =dao.parts(paging.endRow,paging.startRow,products);
		  model.addAttribute("score",scores);
		  //사용자이름
		  model.addAttribute("home",dto);
		  //최신목록
		  ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	      
	      return "loginproductssel";
	}else {
		 //페이징하기
		  String st=request.getParameter("pageNum");
		  Paging paging = new Paging();
		  paging.page(dao.parts_total(products),st,model);
		  ArrayList<Score> scores =dao.parts(paging.endRow,paging.startRow,products);
		  model.addAttribute("score",scores);
		  //최신목록
	      ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	     
	      return "productssel";
	}
}
	
	//장르볗----------------------------------------------------------------------------------------------------------------------------------------
	   @RequestMapping("/genre")
	   public String genre(HttpServletRequest request,Model model) {
	      System.out.println("products()");
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      
	      ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	      
	      if(session.getAttribute("loginuser")!=null) {
	    	//페이징하기
			  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.score_total(),st,model);
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow);
		      model.addAttribute("score",scores);
		      
		      model.addAttribute("home",dto);

		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      model.addAttribute("sideImg",sideImg);
		      
		      return "logingenre";
	      }else {
		   	  //페이징하기
			  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.score_total(),st,model);
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow);
		      model.addAttribute("score",scores);
		      
		      return "genre";
	      }
	   }
	   
	   //장르볗 카테고리 
	   @RequestMapping("/genresel")
	   public String genresel(HttpServletRequest request,Model model) {
	      System.out.println("genresel()");
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      String genre=request.getParameter("genre");
	      model.addAttribute("gen",genre);
	      if(session.getAttribute("loginuser")!=null) {
	    	  //페이징
	    	  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.genre_total(genre),st,model);
			  ArrayList<Score> scores =dao.genre(paging.endRow,paging.startRow,genre);
		      model.addAttribute("score",scores);	      
		      //사용자 이름
		      model.addAttribute("home",dto);
		      //최신목록
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      
		      //최근 본 목록
		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      model.addAttribute("sideImg",sideImg);
	       
		      return "logingenre";
	      }else {
			 //페이징
	    	  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.genre_total(genre),st,model);
			  ArrayList<Score> scores =dao.genre(paging.endRow,paging.startRow,genre);
			  
		      model.addAttribute("score",scores);	
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      
		      return "genre";
		   }
	   }
	   
	  //공지사항 --------------------------------------------------------------------------------------------------------------------------------
	 //공지사항
	      @RequestMapping("/notice_list")
	      public String notice_list(HttpServletRequest request,Model model) {
	         System.out.println("notice_list()");
	         HttpSession session=request.getSession();
	         loginDto dto=(loginDto)session.getAttribute("loginuser");
	         String temp=(String) session.getAttribute("notc");
	         IDao dao=sqlSession.getMapper(IDao.class);
	         if(session.getAttribute("loginuser")==null){
	        	 //페이징
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.notice_total(),st,model);
				 ArrayList<notice> notice=dao.notice_page(paging.endRow,paging.startRow);
		         model.addAttribute("notice",notice);
	            
		         ArrayList<Newlist> sideImg=dao.sideImg(temp);
		         model.addAttribute("sideImg",sideImg);
		          
		         return "notice_list2";     
	         }else if("seungmi".equals(dto.getM_id())) {
	        	//페이징
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.notice_total(),st,model);
				 ArrayList<notice> notice=dao.notice_page(paging.endRow,paging.startRow);
		         model.addAttribute("notice",notice);
		         model.addAttribute("home",dto);
		         
		         return "notice_list";
	      }else if(!"seungmi".equals(dto.getM_id())){
    	  	 //페이징
        	 String st=request.getParameter("pageNum");
			 Paging paging = new Paging();
			 paging.page(dao.notice_total(),st,model);
			 ArrayList<notice> notice=dao.notice_page(paging.endRow,paging.startRow);
	         model.addAttribute("notice",notice);
	         
	     	 model.addAttribute("home",dto);
	     	 
	         return "notice_list3";
	      }else {
	    	  ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
	         return "notice_list3";
	      }
	   }
//공지사항 목록에서 -> 내용보기
      @RequestMapping("/notice_content_view")
      public String notice_content_view(HttpServletRequest request,Model model) {
         System.out.println("notice_content_view()");
         String id=request.getParameter("m_id");
         String a=request.getParameter("notice_id");
         int n_id=Integer.parseInt(a);
         HttpSession session=request.getSession();
         loginDto dto=(loginDto)session.getAttribute("loginuser");
         IDao dao=sqlSession.getMapper(IDao.class);
         model.addAttribute("notice",dao.noticecontent(n_id));
         if(session.getAttribute("loginuser")==null){
            return "notice";
         }else if("seungmi".equals(dto.getM_id())) {
            model.addAttribute("home",dto);
            ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
             model.addAttribute("sideImg",sideImg);
            return "notice3";
         }else if(!"seungmi".equals(dto.getM_id())) {
            model.addAttribute("home",dto);
            ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
             model.addAttribute("sideImg",sideImg);
               return "notice2";
         }else {
            model.addAttribute("home",dto);
            ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
             model.addAttribute("sideImg",sideImg);
               return "notice2";
         }
      }
//공지사항 목록에서 -> 수정하기 
          @RequestMapping("/notice_modify")
          public String notice_modify(HttpServletRequest request,Model model) {
             System.out.println("notice_modify()");
             HttpSession session=request.getSession();
             if(session.getAttribute("loginuser")!=null) {
                String a=request.getParameter("notice_id");
                int n_id=Integer.parseInt(a);
                IDao dao=sqlSession.getMapper(IDao.class);
                model.addAttribute("modify_view",dao.noticecontent(n_id));
                
                return"/notice_modify";
             }else {
                return "redirect:login";
             }
             
          }
          
//공지사항 목록에서 -> 수정완료
      @RequestMapping(value="/notice_mo",produces="text/plain;charset=UTF-8")
      public String notice_mo(HttpServletRequest request,Model model) {
         System.out.println("notice_modify()");
         String notice_id=request.getParameter("notice_id");
         HttpSession session=request.getSession();
         loginDto dto=(loginDto)session.getAttribute("loginuser");
         int n_id=Integer.parseInt(notice_id);
         IDao dao=sqlSession.getMapper(IDao.class);
         dao.notice_update(request.getParameter("title"),request.getParameter("notice_con"),n_id);
         model.addAttribute("home",dto);
         return"redirect:notice_content_view?notice_id="+notice_id+"&m_id="+dto.getM_id();
      }
//공지사항 삭제하기
      @RequestMapping(value="/notice_delete",produces="text/plain;charset=UTF-8")
      public String notice_delete(HttpServletRequest request,Model model) {
         System.out.println("notice_delete()");
         HttpSession session=request.getSession();
         IDao dao=sqlSession.getMapper(IDao.class);
         String a=request.getParameter("notice_id");
         int n_id=Integer.parseInt(a);
         dao.notice_delete(n_id);
         loginDto dto=(loginDto)session.getAttribute("loginuser");
           model.addAttribute("home",dto);
         
         return "redirect:notice_list?pageNum=1";
      }
	    
//공지사항 쓰기
      @RequestMapping("/notice_write_view")
      public String notice_write_view(HttpServletRequest request,Model model) {
         System.out.println("notice_write_view()");
         HttpSession session=request.getSession();
         loginDto dto=(loginDto)session.getAttribute("loginuser");
         
         if(session.getAttribute("loginuser")!=null) {
         IDao dao=sqlSession.getMapper(IDao.class);
         model.addAttribute("home",dto);
         ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
         model.addAttribute("sideImg",sideImg);
         return "notice_write_view";
         }else {
            return "redirect:login";
         }
      }
      
//공지사항 글쓰기 완료
      @RequestMapping(value="/notice_write",method=RequestMethod.POST)
      public String notice_write(HttpServletRequest request,Model model) {
         System.out.println("notice_write()");
         HttpSession session=request.getSession();
         loginDto dto=(loginDto)session.getAttribute("loginuser");
         IDao dao=sqlSession.getMapper(IDao.class);
         
         String m_id=dto.getM_id();
         String title=request.getParameter("notice_title");
         String name=request.getParameter("notice_name");
         String con=request.getParameter("content");
         
         dao.notice_insert(m_id,name,title,con);
     	model.addAttribute("home",dto);
         
         return"redirect:notice_list?pageNum=1";
      }
//---------------------------------------------------------------------------------------------------------------
	      
	      @RequestMapping(value="/search_products",produces="text/plain;charset=UTF-8")
		   public String seach_products(HttpServletRequest request,Model model) {
		      System.out.println("seach_products()");
		      HttpSession session=request.getSession();
		      loginDto dto=(loginDto)session.getAttribute("loginuser");
		      IDao dao=sqlSession.getMapper(IDao.class);
		      String id=request.getParameter("search");
			  String iss=request.getParameter("search");
				
		      if(session.getAttribute("loginuser")!=null) {
		    	 //DB갯수 레코드 갯수 확인
		    	 int total=dao.search_total(id,iss);
		    	 //페이징
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.search_total(id,iss),st,model);
		         ArrayList<Score> score=dao.search_select(paging.endRow,paging.startRow,id,iss);
			     model.addAttribute("score",score);
			     
			     //최신목록
			     ArrayList<Score> newScore=dao.newScore();
			     //최근 본 목록
			     ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      
			     model.addAttribute("home",dto);
			     model.addAttribute("sea",iss);
			     model.addAttribute("noid",id);
		      if(total==0) {		    	  
		          model.addAttribute("sideImg",sideImg);         
		          model.addAttribute("newScore",newScore);
		    	  return "nob_login";
		      }else {  
		    	  model.addAttribute("newScore",newScore);
		          model.addAttribute("sideImg",sideImg);
		    	  return "search_products_login";
		      }
		   }else {
			   //DB갯수 레코드 갯수 확인
		       int total=dao.search_total(id,iss);
		       //페이징
			   String st=request.getParameter("pageNum");
			   Paging paging = new Paging();
			   paging.page(dao.search_total(id,iss),st,model);
			   ArrayList<Score> score=dao.search_select(paging.endRow,paging.startRow,id,iss);
			   model.addAttribute("score",score);
		       ArrayList<Score> newScore=dao.newScore();
		    
		      
		       model.addAttribute("sea",iss);
		       model.addAttribute("noid",id);
		       model.addAttribute("newScore",newScore);
		       if(total==0) {
		    	  
		    	   return "nob";
		       }else {
		       	   return "search_products";
		       }
		   }
		  } 
}


