<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>맛집 & 여행</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
</head>
<body>
   <div class="wrapper row3">
     <main class="container clear"> 
       <!-- main body --> 
       <!-- ################################################################################################ -->
       <div class="content"> 
         <!-- ################################################################################################ -->
         <div id="gallery">
           <figure>
             <header class="heading">
              <form method =post action="../food/food_location.do" class="inline" >
                  <input type= "text" name ="ss" size=25 class="input-sm" value="${ss }">
                  <input type =submit value= "검색">
              </form>
             </header>
             <ul class="nospace clear">
              <c:forEach var ="vo" items="${list }" varStatus="s">
               <li class="one_quarter ${s.index%4==0?'first':'' }"><a href="#"><img src="${vo.poster }" title="${vo.name }"></a></li>
               </c:forEach>
             </ul>
             <figcaption>Gallery Description Goes Here</figcaption>
           </figure>
         </div>
         <!-- ################################################################################################ --> 
         <!-- ################################################################################################ -->
         <nav class="pagination">
           <ul>
           <c:forEach var ="i" begin="1" end="${totalpage }">
             <li ${i==curpage?'class=current':'' }><a href="../food/food_location.do?ss=${ss }&page=${i }">${i }</a></li>
            </c:forEach>
           </ul>
         </nav>
         <!-- ################################################################################################ --> 
       </div>
       <!-- ################################################################################################ --> 
       <!-- / main body -->
       <div class="clear"></div>
     </main>
   </div>
   <!-- ################################################################################################ --> 
   <!-- ################################################################################################ --> 
   <!-- ################################################################################################ -->
   
   </div>
</body>
</html>