package com.sist.dao;
import java.util.*;

import com.sist.vo.BoardReplyVO;
import com.sist.vo.FreeBoardVO;

import java.sql.*;
/*
	SELECT, DELETE, UPDATE, INSERT
	인라인뷰 / 조인 / 서브쿼리
	


           Collection
               |
       -----------------
       |       |       |
      List    Set     Map
       |
    ArrayList
     Vector
	
*/
public class FreeBoardDAO {
	private Connection conn;
	private PreparedStatement ps;
	
	//목록 출력
	public List<FreeBoardVO> boardListData(int page)
	{
		List<FreeBoardVO> list=new ArrayList<FreeBoardVO>();
		try
		{
			conn=CreateConnection.getConnection();
			String sql="SELECT no,subject,name,TO_CHAR(regdate,'YYYY-MM-DD'),hit,num "
					  +"FROM (SELECT no,subject,name,regdate,hit,rownum as num "
					  +"FROM (SELECT /*+ INDEX_DESC(project_freeboard pf1_no_pk)*/ no,subject,name,regdate,hit "
					  +"FROM project_freeboard)) "
					  +"WHERE num BETWEEN ? AND ?";
			ps=conn.prepareStatement(sql); //SQL문장을 오라클 전송
			int rowSize=10;
			int start=(rowSize*page)-(rowSize-1);
			int end=rowSize*page;
			ps.setInt(1, start);
			ps.setInt(2, end);
			ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				FreeBoardVO vo=new FreeBoardVO();
				vo.setNo(rs.getInt(1));
				vo.setSubject(rs.getString(2));
				vo.setName(rs.getString(3));
				vo.setDbday(rs.getString(4));
				vo.setHit(rs.getInt(5));
				
				list.add(vo);
			}
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps); //반환
		}
		return list;
	}
	
	public int boardTotalPage()
	{
		int total=0;
		try
		{
			conn=CreateConnection.getConnection();
			String sql="SELECT CEIL(count(*)/10.0) FROM project_freeboard";
			ps=conn.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			rs.next();
			total=rs.getInt(1);
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
		return total;
	}
	//추가(글쓰기)
	public void boardInsert(FreeBoardVO vo)
	{
		try
		{
			conn=CreateConnection.getConnection();
			String sql="INSERT INTO project_freeboard(no,name,subject,content,pwd) "
						+"VALUES(pf_no_seq.nextval,?,?,?,?)";
			//sql 문장 전송
			ps=conn.prepareStatement(sql);
			//실행 전 ?에 값 채우기
			ps.setString(1, vo.getName());
			ps.setString(2, vo.getSubject());
			ps.setString(3, vo.getContent());
			ps.setString(4, vo.getPwd());
			//실행 명령 => executeUpdate() => commit 포함
			ps.executeUpdate();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
	}
	//상세보기
	public FreeBoardVO boardDetailData(int no)
	{
		FreeBoardVO vo=new FreeBoardVO();
		try
		{
			conn=CreateConnection.getConnection();
			//조회수 증가
			String sql="UPDATE project_freeboard SET "
						+"hit=hit+1 "
						+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ps.executeUpdate();
			//실제 게시물번호에 해당되는 데이터 가져오기
			sql="SELECT no,name,subject,content,TO_CHAR(regdate,'YYYY-MM-DD'),hit "
				+"FROM project_freeboard "
				+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			vo.setDbday(rs.getString(5));
			vo.setHit(rs.getInt(6));
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
		return vo;
	}
	//수정
	public FreeBoardVO boardUpdateData(int no)
	{
		FreeBoardVO vo=new FreeBoardVO();
		try
		{
			conn=CreateConnection.getConnection();
			//조회수 증가
			String sql="SELECT no,name,subject,content "
					+"FROM project_freeboard "
					+"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			vo.setNo(rs.getInt(1));
			vo.setName(rs.getString(2));
			vo.setSubject(rs.getString(3));
			vo.setContent(rs.getString(4));
			rs.close();
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
		return vo;
	}
	public boolean boardUpdate(FreeBoardVO vo)
	{
		boolean bCheck=false;
		try
		{
			conn=CreateConnection.getConnection();
			String sql="SELECT pwd FROM project_freeboard "
					  +"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, vo.getNo());
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			if(db_pwd.equals(vo.getPwd()))
			{
				bCheck=true;
				sql="UPDATE project_freeboard SET "
					+"name=?,subject=?,content=? "
					+"WHERE no=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, vo.getName());
				ps.setString(2, vo.getSubject());
				ps.setString(3, vo.getContent());
				ps.setInt(4, vo.getNo());
				ps.executeUpdate();
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
		return bCheck;
	}
	//삭제
	public boolean boardDelete(int no,String pwd)
	{
		boolean bCheck=false;
		try
		{
			conn=CreateConnection.getConnection();
			String sql="SELECT pwd FROM project_freeboard "
					  +"WHERE no=?";
			ps=conn.prepareStatement(sql);
			ps.setInt(1, no);
			ResultSet rs=ps.executeQuery();
			rs.next();
			String db_pwd=rs.getString(1);
			rs.close();
			if(db_pwd.equals(pwd))
			{
				bCheck=true;
				sql="DELETE FROM project_freeboard "
					+"WHERE no=?";
				ps=conn.prepareStatement(sql);
				ps.setInt(1, no);
				ps.executeUpdate();
			}
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally
		{
			CreateConnection.disConnection(conn, ps);
		}
		return bCheck;
	}
	//댓글
   public void replyInsert(BoardReplyVO vo)
   {
	   try
	   {
		   conn=CreateConnection.getConnection();
		   String sql="INSERT INTO project_reply(rno,bno,id,name,msg,group_id) "
				    +"VALUES(pr_rno_seq.nextval,?,?,?,?,(SELECT NVL(max(group_id)+1,1) FROM project_reply))";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, vo.getBno());
		   ps.setString(2, vo.getId());
		   ps.setString(3, vo.getName());
		   ps.setString(4, vo.getMsg());
		   ps.executeUpdate();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   CreateConnection.disConnection(conn, ps);
	   }
   }
   //댓글 읽기
   public List<BoardReplyVO> replyListData(int bno)
   {
	   List<BoardReplyVO> list=new ArrayList<BoardReplyVO>();
	   try
	   {
		   conn=CreateConnection.getConnection();
		   String sql="SELECT rno,bno,id,name,msg,group_tab,TO_CHAR(regdate,'YYYY-MM-DD HH24:MI:SS') "
				      +"FROM project_reply "
				      +"WHERE bno=? "
				      +"ORDER BY group_id DESC,group_step ASC";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, bno);
		   ResultSet rs=ps.executeQuery();
		   while(rs.next())
		   {
			   BoardReplyVO vo=new BoardReplyVO();
			   vo.setRno(rs.getInt(1));
			   vo.setBno(rs.getInt(2));
			   vo.setId(rs.getString(3));
			   vo.setName(rs.getString(4));
			   vo.setMsg(rs.getString(5));
			   vo.setGroup_tab(rs.getInt(6));
			   vo.setDbday(rs.getString(7));
			   list.add(vo);
		   }
		   rs.close();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   CreateConnection.disConnection(conn, ps);
	   }
	   return list;
   }
   public void replyUpdate(int rno,String msg)
   {
	   try
	   {
		   conn=CreateConnection.getConnection();
		   String sql="UPDATE project_reply SET "
				     +"msg=? "
				     +"WHERE rno=?";
		   ps=conn.prepareStatement(sql);
		   ps.setString(1, msg);
		   ps.setInt(2, rno);
		   ps.executeUpdate();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
	   }
	   finally
	   {
		   CreateConnection.disConnection(conn, ps);
	   }
   }
   //트랜잭션 프로그램(일괄처리) => sql문장 여러 개를 동시에 수행 => 스프링
   public void replyReplyInsert(int root,BoardReplyVO vo)
   {
	   try
	   {
		   conn=CreateConnection.getConnection();
		   conn.setAutoCommit(false);  //오토커밋 안 되도록 : 롤백 수행하기 위함임(여러 작업 중 하나가 안 되면 다른 것도 작동 안 되게 catch절에 롤백 수행)
		   //1. root가 가진 group_id, group_step, group_tab을 가져오기
		   String sql="SELECT group_id,group_step,group_tab "
		   		 	 +"FROM project_reply "
				     +"WHERE rno=?";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, root);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   int gi=rs.getInt(1);
		   int gs=rs.getInt(2);
		   int gt=rs.getInt(3);
		   rs.close();
		   rs.close();
		   //2. group_step 증가
		   sql="UPDATE project_reply SET "
			   +"group_step=group_step+1 "
			   +"WHERE group_id=? AND group_step>?";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, gi);
		   ps.setInt(2, gs);
		   ps.executeUpdate();
		   //3. INSERT commit() ==> rollback은 수행하지 않음(기술면접 자주 등장)
		   sql="INSERT INTO project_reply(rno,bno,id,name,msg,group_id,group_step,group_tab,root) "
			   +"VALUES(pr_rno_seq.nextval,?,?,?,?,?,?,?,?)";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, vo.getBno());
		   ps.setString(2, vo.getId());
		   ps.setString(3, vo.getName());
		   ps.setString(4, vo.getMsg());
		   ps.setInt(5, gi);
		   ps.setInt(6, gs+1);
		   ps.setInt(7, gt+1);
		   ps.setInt(8, root);
		   ps.executeUpdate();
		   //4. UPDATE (depth 증가)
		   sql="UPDATE project_reply SET "
			  +"depth=depth+1 "
		      +"WHERE rno=?";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, root);
		   ps.executeUpdate();
		   conn.commit();
	   }catch(Exception ex)
	   {
		   ex.printStackTrace();
		   try
		   {
			   conn.rollback(); //둘 중 하나가 실패하면 롤백 수행
		   }catch(Exception e) {}
	   }
	   finally
	   {
		   try
		   {
			   conn.setAutoCommit(true);
		   }catch(Exception ex) {}
		   CreateConnection.disConnection(conn, ps);
	   }
   }

   public void replyDelete(int rno)
   {
	   try
	   {
		   //트랜잭션 프로그램 : 오토커밋 해제 => 정상수행 시 커밋 날리기 / 오류 발생 시 롤백 수행
		   conn=CreateConnection.getConnection();
		   conn.setAutoCommit(false);
		   //1. root, depth
		   String sql="SELECT root,depth FROM project_reply "
				     +"WHERE rno=? ";
		   ps=conn.prepareStatement(sql);
		   ps.setInt(1, rno);
		   ResultSet rs=ps.executeQuery();
		   rs.next();
		   int root=rs.getInt(1);
		   int depth=rs.getInt(2);
		   rs.close();
		   //2. depth=0 (DELETE), depth!=0(UPDATE)
		   if(depth==0)
		   {
			   sql="DELETE FROM project_reply "
				  +"WHERE rno=?";
			   ps=conn.prepareStatement(sql);
			   ps.setInt(1, rno);
			   ps.executeUpdate();
			   //depth감소 => root
			   sql="UPDATE project_reply SET "
				  +"depth=depth-1 "
				  +"WHERE rno=?";
			   ps=conn.prepareStatement(sql);
			   ps.setInt(1, root);
			   ps.executeUpdate();
		   }
		   else
		   {
			   String msg="관리자에 의해 삭제된 댓글입니다.";
			   sql="UPDATE project_reply SET "
				  +"msg=? "
				  +"WHERE rno=?";
			   ps=conn.prepareStatement(sql);
			   ps.setString(1, msg);
			   ps.setInt(2, rno);
			   ps.executeUpdate();
		   }
		   //3. depth 감소
		   conn.commit();
	   }catch(Exception ex)
	   {
		   try
		   {
			   conn.rollback();
		   }catch(Exception e) {}
	   }
	   finally
	   {
		   try
		   {
			   conn.setAutoCommit(true);
			   CreateConnection.disConnection(conn, ps);
		   }catch(Exception e) {}
	   }
   }
}
