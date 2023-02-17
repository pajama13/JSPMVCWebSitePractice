package com.sist.controller;

import java.io.*;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.*;
import com.sist.model.*;
import java.net.*;
//DispatcherServlet => Spring(Controller) => 동작 => 구분(어노테이션), 등록(XML)
/*
	 MVC 동작과정
	 1. 요청 => DispatcherServlet을 찾는다
	          ------------------- URL주소로 찾는다 => .do  (list.do, insert.do, delete.do => .do가 붙으면 DispatcherServlet 호출)
	 
	 	** Controller : 요청을 받는다 => Model로 전송(요청내용 전송 => request)
	 					-----------------------
	 					Model 처리결과를 받는다 => request.setAttribute() - 여러 개 사용 가능
	 					-JSP로 request를 전송 출력 => forward
	 					-이미 존재하는 화면으로 이동 => sendRedirect
	 	**Model : 요청에 대한 처리(데이터 관련) - 데이터베이스 처리, 크롤링, request에 값을 담아주는 역할
	 	**View : Controller가 전송한 request에 담긴 데이터를 출력하는 역할
	 	
	 	MVC 사용 목적 : 보안 / 확장성 / 재사용 / 유지보수 수월 / 역할분담 & 동시작업 수월
	 	 
	 
	 JSP (링크, 데이터 전송)  =======> DispatcherServlet =======> Model =======> DAO =======> 오라클 
	                                      |          <=======   |   <=======     <=======
	                                      |                     |   ArrayList, VO  결과값 읽기
	                                      |                  가져온 데이터를 request에 담는다
	                              Model에서 받은 request를 해당 JSP로 전송
	                                             |
	                                          request에 담긴 데이터를 출력한 화면을 브라우저 전송
	                            
	 ***DispatcherServlet은 고정됨 => 이걸 건드리면 서버문제 생겨서 모두 사이트 나가야 함
	 ***Model이 가지고 있는 모든 메소드를 자동 호출되도록 만듬
	 public class A
	 {
	    @ => FIELD 
	    B b;
	    public void display()
	    {
	    }
	    public void disp(@ => PARAMETER B b)
	    {
	    }
	 } 
	 
	 데이터 전송
	 : <a>, <form>, ajax   .do
	                       ----
	                       폴더구분
	                       food/list.do
	                       notice/list.do
	                       
	 Controller 역할
	 1) 사용자 요청 => 해당 Model 클래스를 찾는다 => 등록된 Model클래스 로딩
	 2) 요청 처리 => Model 메소드 호출
	 3) Model 메소드 호출 후 결과값을 받기 (request, session)
	 4) JSP를 찾아서 request를 전송 => forward()
	 
	 
	 음식점
	  |
	 고객 : 음식주문(.do) ==> 서빙(Controller) : 주문 받고 주방에 요청 ==> 주방 Model
	                                             <==
	                                          주문 음식 전달
	     
*/
@WebServlet("*.do")
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ArrayList<String> clsList=new ArrayList<String>(); //application.xml에 등록된 클래스를 모아둔다 => Model클래스 인식(메뉴판 가져오기)
    //초기화 => 사용자에게 서비스 (JSP로 request => 화면 브라우저에 출력)
	//Model 클래스를 가지고 있어야 함

	public void init(ServletConfig config) throws ServletException {
		//Model 클래스를 읽어온다
		try
		{
			//1. XML파일 가져오기
			//1-1. XML의 파일 읽기 => 호환성 => 
			URL url=this.getClass().getClassLoader().getResource("."); // .은 현재 폴더를 의미 => 현재폴더 경로 읽기
			File file=new File(url.toURI());
			System.out.println(file.getPath()); 
			//C:\webDev\webStudy\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\JSPFinalProject\WEB-INF\classes
			String path=file.getPath();
			path=path.replace("\\", File.separator);
			path=path.substring(0,path.lastIndexOf(File.separator));
			path=path+File.separator+"application.xml";
			//2. 클래스명 읽기
			//2-1. XML 파싱 => JAXP(DOM, SAX:MyBatis, Spring:SAX), JAXB(1.8)
			/*
				DOM : 메모리 저장 => 트리형식 (속도 느림) => 추가, 수정, 삭제 가능
				SAX : 한줄씩 읽어서 데이터만 추출 (속도 빠름) => 추가, 수정, 삭제 불가능
				스프링, 마이바티스 => 파싱이 이미 되어 있음
				Jsoup(X)
			*/
			//2-2. XML 파싱 : 파싱 클래스 생성
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			//html, xml, wml... => Factory패턴 - ex DriverManager
			//2-3. 파싱기를 가지고 온다
			DocumentBuilder db=dbf.newDocumentBuilder();
			//2-4. XML을 파싱 => 메모리에 트리형태로 저장
			Document doc=db.parse(new File(path));
			  //System.out.println(doc.toString());
			//2-5. 최상위태그 읽기 (테이블명)
			Element root=doc.getDocumentElement();
			  //System.out.println(root.getTagName());
			//2-6. 같은 태그를 묶어서 데이터 읽기 시작
			NodeList node=root.getElementsByTagName("bean");
			  //System.out.println(node.toString());
			//3. ArrayList에 저장 (사용자가 요청하면 service()에서 찾아줌)
			for(int i=0;i<node.getLength();i++)
			{
				Element bean=(Element)node.item(i);
				String cls=bean.getAttribute("class");
				System.out.println(cls);
				clsList.add(cls);
			}
		}catch(Exception ex) {}
	}
	//브라우저에 필요한 데이터를 전송
	//Model이 갖고 있는 메소드를 호출 => 요청 처리 => request를 해당 JSP로 전송
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//사용자가 요청 => 등록된 Model의 해당 메소드를 찾는다 => 메소드 자동 호출 => request, session을 JSP로 전송
		try
		{
			//1. 사용자가 요청한 주소를 확인 => ?뒤에는 자동으로 request 통해 값이 들어옴
			String uri=request.getRequestURI();
			System.out.println(uri);
			// /JSPFinalProject/main/main.do => 구분자 : main/main.do
			uri=uri.substring(request.getContextPath().length()+1);
			System.out.println("uri"+uri);
			//uri에 해당하는 메소드를 찾아서 호출 => 결과값 jsp로 전송
			for(String cls:clsList) //등록된 클래스 안에 있는 요청에 해당되는 메소드 호출
			{
				Class clsName=Class.forName(cls);
				//클래스 위에 @Controller가 없는 경우에는 제외시키기
				if(clsName.isAnnotationPresent(Controller.class)==false)
					continue;
				//1) 메모리 할당(클래스)
				Object obj=clsName.getDeclaredConstructor().newInstance();
				//2) 메소드 찾기
				Method[] methods=clsName.getDeclaredMethods();
				for(Method m:methods)
				{
					//어노테이션 이용해서 메소드를 찾는다
					RequestMapping rm=m.getAnnotation(RequestMapping.class);
					if(rm.value().equals(uri)) //RequestMapping value와 요청 uri 같은 것 찾기
					{
						//찾으면 메소드 호출해라
						String jsp=(String)m.invoke(obj, request,response); //메소드명 상관없이 어노테이션이 일치하는 메소드는 호출 가능
						//request를 JSP로 전송
						//방법1 : sendRedirect => request 초기화시켜서 보내기
						//방법2 : forward() => request 그대로 전송
						if(jsp==null)
						{
							return; // => 리턴형 String, void(ajax)
						}
						if(jsp.startsWith("redirect:"))	// 언제 사용? : _ok.jsp 와 같은 처리만 할 때 (화면 출력이 아닌)
						{
							response.sendRedirect(jsp.substring(jsp.indexOf(":")+1));
						}
						else
						{
							RequestDispatcher rd=request.getRequestDispatcher(jsp);
							rd.forward(request, response);
						}
						return;
					}
				}
			}
		}catch(Exception ex) {}
	}

}







