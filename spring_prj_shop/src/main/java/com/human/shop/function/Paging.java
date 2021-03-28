package com.human.shop.function;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.human.shop.dto.Score;

public class Paging {
	public int startRow;
	public int endRow;

	public void page(int total,String st,Model model){
		//pageNum=1 ,페이지번호
		int currpage=Integer.parseInt(st);
        model.addAttribute("c",currpage);
        //한페이지당 보여줄 리스트 갯수
        int pagesize=10;
        //페이지 블록갯수
        int pageblock=5;
        //전체 글갯수
        int to=total%pagesize;
        if(to==0) {
           to=0;
        }else {
           to=1;
        }
        int cnt=total / pagesize + to;
        //총페이지 갯수
        model.addAttribute("cnt",cnt);
        //시작 끝 행 번호[db레코드 rownum)
        int startRow=(currpage - 1) * pagesize + 1;
        int endRow=currpage * pagesize;
        //시작페이지
        int na=(currpage%pageblock);
        if(na==0) {
           na=1;
        }else {
           na=0;
        }
        int startpage=((currpage/pageblock) - na) * pageblock + 1;
        //끝페이지
        int endpage=startpage +pageblock -1;
        if(endpage > cnt) {
           endpage=cnt;
        }
        model.addAttribute("endpage",endpage);
        //forEach문 들어갈 시작  끝
        int fs=pageblock-1;
        model.addAttribute("fs",fs);
        //[이전]
        if(startpage > pageblock) {
           int back=endpage-pageblock;
           System.out.println(back);
           model.addAttribute("back",back);
           int yes=1;
           model.addAttribute("startyes",yes);
        }else {
           int yes=0;
           model.addAttribute("startyes",yes);
        }
        //1~5
        int a=startpage;
        ArrayList<Integer> num= new ArrayList<Integer>();
        for(int i=startpage;i<=endpage;i++) {
           num.add(a);
           a++;
        }
        model.addAttribute("num",num);
        //[다음]
        if(endpage<cnt) {
           int nexts=startpage+pageblock;
           model.addAttribute("nexts",nexts);
           int no=1;
           model.addAttribute("endno",no);
        }else {
           int no=0;
           model.addAttribute("endno",no);
        }     
        this.startRow=startRow;
	    this.endRow=endRow; 
	}
	
	
}
