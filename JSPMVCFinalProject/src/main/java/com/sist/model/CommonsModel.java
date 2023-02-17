package com.sist.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sist.dao.NoticeDAO;
import com.sist.vo.NoticeVO;

// 공통으로 들어가는 부분은 => Controller가 없는 부분으로!!
public class CommonsModel {
   // .do에서 공통으로 들어가는 부분
      public static void footerData(HttpServletRequest request)
      {
         // Footer 출력
            NoticeDAO ndao =new NoticeDAO();
            List<NoticeVO> nList=ndao.noticeTop5();
            request.setAttribute("nList", nList);
      }
}