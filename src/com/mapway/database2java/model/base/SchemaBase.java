package com.mapway.database2java.model.base;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import com.mapway.database2java.database.AccessBase;
import com.mapway.database2java.database.IConnectionPool;
import com.mapway.database2java.model.itf.ISQLClause;
import com.mapway.database2java.model.itf.ISchema;
import com.mapway.database2java.model.schema.Column;
import com.mapway.database2java.model.schema.ITable;
import com.mapway.database2java.model.schema.Packages;
import com.mapway.database2java.model.schema.Sequences;
import com.mapway.database2java.model.schema.Table;
import com.mapway.database2java.model.schema.Tables;
import com.mapway.database2java.model.schema.View;
import com.mapway.database2java.model.schema.Views;

public class SchemaBase implements ISchema {

	public AccessBase a = null;

	Tables tables = null;

	Sequences sequences = null;

	Packages packages = null;

	Views views = null;

	ISQLClause sqlClause = null;

	Configure m_configure = null;

	Object[][] pks = null;

	public SchemaBase(IConnectionPool pool, Configure configure) {
		a = new AccessBase(pool);
		tables = new Tables();
		sequences = new Sequences();
		packages = new Packages();
		views = new Views();
		m_configure = configure;
	}

	public boolean fetchSchema() {
		Result r;
		try {
			r = a.execute(this.getSQLClause().getPKSQL());
			pks = r.getRowsByIndex();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	String copyright = "\r\n<pre>\r\n\r\n"
			+ "           =============================================================\r\n"
			+ "           -   ____ _  _ ____ _  _ ____  _ _ ____ _  _ ____ _  _ ____  -\r\n"
			+ "           -    __] |__| |__| |\\ | | __  | | |__| |\\ | [__  |__| |___  -\r\n"
			+ "           -   [___ |  | |  | | \\| |__| _| | |  | | \\| ___] |  | |___  -\r\n"
			+ "           -           http://hi.baidu.com/zhangjianshe                -\r\n"
			+ "           ============================================================="
			+ "\r\n\r\n</pre>\r\n";

	public String getHeader() {
		return copyright;
	}

	/**
	 * 设置SQL语句提供器
	 * 
	 * @param c
	 */
	public void setSQLClause(ISQLClause c) {
		sqlClause = c;
	}

	/**
	 * 获取当前配置
	 * 
	 * @return
	 */
	public Configure getConfigure() {
		return m_configure;
	}

	/**
	 * 表 tn中的列cn 是否为主键
	 * 
	 * @param tn
	 * @param cn
	 * @return
	 */
	public boolean isPK(String tn, String cn) {
		boolean b = false;
		for (int i = 0; i < pks.length; i++) {
			String tna = (String) pks[i][0];
			String cna = (String) pks[i][1];
			String pktype = (String) pks[i][2];
			if (tna.equals(tn) && cna.equals(cn)
					&& (pktype.compareToIgnoreCase("PRIMARY") == 0)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * 获取SCHEMA语句
	 * 
	 * @return
	 */
	public ISQLClause getSQLClause() {
		return sqlClause;
	}

	public Tables getTables() {
		return tables;
	}

	public Sequences getSequences() {
		return sequences;
	}

	public Packages getPackages() {
		return packages;
	}

	public Views getViews() {
		return views;
	}

	public Object[][] getPKS() {
		return pks;
	}

	public void writeToFile(String path, String fn, String s) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			OutputStreamWriter out = new OutputStreamWriter(
					new FileOutputStream(path + "\\" + fn), "UTF-8");
			out.write(s);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String exportJSONTools(Configure conf) {
		StringBuilder sb = new StringBuilder();
		out(sb, getCopyright());
		writeJSONTools(sb, conf);
		writeToFile(conf.getFilePath(), "JSONHelper.java", sb.toString());
		return "";
	}

	private void writeJSONTools(StringBuilder sb, Configure conf) {
		out(sb, "package " + conf.getPackage() + ";");
		out(sb, "public class JSONHelper");
		out(sb, "{");
		out(sb,
				"private static final char[] QUOTE_ENCODE = \"&quot;\".toCharArray();");
		out(sb,
				"                                                                  ");
		out(sb,
				"	private static final char[] AMP_ENCODE = \"&amp;\".toCharArray(); ");
		out(sb,
				"                                                                  ");
		out(sb,
				"	private static final char[] LT_ENCODE = \"&lt;\".toCharArray();   ");
		out(sb,
				"                                                                  ");
		out(sb,
				"	private static final char[] GT_ENCODE = \"&gt;\".toCharArray();   ");
		out(sb,
				"                                                                  ");
		out(sb,
				"	public static final String escapeForXML(String string) {        ");
		out(sb,
				"		if (string == null) {                                         ");
		out(sb,
				"			return null;                                                ");
		out(sb,
				"		}                                                             ");
		out(sb,
				"		char ch;                                                      ");
		out(sb,
				"		int i = 0;                                                    ");
		out(sb,
				"		int last = 0;                                                 ");
		out(sb,
				"		char[] input = string.toCharArray();                          ");
		out(sb,
				"		int len = input.length;                                       ");
		out(sb,
				"		StringBuffer out = new StringBuffer((int) (len * 1.3));       ");
		out(sb,
				"		for (; i < len; i++) {                                        ");
		out(sb,
				"			ch = input[i];                                              ");
		out(sb,
				"			if (ch > '>') {                                             ");
		out(sb,
				"				continue;                                                 ");
		out(sb,
				"			} else if (ch == '<') {                                     ");
		out(sb,
				"				if (i > last) {                                           ");
		out(sb, "					out.append(input, last, i - last);                      ");
		out(sb,
				"				}                                                         ");
		out(sb,
				"				last = i + 1;                                             ");
		out(sb,
				"				out.append(LT_ENCODE);                                    ");
		out(sb,
				"			} else if (ch == '&') {                                     ");
		out(sb,
				"				if (i > last) {                                           ");
		out(sb, "					out.append(input, last, i - last);                      ");
		out(sb,
				"				}                                                         ");
		out(sb,
				"				last = i + 1;                                             ");
		out(sb,
				"				out.append(AMP_ENCODE);                                   ");
		out(sb,
				"			} else if (ch == '\"') {                                     ");
		out(sb,
				"				if (i > last) {                                           ");
		out(sb, "					out.append(input, last, i - last);                      ");
		out(sb,
				"				}                                                         ");
		out(sb,
				"				last = i + 1;                                             ");
		out(sb,
				"				out.append(QUOTE_ENCODE);                                 ");
		out(sb,
				"			}                                                           ");
		out(sb,
				"		}                                                             ");
		out(sb,
				"		if (last == 0) {                                              ");
		out(sb,
				"			return string;                                              ");
		out(sb,
				"		}                                                             ");
		out(sb,
				"		if (i > last) {                                               ");
		out(sb,
				"			out.append(input, last, i - last);                          ");
		out(sb,
				"		}                                                             ");
		out(sb,
				"		return out.toString();                                        ");
		out(sb,
				"	}                                                               ");

		out(sb, "/**                                         ");
		out(sb, "	 * @param s                               ");
		out(sb, "	 * @return                                ");
		out(sb, "	 */                                       ");
		out(sb, "	public static String escape(String s){    ");
		out(sb, "		if(s==null)                             ");
		out(sb, "			return null;                          ");
		out(sb, "		StringBuffer sb=new StringBuffer();     ");
		out(sb, "		for(int i=0;i<s.length();i++){          ");
		out(sb, "			char ch=s.charAt(i);                  ");
		out(sb, "			switch(ch){                           ");
		out(sb, "			case '\"':                             ");
		out(sb, "				sb.append(\"\\\\\\\"\");                  ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\\\':                            ");
		out(sb, "				sb.append(\"\\\\\\\\\");                  ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\b':                            ");
		out(sb, "				sb.append(\"\\\\b\");                   ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\f':                            ");
		out(sb, "				sb.append(\"\\\\f\");                   ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\n':                            ");
		out(sb, "				sb.append(\"\\\\n\");                   ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\r':                            ");
		out(sb, "				sb.append(\"\\\\r\");                   ");
		out(sb, "				break;                              ");
		out(sb, "			case '\\t':                            ");
		out(sb, "				sb.append(\"\\\\t\");                   ");
		out(sb, "				break;                              ");
		out(sb, "			case '/':                             ");
		out(sb, "        				sb.append(\"\\\\/\");                    ");
		out(sb, "        				break;                               ");
		out(sb, "        			default:                               ");
		out(sb, "        				if(ch>='\\u0000' && ch<='\\u001F'){      ");
		out(sb, "        					String ss=Integer.toHexString(ch); ");
		out(sb, "        					sb.append(\"\\\\u\");                ");
		out(sb, "        					for(int k=0;k<4-ss.length();k++){    ");
		out(sb, "        						sb.append('0');                  ");
		out(sb, "        					}                                  ");
		out(sb, "        					sb.append(ss.toUpperCase());         ");
		out(sb, "        				}                                    ");
		out(sb, "        				else{                                ");
		out(sb, "        					sb.append(ch);                       ");
		out(sb, "        				}                                    ");
		out(sb, "        			}                                      ");
		out(sb, "        		}//for                                   ");
		out(sb, "        		return sb.toString();                    ");
		out(sb, "        	}                                         ");
		out(sb, "}");
	}

	public void out(StringBuilder sb, String s) {
		sb.append(s + "\r\n");
	}

	public String exportTable(ITable table, Configure conf) {
		// TODO Auto-generated method stub
		return null;
	}

	public void exportPoolInterface(Configure conf) {
		StringBuilder sb = new StringBuilder();
		sb.append(getCopyright());
		out(sb, "package " + conf.getPackage() + ";\r\n");
		out(sb, "import java.sql.Connection;\r\n");

		out(sb, "public interface IConnectionPool {");
		out(sb, "\tpublic Connection getConnection();");
		out(sb, "\tpublic void releaseConnection(Connection con);");
		out(sb, "}");
		writeToFile(conf.getFilePath(), "IConnectionPool.java", sb.toString());

		InputStream ins = getClass()
				.getResourceAsStream("/template/MyPool.txt");
		String txt = readUTF8TextFile(ins);
		txt = txt.replace("${package}", conf.getPackage());
		writeToFile(conf.getFilePath(), "MyPool.java", txt);
		try {
			ins.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String readUTF8TextFile(InputStream ins) {

		String txt = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] bytes = new byte[1024];
			int len = ins.read(bytes);
			while (len > 0) {
				out.write(bytes, 0, len);
				len = ins.read(bytes);
			}
			ins.close();
			txt = out.toString("UTF-8");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txt;
	}

	public static String readUTF8TextFile(String fileName) {
		byte[] bytes = ReadFromFile(fileName);
		String str = "";
		try {
			str = new String(bytes, "UTF-8");
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static String readTextFile(String fileName, String encoding) {
		byte[] bytes = ReadFromFile(fileName);
		String str = "";
		try {
			str = new String(bytes, encoding);
			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	public static byte[] ReadFromFile(String fileName) {

		File ff = new java.io.File(fileName);
		long filelength = ff.length();
		byte[] code = new byte[(int) filelength];

		InputStream inStream;
		try {
			inStream = new FileInputStream(fileName);
			inStream.read(code);
			inStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}

	public void exportExecuteResult(Configure conf) {
		System.out.println("Export ExecuteResult.....");
		StringBuilder sb = new StringBuilder();
		sb.append(getCopyright());
		out(sb, "package " + conf.getPackage() + ";\r\n");
		out(sb, "import java.sql.Connection;");
		out(sb, "import java.sql.ResultSet;");
		out(sb, "import java.sql.SQLException;");
		out(sb, "import java.sql.Statement;");

		out(sb, "public class ExecuteResult {");

		out(sb, "public ResultSet result;");

		out(sb, "public Statement statement;");

		out(sb, "Connection       con;");

		out(sb, "IConnectionPool  pool;");

		out(sb, "/**");
		out(sb, "* 记得用完后 调用 dispose 释放资源");
		out(sb, "* ");
		out(sb, "* @param rs");
		out(sb, "* @param stm");
		out(sb, "*/");
		out(sb,
				"public ExecuteResult(IConnectionPool pool, Connection con, ResultSet rs,");
		out(sb, "Statement stm) {");
		out(sb, "this.con = con;");
		out(sb, "this.pool = pool;");
		out(sb, "result = rs;");
		out(sb, "statement = stm;");
		out(sb, "}");

		out(sb, "public void dispose()");
		out(sb, "{");
		out(sb, "try");
		out(sb, "{");
		out(sb, "if (result != null )");
		out(sb, "{");
		out(sb, "result.close();");
		out(sb, "result = null;");
		out(sb, "}");
		out(sb, "if (statement != null )");
		out(sb, "{");
		out(sb, "statement.close();");
		out(sb, "statement = null;");
		out(sb, "}");
		out(sb, "} catch (SQLException e)");
		out(sb, "{");
		out(sb, "e.printStackTrace();");
		out(sb, "} finally");
		out(sb, "{");
		out(sb, "if (this.pool != null)");
		out(sb, "{");
		out(sb, "this.pool.releaseConnection(con);");
		out(sb, "}");
		out(sb, "}");
		out(sb, "}");

		out(sb, "/**");
		out(sb, "* 获取执行的结果");
		out(sb, "* ");
		out(sb, "* @return");
		out(sb, "*/");
		out(sb, "public ResultSet getResultSet()");
		out(sb, "{");
		out(sb, "return this.result;");
		out(sb, "}");
		out(sb, "}");

		writeToFile(conf.getFilePath(), "ExecuteResult.java", sb.toString());

	}

	public void exportAccessBase(Configure conf) {
		StringBuilder sb = new StringBuilder();
		sb.append(getCopyright());
		out(sb, "package " + conf.getPackage() + ";\r\n");
		out(sb, "import java.io.IOException;");
		out(sb, "import java.io.Reader;");
		out(sb, "import java.io.InputStream;");
		out(sb, "import java.sql.Clob;");
		out(sb, "import java.sql.Blob;");
		out(sb, "import java.sql.Connection;");
		out(sb, "import java.sql.ResultSet;");
		out(sb, "import java.sql.PreparedStatement;");
		out(sb, "import java.sql.SQLException;");
		out(sb, "import java.sql.Statement;");
		out(sb, "import java.util.ArrayList;");
		out(sb, "import javax.servlet.jsp.jstl.sql.Result;");
		out(sb, "import javax.servlet.jsp.jstl.sql.ResultSupport;\r\n");
		out(sb, "public class AccessBase {\r\n");
		out(sb, "	protected IConnectionPool ConnectionPool;\r\n");
		out(sb,
				"	public final static String ERROR_DATABASE = \"Database Access Error\";");
		out(sb, "	/**");
		out(sb, "	 * 获取数据库连接池");
		out(sb, "	 * @return IConnectionPool");
		out(sb, "	 */");
		out(sb, "	public IConnectionPool getConnectionPool() {");
		out(sb, "		return this.ConnectionPool;");
		out(sb, "	}\r\n");
		out(sb, "	/**");
		out(sb, "	 * 设置数据库连接池");
		out(sb, "	 * @param IConnectionPool");
		out(sb, "	 */");
		out(sb, "	public void setConnectionPool(IConnectionPool pool) {");
		out(sb, "		this.ConnectionPool = pool;");
		out(sb, "	}\r\n");
		out(sb, "	public AccessBase(IConnectionPool pool) {");
		out(sb, "		this.ConnectionPool = pool;");
		out(sb, "	}\r\n");
		out(sb, "	public Result execute(String sql) throws SQLException {");
		out(sb, "		Result r = null;");
		out(sb, "		Connection con = this.ConnectionPool.getConnection();");
		out(sb, "		if (con == null)");
		out(sb, "			return null;");
		out(sb, "		Statement statement = null;");
		out(sb, "		try {");
		out(sb, "			statement = con.createStatement();");
		out(sb, "			ResultSet rs = statement.executeQuery(sql);");
		out(sb, "			r = ResultSupport.toResult(rs);");
		out(sb, "			rs.close();");
		out(sb, "			if (statement != null)");
		out(sb, "				statement.close();");
		out(sb, "		} catch (SQLException e) {");
		out(sb, "			e.printStackTrace();");
		out(sb, "			throw e;");
		out(sb, "		} finally {");
		out(sb, "			this.ConnectionPool.releaseConnection(con);");
		out(sb, "		}");
		out(sb, "		return r;");
		out(sb, "	}");

		out(sb,
				"	public ExecuteResult execute2Result(String sql) throws SQLException {");
		out(sb, "		Connection con = this.ConnectionPool.getConnection();");
		out(sb, "if (con == null)");
		out(sb, "return null;");
		out(sb, "	PreparedStatement statement = null;");
		out(sb, "	try {");
		out(sb, "		statement = con.prepareStatement(sql);");
		out(sb, "		ResultSet rs = statement.executeQuery();");
		out(sb,
				"				return new ExecuteResult(this.ConnectionPool, con, rs, statement);");
		out(sb, "	} catch (SQLException e) {");
		out(sb, "		e.printStackTrace();");
		out(sb, "		this.ConnectionPool.releaseConnection(con);");
		out(sb, "		return null;");
		out(sb, "	}");
		out(sb, "}");

		out(sb, "	public int findCount(String src, char match) {");
		out(sb, "		int count = 0;");
		out(sb, "		for (int index = 0; index < src.length(); index++) {");
		out(sb, "			char c = src.charAt(index);");
		out(sb, "			if (c == match) {");
		out(sb, "				count++;");
		out(sb, "			}");
		out(sb, "		}");
		out(sb, "		return count;");
		out(sb, "	}\r\n");
		out(sb, "	public void Log(String info) {");
		out(sb, "		System.out.println(info);");
		out(sb, "	}\r\n");
		out(sb,
				"	public static byte[] bytesFromBlob(Blob blob)throws SQLException	{");

		out(sb, "		if(blob==null)return new byte[0];");
		out(sb, "	      byte[] r = new byte[(int) blob.length()];");

		out(sb, "	      InputStream in=blob.getBinaryStream();");

		out(sb, "	          for (int i=0 ; i < blob.length() ; i++) {");
		out(sb, "	              try {");
		out(sb, "					r[i]=(byte)in.read();");
		out(sb, "				} catch (IOException e) {");
		out(sb, "					e.printStackTrace();");
		out(sb, "				}");
		out(sb, "		          }");
		out(sb, "		return r;");
		out(sb, "\t}\t\n");

		out(sb, "public boolean executeDDL(String ddl) throws SQLException {");
		out(sb, "	boolean b = false;");
		out(sb, "	Connection con = this.ConnectionPool.getConnection();");
		out(sb, " if (con == null)");
		out(sb, "		return b;");
		out(sb, "	Statement statement = null;");
		out(sb, "	try {");
		out(sb, "		statement = con.createStatement();");
		out(sb, "		b = statement.execute(ddl);");
		out(sb, "		if (statement != null)");
		out(sb, "			statement.close();");
		out(sb, "	} catch (SQLException e) {");
		out(sb, "			b = false;");
		out(sb, "		e.printStackTrace();");
		out(sb, "	    throw e;");
		out(sb, "	} finally {");
		out(sb, "		this.ConnectionPool.releaseConnection(con);");
		out(sb, "	}");
		out(sb, "	return b;");
		out(sb, "}");

		out(sb,
				"	public static String StringFromClob(Clob clob) throws SQLException {");
		out(sb, "		if (clob == null)");
		out(sb, "			return \"\";");
		out(sb, "		Reader clobStream = null; ");
		out(sb, "		try { ");
		out(sb, "			clobStream = clob.getCharacterStream();");
		out(sb, "		} catch (SQLException e) {");
		out(sb, "			e.printStackTrace();");
		out(sb, "			throw e;");
		out(sb, "		}\r\n");

		out(sb, "		StringBuilder suggestions = new StringBuilder();");
		out(sb, "		int nchars = 0; // Number of characters read  ");
		out(sb, "		char[] buffer = new char[10];                 ");
		out(sb, "		try { ");
		out(sb, "			while ((nchars = clobStream.read(buffer)) != -1)");
		out(sb, "				suggestions.append(buffer, 0, nchars);");
		out(sb, "		} catch (IOException e) { ");
		out(sb, "			e.printStackTrace();    ");
		out(sb, "			throw new SQLException();");
		out(sb, "		}");
		out(sb, "		try { ");
		out(sb, "			clobStream.close();");
		out(sb, "		} catch (IOException e){ ");
		out(sb, "			e.printStackTrace();");
		out(sb, "			throw new SQLException();");
		out(sb, "		}");
		out(sb, "		return suggestions.toString();");
		out(sb, "	}\r\n");
		out(sb, "	public ArrayList<String> m_SQLs;\r\n");
		out(sb, "	/* ");
		out(sb, "	 * @param sql");
		out(sb, "	 */");
		out(sb, "	public void AddSQL(String sql) {");
		out(sb, "		if (sql.equals(\"\"))");
		out(sb, "			return;");
		out(sb, "		if (m_SQLs == null)");
		out(sb, "			m_SQLs = new ArrayList<String>();");
		out(sb, "		m_SQLs.add(sql);");
		out(sb, "	}\r\n");
		out(sb, "	/*      ");
		out(sb, "	 *  @return int[]    ");
		out(sb, "	 */     ");
		out(sb, "	public int[] executeBatch() throws SQLException {");
		out(sb, "		int[] rs = new int[0];");
		out(sb, "		Connection conn = this.ConnectionPool.getConnection();");
		out(sb, "		if (conn == null)");
		out(sb, "			return rs;");
		out(sb, "		Statement statement = null;");
		out(sb, "		boolean defaultCommit;");
		out(sb, "		defaultCommit = conn.getAutoCommit();");
		out(sb, "		try {");
		out(sb, "			conn.setAutoCommit(false);");
		out(sb, "			statement = conn.createStatement();");
		out(sb, "			int c = 0;");
		out(sb, "			if (m_SQLs != null && m_SQLs.size() > 0) {");
		out(sb, "				c = m_SQLs.size();");
		out(sb, "			}");
		out(sb, "			if (c > 0) {");
		out(sb, "				for (int i = 0; i < c; i++) {");
		out(sb, "					statement.addBatch(m_SQLs.get(i));");
		out(sb, "				}");
		out(sb, "          ");
		out(sb, "				rs = statement.executeBatch();");
		out(sb, "				conn.commit();");
		out(sb, "			} else {");
		out(sb, "			}");
		out(sb, "		} catch (Exception e) {");
		out(sb, "			conn.rollback();");
		out(sb, "			e.printStackTrace();");
		out(sb, "          ");
		out(sb, "		} finally {");
		out(sb, "			conn.setAutoCommit(defaultCommit);");
		out(sb, "			if (statement != null) {");
		out(sb, "				statement.clearBatch();");
		out(sb, "				statement.close();");
		out(sb, "			}");
		out(sb, "			ClearSQL();");
		out(sb, "			this.ConnectionPool.releaseConnection(conn);");
		out(sb, "		}");
		out(sb, "		return rs;");
		out(sb, "	}\r\n");
		out(sb, "	/* ");
		out(sb, "	 *   Clear SQL Batch   ");
		out(sb, "	 */");
		out(sb, "	public void ClearSQL() {");
		out(sb, "		if (m_SQLs == null)");
		out(sb, "			m_SQLs = new ArrayList<String>();");
		out(sb, "		else");
		out(sb, "			m_SQLs.clear();");
		out(sb, "	}");
		out(sb, "}");
		writeToFile(conf.getFilePath(), "AccessBase.java", sb.toString());

	}

	public String getCopyright() {
		return null;
	}

	public void exportViews(View at, Configure confTable) {

	}

	public void exportProcedures(Configure confProcedure) {
		// TODO Auto-generated method stub

	}

	public String findPrevPath(String path) {
		int index = path.lastIndexOf('.');
		if (index >= 0) {
			return path.substring(0, index);
		}
		return "";
	}

	public void exportSequence(Configure conf) {

	}

	public void exportSpringConfigure(Configure conf) {
	}

	@Override
	public void exportDwrConfigure(Configure conf) {
		// TODO Auto-generated method stub

	}

	/**
	 * 输出GWT模块配置信息
	 */
	@Override
	public void exportGwtModule(Configure conf) {
		// write GWT module
		InputStream ins = getClass().getResourceAsStream("/template/gwt.txt");
		String txt = readUTF8TextFile(ins);
		writeToFile(conf.getFilePath(), conf.getSchema() + "Data.gwt.xml", txt);
		try {
			ins.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 输出JavaBean
	 */
	@Override
	public void exportJavaBean(Configure conf) {
		String fileName = conf.getFilePath();
		StringBuilder sb;
		Tables tables = getTables();
		for (int i = 0; i < tables.getCount(); i++) {
			ITable t = tables.getAt(i);
			sb = new StringBuilder();

			genNutzBean(conf, t, sb);
			writeToFile(fileName, t.getJavaName() + ".java", sb.toString());
		}
		Tables views = getTables();
		for (int i = 0; i < tables.getCount(); i++) {
			ITable t = views.getAt(i);
			sb = new StringBuilder();

			genNutzBean(conf, t, sb);
			writeToFile(fileName, t.getJavaName() + ".java", sb.toString());
		}
	}

	private void genNutzBean(Configure conf, ITable table, StringBuilder sb) {
		sb.append(this.getCopyright());
		out(sb, "package " + conf.getPackage() + ";\r\n");
		out(sb, "import java.util.Date;\r\n");
		out(sb, "import org.nutz.dao.entity.annotation.*;");
		out(sb, "import java.math.BigDecimal;");
		out(sb, "/**");
		out(sb, " * 数据库表 " + table.getComment() + "<br/>");
		out(sb, " * @author database2java@polatu.cn\r\n");
		out(sb, " * <b>字段列表</b><br/>\r\n");
		for (int i = 0; i < table.getColumns().getCount(); i++) {
			Column col = table.getColumns().getAt(i);
			out(sb, " * " + col.getName() + "   " + col.getComment() + " "
					+ col.getJavaType() + "<br/>");
		}
		out(sb, " */");

		out(sb, "@Table(\"" + table.getName() + "\")\r\n");

		// 查看是否有联合主键
		boolean moreKey = false;

		int count = 0;
		for (int i = 0; i < table.getColumns().getCount(); i++) {
			Column col = table.getColumns().getAt(i);
			if (col.isPK()) {
				count++;
			}
		}

		if (count > 1) {
			StringBuilder pk = new StringBuilder();
			pk.append("@PK({");
			for (int i = 0; i < table.getColumns().getCount(); i++) {
				Column col = table.getColumns().getAt(i);
				if (col.isPK()) {
					if (pk.length() > 5) {
						pk.append(",");
					}
					pk.append("\"").append(col.getName()).append("\"");
				}
			}

			pk.append("})");
			out(sb, pk.toString());
		}

		out(sb, "public class " + table.getJavaName()
				+ " implements java.io.Serializable{");

		out(sb, "public " + table.getJavaName() + "(){}");
		
		
		for (int i = 0; i < table.getColumns().getCount(); i++) {
			Column col = table.getColumns().getAt(i);
			
			out(sb,"\t public final static String FLD_"+col.getName()+"=\""+col.getName()+"\";");
			
			if(count==1)
			{
				if (col.isPK()) {
					if (col.getJavaType().contains("String")) {
						out(sb, "\t@Name");
					} else {
						if(col.isAuto())
						{
							out(sb, "\t@Id");
						}
						else
						{
							out(sb, "\t@Id(auto=false)");
						}
					}
				}
			}
			out(sb, "\tprivate " + col.getJavaType() + " " + col.getName()
					+ ";\r\n");
			out(sb, "\t/**");
			out(sb, "\t * @return " + col.getName() + "  " + col.getComment()
					+ "  " + col.getDatabaseType());
			out(sb, "\t */");
			out(sb, "\tpublic " + col.getJavaType() + " get" + col.getName()
					+ "(){");
			out(sb, "\t\treturn " + col.getName() + ";");
			out(sb, "\t\t}\r\n");
			out(sb, "\t/**");
			out(sb, "\t * @param " + col.getName() + "  " + col.getComment()
					+ "  " + col.getDatabaseType());
			out(sb, "\t */");
			out(sb,
					"\tpublic void set" + col.getName() + "("
							+ col.getJavaType() + " " + col.getName() + "){");
			out(sb, "\t\tthis." + col.getName() + "=" + col.getName() + ";");

			out(sb, "\t\t}\r\n");
		}
		out(sb, "}");
	}
}
