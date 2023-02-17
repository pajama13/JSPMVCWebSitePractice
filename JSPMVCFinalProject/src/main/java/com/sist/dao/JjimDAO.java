package com.sist.dao;
import java.util.*;
import com.sist.vo.*;
import java.sql.*;
public class JjimDAO {
  private Connection conn;
  private PreparedStatement ps;
  public void jjimInsert(JjimVO vo)
  {
	  try
	  {
		  conn=CreateConnection.getConnection();
		  String sql="INSERT INTO project_jjim VALUES("
				    +"(SELECT NVL(MAX(jno)+1,1) FROM project_jjim),?,?)";
		  ps=conn.prepareStatement(sql);
		  ps.setInt(1, vo.getNo());
		  ps.setString(2, vo.getId());
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
  // JJIM확인 
  public int jjimCount(int no,String id)
  {
	  int count=0;
	  try
	  {
		  conn=CreateConnection.getConnection();
		  String sql="SELECT COUNT(*) FROM project_jjim "
				    +"WHERE no=? AND id=?";
		  ps=conn.prepareStatement(sql);
		  ps.setInt(1, no);
		  ps.setString(2, id);
		  ResultSet rs=ps.executeQuery();
		  rs.next();
		  count=rs.getInt(1);
		  rs.close();
	  }catch(Exception ex)
	  {
		  ex.printStackTrace();
	  }
	  finally
	  {
		  CreateConnection.disConnection(conn, ps);
	  }
	  return count;
  }
  /*
   try
   {
	  conn=CreateConnection.getConnection();
   }catch(Exception ex)
   {
	  ex.printStackTrace();
   }
   finally
   {
	  CreateConnection.disConnection(conn, ps);
   }
  */
  // JJIM 목록 
  public List<JjimVO> jjimListData(String id)
  {
	  List<JjimVO> list=new ArrayList<JjimVO>();
	  try
	  {
		  conn=CreateConnection.getConnection();
		  String sql="SELECT /*+ INDEX_DESC(project_jjim pj_jno_pk)*/jno,no,"
				    +"(SELECT DISTINCT name FROM project_food WHERE fno=project_jjim.no),"
				    +"(SELECT DISTINCT poster FROM project_food WHERE fno=project_jjim.no),"
				    +"(SELECT DISTINCT address FROM project_food WHERE fno=project_jjim.no),"
				    +"(SELECT DISTINCT tel FROM project_food WHERE fno=project_jjim.no) "
				    +"FROM project_jjim "
				    +"WHERE id=?";
		  ps=conn.prepareStatement(sql);
		  ps.setString(1, id);
		  ResultSet rs=ps.executeQuery();
		  while(rs.next())
		  {
			  JjimVO vo=new JjimVO();
			  vo.setJno(rs.getInt(1));
			  vo.setNo(rs.getInt(2));
			  vo.setName(rs.getString(3));
			  String poster=rs.getString(4);
			  poster=poster.substring(0,poster.indexOf("^"));
			  poster=poster.replace("#", "&");
			  vo.setPoster(poster);
			  vo.setAddress(rs.getString(5));
			  vo.setTel(rs.getString(6));
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
  // JJIM 취소 
  public void jjimDelete(int jno)
  {
	  try
	   {
		  conn=CreateConnection.getConnection();
		  String sql="DELETE FROM project_jjim "
				    +"WHERE jno=?";
		  ps=conn.prepareStatement(sql);
		  ps.setInt(1, jno);
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
}