<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
<style type="text/css">
.container{
	margin-top: 30px;
}
.row{
	width: 590px;
	margin: 0px auto;
}
</style>
<script type="text/javascript" src="http://code.jquery.com/jquery.js"></script>
<script type="text/javascript">
/*
	자바스크립트 : 변수설정(let, const), 연산자, 제어문, function
	
	JSP(MVC패턴)  제이쿼리(Ajax)   DBCP(SQL, 웹흐름(MVC))
	|            |             |                 
	Spring       VueJS         MyBatis                  
	|            |             |                             
	Spring-Boot  ReactJS       JPA                      
*/
$(function(){
	$('#postfindBtn').click(function(){
		let dong=$('#dong').val(); //#dong에 입력한 값
		if(dong.trim()==="")
		{
			$('#dong').focus()
			return
		}
		$.ajax({
			type:'post',
			url:'../member/postfind_result.do',
			data:{"dong":dong},
			success:function(result)
			{
				$('#print').html(result)
			}
		})
	})
})
</script>
</head>
<body>
  <div class="container">
    <div class="row">
      <table class="table">
        <tr>
          <td class="text-center">
            입력 <input type=text name=dong id=dong size=20 class="input-sm">
            <input type=button value=우편번호검색 class="btn btn-sm btn-danger" id="postfindBtn">
          </td>
        </tr>
      </table>
      <%-- 결과값 싣는 곳 --%>
      <span id="print">
      
      
      </span>
    </div>
  </div>
</body>
</html>