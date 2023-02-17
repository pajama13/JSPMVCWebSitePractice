package com.sist.dao;
import java.util.*;
import java.sql.*;
import javax.naming.*;
import javax.sql.*;
//DAO마다 공통 사용되는 부분은 클래스화 시키기 (+static줘서 접근 가능하게 하기)
public class CreateConnection {
	private static Connection conn;
	
	public static Connection getConnection()
	{
		try
		{
			Context init=new InitialContext();
			Context c=(Context)init.lookup("java://comp/env");
			DataSource ds=(DataSource)c.lookup("jdbc/oracle");
			conn=ds.getConnection();
		}catch(Exception ex) {}
		return conn;
	}
	public static void disConnection(Connection conn,PreparedStatement ps)
	{
		try
		{
			if(ps!=null) ps.close();
			if(conn!=null) conn.close();
		}catch(Exception ex) {}
		
	}
}
