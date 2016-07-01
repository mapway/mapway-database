
package com.mapway.database2java.database;

import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

public class AccessBase {

	protected IConnectionPool ConnectionPool;

	public final static String ERROR_DATABASE = "Database Access Error";

	
	public IConnectionPool getConnectionPool() {
		return this.ConnectionPool;
	}

	
	public void setConnectionPool(IConnectionPool pool) {
		this.ConnectionPool = pool;
	}

	public AccessBase(IConnectionPool pool) {
		this.ConnectionPool = pool;
	}

	public Result execute(String sql) throws SQLException {
		Result r = null;
		Connection con = this.ConnectionPool.getConnection();
		if (con == null)
			return null;
		Statement statement = null;
		try {
			statement = con.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			r = ResultSupport.toResult(rs);
			rs.close();
			if (statement != null)
				statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		} finally {
			this.ConnectionPool.releaseConnection(con);
		}
		return r;
	}

	public int findCount(String src, char match) {

		int count = 0;
		for (int index = 0; index < src.length(); index++) {
			char c = src.charAt(index);
			if (c == match) {
				count++;
			}
		}
		return count;
	}

	public void log(String info) {
		System.out.println(info);
	}

	public String stringFromClob(Clob clob) throws SQLException {
		if (clob == null)
			return "";
		Reader clobStream = null;
		try {
			clobStream = clob.getCharacterStream();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}

		StringBuilder suggestions = new StringBuilder();

		int nchars = 0; // Number of characters read

		char[] buffer = new char[10];

		try {
			while ((nchars = clobStream.read(buffer)) != -1)
				suggestions.append(buffer, 0, nchars);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SQLException();
		}
		try {
			clobStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SQLException();
		}
		return suggestions.toString();
	}

	public ArrayList<String> m_SQLs;

	/*
	 * 
	 */
	public void addSQL(String sql) {
		if (sql.equals(""))
			return;
		if (m_SQLs == null)
			m_SQLs = new ArrayList<String>();
		m_SQLs.add(sql);
	}

	/*
	 * 
	 */
	public int[] executeBatch() throws SQLException {
		int[] rs = new int[0];
		Connection conn = this.ConnectionPool.getConnection();
		if (conn == null)
			return rs;
		Statement statement = null;
		boolean defaultCommit;
		defaultCommit = conn.getAutoCommit();
		try {
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			int c = 0;
			if (m_SQLs != null && m_SQLs.size() > 0) {
				c = m_SQLs.size();
			}
			if (c > 0) {
				for (int i = 0; i < c; i++) {
					statement.addBatch(m_SQLs.get(c));
				}

				rs = statement.executeBatch();
				conn.commit();
			} else {
			}
		} catch (Exception e) {

			conn.rollback();
			e.printStackTrace();

		} finally {
			conn.setAutoCommit(defaultCommit);
			if (statement != null) {
				statement.clearBatch();
				statement.close();
			}
			alearSQL();
			this.ConnectionPool.releaseConnection(conn);
		}
		return rs;
	}

	/*
	 * 
	 */
	public void alearSQL() {
		if (m_SQLs == null)
			m_SQLs = new ArrayList<String>();
		else
			m_SQLs.clear();
	}
	
	public String resultToString(Result rs)
	{
	  StringBuilder sb=new StringBuilder();
	  String[] cns=rs.getColumnNames();
	  for(int i=0;i<cns.length;i++)
	  {
	    sb.append(cns[i]);
	    sb.append("\t");
	  }
	  
	  Object[][] oos=rs.getRowsByIndex();
	  for(int i=0;i<oos.length;i++)
	  {
	    sb.append("\r\n");
	    Object[] os=oos[i];
	    for(int j=0;j<os.length;j++)
	    {
	      sb.append(objectToString(os[j]));
	      sb.append("\t");
	    }
	    
	  }
	  return sb.toString();
	}
	
	public String objectToString(Object o)
	{
	  if(o==null)
	  {
	    return "----";
	  }
	  else
	  {
	    return o.toString();
	  }
	}
}
