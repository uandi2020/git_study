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
	
	//1
		@RequestMapping(value="/base_html")
		public String base_html(Model model) {
			System.out.println("agree()");
			int i = 0;
			return "base_html";
		}
	
//筌롫뗄�뵥占쎌냳占쎈읂占쎌뵠筌욑옙
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
	    		  StringBuffer te = new StringBuffer(); //�눧紐꾩쁽占쎈였占쎌뱽 �빊遺쏙옙占쎈릭椰꾧퀡援� 癰귨옙野껓옙 占쎈막 占쎈르 雅뚯눖以� 占쎄텢占쎌뒠占쎈릭占쎈뮉 占쏙옙占쎌뿯
	    		  Random rnd = new Random(); //占쎌삏占쎈쑁占쎈맙占쎈땾
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
	    		  String temp=te.toString(); //String 癰귨옙占쎌넎
	  			session.setAttribute("notc",temp); //session占쎈퓠 揶쏅�わ옙占쎌삢
	  			System.out.println("notc=["+temp+"]");
	  		}
	         model.addAttribute("newScore",newScore);
	         model.addAttribute("bestScore",bestScore);
	         model.addAttribute("gayoBestScore",gayoBestScore);
	         return "main";
	      }
	      
	   }
	//-------------------------------------------------------------------------------------------//
//嚥≪뮄�젃占쎌뵥占쎈읂占쎌뵠筌욑옙
	@RequestMapping(value="/login")
	public String login(Model model) {
		System.out.println("login()");
		return "login";
	}
	@RequestMapping(value="/logins",method=RequestMethod.POST)
	   public @ResponseBody String login(HttpServletRequest request,HttpServletResponse response,Model model) {
	      System.out.println("logins()");  
	      String id=request.getParameter("ids");//占쎈툡占쎌뵠占쎈탵
	      String pw=request.getParameter("pwd");//�뜮袁⑨옙甕곕뜇�깈
	      HttpSession session=request.getSession();
	      IDao dao=sqlSession.getMapper(IDao.class);
	      loginDto dto=dao.login(id,pw);//DB[select]
	      if(dto==null) {
	    	  //占쎈씨占쎌몵筌롳옙 0占쎌뱽 獄쏆꼹�넎占쎈뻻�놂옙 done�겫占썽겫袁⑸퓠 alert 占쎌뱽占쏙옙
	         return "0";
	      }else {
	    	 //占쎄쉭占쎈�∽옙肉� 占쎈툡占쎌뵠占쎈탵�몴占� 占쏙옙占쎌삢
	         session.setAttribute("loginuser",dto);
	      }
	      Gson gson=new Gson();
	      String log=gson.toJson(dto);
	      return "redirect:homes";
	   }
//嚥≪뮄�젃占쎌뵥 占쎈툡占쎌뵠占쎈탵 筌≪뼐由�
	@RequestMapping(value="/login_idFind")
	public String login_idFind(Model model) {
		System.out.println("login_idFind");
		return "login_idFind";
	}

//占쎈툡占쎌뵠占쎈탵 筌≪뼐由�
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
//占쎈툡占쎌뵠占쎈탵 占쎌젟癰귨옙 占쎌넅筌롳옙
   @RequestMapping(value="/idInfo",method=RequestMethod.POST) 
   public  String idInfo(HttpServletRequest request,Model model) {
      System.out.println("idInfo()");
      
      model.addAttribute("id", request.getParameter("hide"));
      return "idInfo";
   }
   
//嚥≪뮄�젃占쎌뵥 �뜮袁⑨옙甕곕뜇�깈 筌≪뼐由�
	@RequestMapping(value="/login_pwFind")
	public String login_pwFind(Model model) {
		System.out.println("login_pwFind()");
		return "login_pwFind";
	}
//�뜮袁⑨옙甕곕뜇�깈筌≪뼐由�(login_idFind) -> 占쎌뵥筌앹빖�궖占쎄땀疫뀐옙
	@RequestMapping(value="/pwFind",method=RequestMethod.POST)
	public @ResponseBody String pwFind(HttpServletRequest request,Model model) throws AddressException, MessagingException {
		IDao dao=sqlSession.getMapper(IDao.class);
		System.out.println("pwFind()");
		String id=request.getParameter("loginId");
		String name=request.getParameter("loginName");
		String mail1=request.getParameter("loginMail");
		String mail2=request.getParameter("loginMail2");
		String mail=mail1+'@'+mail2; //筌롫뗄�뵬
		
		System.out.println(mail);
		Member member=dao.member_pwSelect(mail,id,name);
		if(member==null) {
			return "0";
		}
		model.addAttribute("member",member);
		//�뜮袁⑨옙甕곕뜇�깈筌≪뼚釉섆틠�눊由�
		String pass="占쎈뼣占쎈뻿占쎌벥 �뜮袁⑨옙甕곕뜇�깈占쎈뮉 "+member.member_pw+" 占쎌뿯占쎈빍占쎈뼄.";
	    Email emails = new Email();
	    emails.EmailSend(mail,pass);
		return "pwInfo";
	}
//嚥≪뮄�젃占쎌뵥 �뜮袁⑨옙甕곕뜇�깈 筌≪뼐由�
	@RequestMapping(value="/pwInfo",method=RequestMethod.POST)
	public String pwInfo(HttpServletRequest request,Model model) {
		System.out.println("pwInfo()");
		IDao dao=sqlSession.getMapper(IDao.class);
		String id=request.getParameter("loginId");
		String name=request.getParameter("loginName");
		String mail1=request.getParameter("loginMail");
		String mail2=request.getParameter("loginMail2");
		String mail=mail1+'@'+mail2; //筌롫뗄�뵬
		model.addAttribute("member",dao.member_pwSelect(mail,id,name));
		return "pwInfo";
	}
//-------------------------------------------------------------------------------------------//	
//嚥≪뮄�젃占쎈툡占쎌뜍
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request,Model model) {
		System.out.println("logout()");
		HttpSession session=request.getSession();
		session.removeAttribute("loginuser");
//		session.invalidate();
		return "redirect:home";
		
	}
		
	//-------------------------------------------------------------------------------------------//		
	   //筌ㅼ뮇�뻿占쎈툢癰귨옙
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
    //甕곗쥙�뮞占쎈뱜占쎈툢癰귨옙
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
//占쎌돳占쎌뜚揶쏉옙占쎌뿯 占쎌읈 占쎈짗占쎌벥占쎈릭疫뀐옙
	@RequestMapping(value="/agree")
	public String agree(Model model) {
		System.out.println("agree()");
		return "agree";
	}
//占쎌돳占쎌뜚揶쏉옙占쎌뿯
	@RequestMapping(value="/join")
	public String join(Model model) {
		System.out.println("join()");
		return "join";
	}
//占쎌돳占쎌뜚揶쏉옙占쎌뿯 占쎌뜎 �빊類λ릭
	@RequestMapping(value="/join_welcome",method=RequestMethod.POST)
	public String join_welcome(HttpServletRequest request,Model model) {
		System.out.println("join_welcome()");
		model.addAttribute("memberName",request.getParameter("joinName"));
		return "join_welcome";
	}
//占쎈툡占쎌뵠占쎈탵餓λ쵎�궗筌ｋ똾寃�
   @RequestMapping(value="/join_idchk",method=RequestMethod.POST)
   public @ResponseBody String join_idchk(HttpServletRequest request,Model model) {
      String id=request.getParameter("id");
      IDao dao=sqlSession.getMapper(IDao.class);
      Member member=dao.join_idchk(id);
      if(member==null) {
         return "0";
      }
      System.out.println("member["+member.m_id+"]");
      Gson gson=new Gson(); //json type占쎌몵嚥∽옙 獄쏆꼹�넎
      String member_id=gson.toJson(member.m_id);
      return member_id;//done占쎈퓠 return揶쏅�れ뵠 占쎈굶占쎈선揶쏉옙
      }
	   
//占쎌뵠筌롫뗄�뵬 餓λ쵎�궗筌ｋ똾寃�
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

//占쎌삢獄쏅떽�럡占쎈빍
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
//占쎌삢獄쏅떽�럡占쎈빍占쎈퓠 占쎈뼊占쎈즴占쎄맒占쎈�뱄옙苑뷂옙�뱽占쎈르 insert占쎈연占쎌쑎甕곤옙 占쎈툧占쎈┷野껓옙
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
				System.out.println("占쎈툧占쎈�욑옙釉�占쎄쉭占쎌뒄");
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
				System.out.println("占쎈툧占쎈�욑옙釉�占쎄쉭占쎌뒄");
				return "redirect:home";
			} else  {
				return "redirect:cart";
			}	
	}
//占쎌삢獄쏅떽�럡占쎈빍 占쎄퐨占쎄문占쎄텣占쎌젫	
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

//占쎌삢獄쏅떽�럡占쎈빍筌뤴뫀紐��뜮袁⑹뒭疫뀐옙
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
//雅뚯눖揆野껉퀣�젫筌∽옙
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
	
	//雅뚯눖揆野껉퀣�젫 占쎌끏�뙴占�
	   @RequestMapping(value="/pay_complete",produces="text/plain;charset=UTF-8")
	   public String pay_complete(HttpServletRequest request,Model model) {
	      System.out.println("pay_complete()");
	      String del = request.getParameter("cart_id");//燁삳똾�뱜 占쎈툡占쎌뵠占쎈탵
	      String addr = request.getParameter("addr");//雅뚯눘�꺖 獄쏄퀣�꽊筌욑옙
	      String point = request.getParameter("point"); //占쎌읅�뵳�럡�닊
	      HttpSession session=request.getSession();
	      loginDto dto2=(loginDto) session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      String[] ad=del.split(",");
	      for(int i=0;i<ad.length;i++) {
	         Cart ca=dao.carts(ad[i]);
	         System.out.println();
	         dao.sales_insert(ca.getM_id(),ca.getS_id(),ca.getCart_name(),ca.getCart_parts(),ca.getCart_genre(),ca.getCart_total(),addr);//sales占쎈퓠 占쎄퐫
	      }
	      for(int j=0;j<ad.length;j++) {
	         dao.cart_delete(ad[j]);
	      }
	      
	       dao.mileage_sales_insert(dto2.getM_id(),point);
	       
	       String basemile =request.getParameter("basemile");
	       String usingmile = request.getParameter("usingmile"); 
	       int a= Integer.parseInt(basemile);//疫꿸퀣�� 筌띾뜆�뵬�뵳�딉옙
	       int b= Integer.parseInt(point);//占쎌읅�뵳�럥留� 筌띾뜆�뵬�뵳�딉옙
	       int c= Integer.parseInt(usingmile);//疫꿸퀣�덌쭕�뜆�뵬�뵳�딉옙占쎈퓠占쎄퐣 占쎄텢占쎌뒠占쎈막 筌띾뜆�뵬�뵳�딉옙
	      
	       
	       int mymileage = a + b;//筌ㅼ뮇伊� 占쎄땀 筌띾뜆�뵬�뵳�딉옙
	       
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
	//筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 野껉퀣�젫占쎄땀占쎈열(疫꿸퀡�궚)
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
	   
	//筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 占쎄텊筌욎뮆�롦에占� 鈺곌퀬�돳   
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
	   //筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 筌띾뜆�뵬�뵳�딉옙
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
	   
	//筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 占쎄땀占쎌젟癰귨옙
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
	//筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 占쎌뵠占쎌읈 �뜮袁⑨옙甕곕뜇�깈 占쎌넇占쎌뵥獄쎻뫖苡�
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
	//占쎄땀占쎌젟癰귣똻�땾占쎌젟   
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
	   
	   
	   //筌띾뜆�뵠占쎈읂占쎌뵠筌욑옙 筌≪뮉釉놂옙沅∽옙肉� �겫�뜄�쑎占쎌궎疫뀐옙
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
	      
	   //筌≪뮉釉놂옙沅∽옙肉� 占쎄텣占쎌젫
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
	    //筌≪뮉釉놂옙沅∽옙肉�筌뤴뫀紐��뜮袁⑹뒭疫뀐옙
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
//占쎄맒占쎈�� 占쎈읂占쎌뵠筌욑옙
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
	      
	      //筌≪뮆由븝옙堉깍옙�뿳占쎄돌 占쎌넇占쎌뵥
	      String mid = dto2.getM_id();
	      int sid = Integer.parseInt(s_id);
	      System.out.println("s_id["+sid+"] m_id["+mid+"]");
	      Heart product_heart = dao.product_heart(mid,sid);
	      model.addAttribute("heart",product_heart);
	      
	      //筌ㅼ뮇�뻿占쎄깻�뵳占쏙옙釉녑쳞占�
	      ArrayList<Newlist> newlist = dao.newlist_compare(dto2.getM_id(),s_id);
	      if(newlist.size()==0) {
	    	  //占쎈�믭옙�뵠�뇡遺우벥 筌ｃ꺂苡뀐쭪占� 占쎌쟿�굜遺얜굡 �⑥쥙��占쎈툡占쎌뵠占쎈탵 揶쏉옙占쎌죬占쎌궎疫뀐옙
	    	  ArrayList<String> rowid = dao.newlist_rowid(dto2.getM_id());
	    	  //占쎈툧野껊��뒄筌롳옙 筌ｃ꺂苡뀐쭪占� 占쎌쟿�굜遺얜굡 占쎄텣占쎌젫
	    	  dao.newlist_delete(dto2.getM_id(),rowid.get(0));
	      } else {
	    	  //野껊��뒄筌롫똻沅섓옙�젫
	    	  dao.newlist_delete2(dto2.getM_id(),s_id);
	      }
	      //�빊遺쏙옙
	      dao.newlist_insert(dto2.getM_id(),s_id); 
	      //筌ㅼ뮇�뻿筌뤴뫖以� �빊�뮆�젾
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

	//占쎈뼊占쎈즴占쎄맒占쎈�� 雅뚯눖揆占쎈읂占쎌뵠筌욑옙
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
       String del = request.getParameter("s_id"); //占쎄맒占쎈�배린�뜇�깈
       String count = request.getParameter("total"); //揶쏉옙占쎈땾
       String point = request.getParameter("point"); //占쎌읅�뵳�럡�닊
       int total = Integer.parseInt(count); //
       int dels = Integer.parseInt(del); //占쎄맒占쎈�배린�뜇�깈 占쎈떭占쎌쁽嚥∽옙 癰귨옙占쎌넎
       String addr = request.getParameter("addr"); //雅뚯눘�꺖(占쎈뮞占쎄쾿�뵳���뱜占쎈퓠占쎄퐣 ','嚥∽옙 占쎌뿳占쎈뮉 占쎈쐭占쎈맙)
       System.out.println(addr);
       HttpSession session=request.getSession();
       loginDto dto2=(loginDto) session.getAttribute("loginuser");
       IDao dao=sqlSession.getMapper(IDao.class);
       Score pay = dao.pay_product(dels);
       dao.sales_insert(dto2.getM_id(),dels,pay.getScore_name(),pay.getScore_parts(),pay.getScore_genre(),total,addr);
       
       dao.mileage_sales_insert(dto2.getM_id(),point);
       
       String basemile =request.getParameter("basemile");
       String usingmile = request.getParameter("usingmile"); 
       int a= Integer.parseInt(basemile);//疫꿸퀣�� 筌띾뜆�뵬�뵳�딉옙
       int b= Integer.parseInt(point);//占쎌읅�뵳�럥留� 筌띾뜆�뵬�뵳�딉옙

      
       
       int mymileage = a + b;//筌ㅼ뮇伊� 占쎄땀 筌띾뜆�뵬�뵳�딉옙
       
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
	//筌≪뮉釉�疫뀐옙
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
    //筌≪뮉釉�疫뀐옙 �뿆�뫁�꺖
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
	

//占쎌돳占쎌뜚揶쏉옙占쎌뿯 (join) -> 揶쏉옙占쎌뿯占쎈뻻 - DB[insert]
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
	
	
//占쎌돳占쎌뜚揶쏉옙占쎌뿯(join) -> 筌롫뗄�뵬癰귣�沅→묾占�(占쎌뵥筌앹빖苡뀐옙�깈)
		@RequestMapping(value = "/mailSender",method = RequestMethod.POST)
		public @ResponseBody void mailSender(HttpServletRequest request,HttpServletResponse response,ModelMap mo) throws AddressException,MessagingException{
		   String email=request.getParameter("email");
		   String pass=request.getParameter("pass");
		   pass="占쎌뵥筌앹빖苡뀐옙�깈占쎈뮉 "+pass+" 占쎌뿯占쎈빍占쎈뼄.";
		   Email emails = new Email();
		   emails.EmailSend(email,pass);
		}
//fqa�⑥쥒而쇽옙苑깍옙苑�----------------------------------------------------------------------------------------------------------------------------------------------
	   
		
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
//占쎈읂占쎌뵠筌욑옙 fqalist
		@RequestMapping("/fqa_list")
		public String list(HttpServletRequest request,Model model) {
			System.out.println("fqa_list()");
			HttpSession session=request.getSession();
			loginDto dto=(loginDto)session.getAttribute("loginuser");
			if(session.getAttribute("loginuser")!=null) {
			IDao dao=sqlSession.getMapper(IDao.class);
			ArrayList<fqaDto> list=dao.list();
			model.addAttribute("list",list);
			//占쎈읂占쎌뵠筌욑옙
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
		
//占쎄맒占쎈�뱄옙�읂占쎌뵠筌욑옙
		//-----------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/products")
	   public String products(HttpServletRequest request,Model model) {
	      System.out.println("products()");
	      
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      
	    if(session.getAttribute("loginuser")!=null) {
		      //占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
			  String st=request.getParameter("pageNum"); //筌뤿돃苡뀐쭪占� 占쎈읂占쎌뵠筌욑옙占쎌뵥筌욑옙
			  Paging paging = new Paging(); //Paging揶쏆빘猿쒐몴占� 占쎌뵥占쎈뮞占쎄쉘占쎈뮞
			  paging.page(dao.score_total(),st,model); //Paging 筌롫뗄�꺖占쎈굡 占쎌깈�빊占�
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow); //占쎈립占쎈읂占쎌뵠筌욑옙占쎈뼣 癰귣똻肉т빳占� 占쎈쑓占쎌뵠占쎄숲 鈺곌퀬�돳
		      model.addAttribute("score",scores);
		      
		      //占쎄텢占쎌뒠占쎌쁽 占쎌뵠�뵳占�
		      model.addAttribute("home",dto);
		      //筌ㅼ뮇�뻿筌뤴뫖以�
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      //筌ㅼ뮄�젏 癰귨옙 筌뤴뫖以�
		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      model.addAttribute("sideImg",sideImg);
		      
		      return "loginproducts";
	   }else {
			  System.out.println("占쎈읂占쎌뵠筌욌벤釉울옙�땾");
			  //占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
			  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.score_total(),st,model);
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow);
		      model.addAttribute("score",scores);
		      
		      //筌ㅼ뮇�뻿筌뤴뫖以�
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      
		      return "products";
	   }
	}
		
//productssel
//占쎈툢疫꿸퀡��  燁삳똾�믤�⑥쥓�봺 --------------------------------------------------------------------------------------------------------------------------------
	@RequestMapping("/productssel")
	public String productssel(HttpServletRequest request,Model model) {
	   System.out.println("productssel()");
	   HttpSession session=request.getSession();
	   loginDto dto=(loginDto)session.getAttribute("loginuser");
	   IDao dao=sqlSession.getMapper(IDao.class);
	   String products=request.getParameter("products");
	   model.addAttribute("pros",products);
	   if(session.getAttribute("loginuser")!=null) {
		 //占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
		  String st=request.getParameter("pageNum");
		  Paging paging = new Paging();
		  paging.page(dao.parts_total(products),st,model);
		  ArrayList<Score> scores =dao.parts(paging.endRow,paging.startRow,products);
		  model.addAttribute("score",scores);
		  //占쎄텢占쎌뒠占쎌쁽占쎌뵠�뵳占�
		  model.addAttribute("home",dto);
		  //筌ㅼ뮇�뻿筌뤴뫖以�
		  ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	      
	      return "loginproductssel";
	}else {
		 //占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
		  String st=request.getParameter("pageNum");
		  Paging paging = new Paging();
		  paging.page(dao.parts_total(products),st,model);
		  ArrayList<Score> scores =dao.parts(paging.endRow,paging.startRow,products);
		  model.addAttribute("score",scores);
		  //筌ㅼ뮇�뻿筌뤴뫖以�
	      ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	     
	      return "productssel";
	}
}
	
	//占쎌삢�몴����----------------------------------------------------------------------------------------------------------------------------------------
	   @RequestMapping("/genre")
	   public String genre(HttpServletRequest request,Model model) {
	      System.out.println("products()");
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      
	      ArrayList<Score> newScore=dao.newScore();
	      model.addAttribute("newScore",newScore);
	      
	      if(session.getAttribute("loginuser")!=null) {
	    	//占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
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
		   	  //占쎈읂占쎌뵠筌욌벤釉�疫뀐옙
			  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.score_total(),st,model);
			  ArrayList<Score> scores =dao.score(paging.endRow,paging.startRow);
		      model.addAttribute("score",scores);
		      
		      return "genre";
	      }
	   }
	   
	   //占쎌삢�몴���� 燁삳똾�믤�⑥쥓�봺 
	   @RequestMapping("/genresel")
	   public String genresel(HttpServletRequest request,Model model) {
	      System.out.println("genresel()");
	      HttpSession session=request.getSession();
	      loginDto dto=(loginDto)session.getAttribute("loginuser");
	      IDao dao=sqlSession.getMapper(IDao.class);
	      String genre=request.getParameter("genre");
	      model.addAttribute("gen",genre);
	      if(session.getAttribute("loginuser")!=null) {
	    	  //占쎈읂占쎌뵠筌욑옙
	    	  String st=request.getParameter("pageNum");
			  Paging paging = new Paging();
			  paging.page(dao.genre_total(genre),st,model);
			  ArrayList<Score> scores =dao.genre(paging.endRow,paging.startRow,genre);
		      model.addAttribute("score",scores);	      
		      //占쎄텢占쎌뒠占쎌쁽 占쎌뵠�뵳占�
		      model.addAttribute("home",dto);
		      //筌ㅼ뮇�뻿筌뤴뫖以�
		      ArrayList<Score> newScore=dao.newScore();
		      model.addAttribute("newScore",newScore);
		      
		      //筌ㅼ뮄�젏 癰귨옙 筌뤴뫖以�
		      ArrayList<Newlist> sideImg=dao.sideImg(dto.getM_id());
		      model.addAttribute("sideImg",sideImg);
	       
		      return "logingenre";
	      }else {
			 //占쎈읂占쎌뵠筌욑옙
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
	   
	  //�⑤벊占쏙옙沅쀯옙鍮� --------------------------------------------------------------------------------------------------------------------------------
	 //�⑤벊占쏙옙沅쀯옙鍮�
	      @RequestMapping("/notice_list")
	      public String notice_list(HttpServletRequest request,Model model) {
	         System.out.println("notice_list()");
	         HttpSession session=request.getSession();
	         loginDto dto=(loginDto)session.getAttribute("loginuser");
	         String temp=(String) session.getAttribute("notc");
	         IDao dao=sqlSession.getMapper(IDao.class);
	         if(session.getAttribute("loginuser")==null){
	        	 //占쎈읂占쎌뵠筌욑옙
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.notice_total(),st,model);
				 ArrayList<notice> notice=dao.notice_page(paging.endRow,paging.startRow);
		         model.addAttribute("notice",notice);
	            
		         ArrayList<Newlist> sideImg=dao.sideImg(temp);
		         model.addAttribute("sideImg",sideImg);
		          
		         return "notice_list2";     
	         }else if("seungmi".equals(dto.getM_id())) {
	        	//占쎈읂占쎌뵠筌욑옙
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.notice_total(),st,model);
				 ArrayList<notice> notice=dao.notice_page(paging.endRow,paging.startRow);
		         model.addAttribute("notice",notice);
		         model.addAttribute("home",dto);
		         
		         return "notice_list";
	      }else if(!"seungmi".equals(dto.getM_id())){
    	  	 //占쎈읂占쎌뵠筌욑옙
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
//�⑤벊占쏙옙沅쀯옙鍮� 筌뤴뫖以됵옙肉됵옙苑� -> 占쎄땀占쎌뒠癰귣떯由�
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
//�⑤벊占쏙옙沅쀯옙鍮� 筌뤴뫖以됵옙肉됵옙苑� -> 占쎈땾占쎌젟占쎈릭疫뀐옙 
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
          
//�⑤벊占쏙옙沅쀯옙鍮� 筌뤴뫖以됵옙肉됵옙苑� -> 占쎈땾占쎌젟占쎌끏�뙴占�
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
//�⑤벊占쏙옙沅쀯옙鍮� 占쎄텣占쎌젫占쎈릭疫뀐옙
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
	    
//�⑤벊占쏙옙沅쀯옙鍮� 占쎈쾺疫뀐옙
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
      
//�⑤벊占쏙옙沅쀯옙鍮� 疫뀐옙占쎈쾺疫뀐옙 占쎌끏�뙴占�
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
		    	 //DB揶쏉옙占쎈땾 占쎌쟿�굜遺얜굡 揶쏉옙占쎈땾 占쎌넇占쎌뵥
		    	 int total=dao.search_total(id,iss);
		    	 //占쎈읂占쎌뵠筌욑옙
	        	 String st=request.getParameter("pageNum");
				 Paging paging = new Paging();
				 paging.page(dao.search_total(id,iss),st,model);
		         ArrayList<Score> score=dao.search_select(paging.endRow,paging.startRow,id,iss);
			     model.addAttribute("score",score);
			     
			     //筌ㅼ뮇�뻿筌뤴뫖以�
			     ArrayList<Score> newScore=dao.newScore();
			     //筌ㅼ뮄�젏 癰귨옙 筌뤴뫖以�
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
			   //DB揶쏉옙占쎈땾 占쎌쟿�굜遺얜굡 揶쏉옙占쎈땾 占쎌넇占쎌뵥
		       int total=dao.search_total(id,iss);
		       //占쎈읂占쎌뵠筌욑옙
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


