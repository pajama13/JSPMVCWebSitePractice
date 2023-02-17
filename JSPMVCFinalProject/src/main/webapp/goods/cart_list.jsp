<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
  <div class="wrapper row3">
    <main class="container clear">
     <h2 class="sectiontitle">장바구니 목록</h2>
     <div style="height: 5px"></div>
     <c:if test="${count==0 }">
	     <table class="table">
	       <tr>
	         <td class="text-center">장바구니에 상품이 없습니다</td>
	       </tr>
	     </table>
     </c:if>
     <c:if test="${count>0 }">
     <table class="table">
       <tr>
        <th class="text-center">번호</th>
        <th class="text-center"></th>
        <th>상품명</th>
        <th class="text-center">수량</th>
        <th class="text-center">가격</th>
        <th class="text-center">총금액</th>
        <th class="text-center"></th>
       </tr>
       <c:forEach var="vo" items="${list }">
         <tr>
	        <td class="text-center">${vo.bno }</td>
	        <td class="text-center">
	         <img src="${vo.gvo.goods_poster }" style="width:30px;height: 30px">
	        </td>
	        <td>${vo.gvo.goods_name }</td>
	        <td>${vo.account}</td>
	        <td class="text-center">${vo.gvo.goods_price }</td>
	        <td class="text-center">${vo.total_price }</td>
	        <td class="text-center">
	         <a href="#" class="btn btn-xs btn-success">구매</a>
	         <a href="#" class="btn btn-xs btn-warning">취소</a>
	        </td>
	       </tr>
       </c:forEach>
     </table>
     </c:if>
    </main>
  </div>
</body>
</html>