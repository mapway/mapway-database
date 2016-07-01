package com.mapway.database2java.model.base;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import com.mapway.Util;
import com.mapway.database2java.database.AccessBase;
import com.mapway.database2java.database.IConnectionPool;
import com.mapway.database2java.model.itf.ISQLClause;
import com.mapway.database2java.model.itf.ISchema;
import com.mapway.database2java.model.schema.ITable;
import com.mapway.database2java.model.schema.Packages;
import com.mapway.database2java.model.schema.Sequences;
import com.mapway.database2java.model.schema.Tables;
import com.mapway.database2java.model.schema.View;
import com.mapway.database2java.model.schema.Views;

public class NetSchemaBase implements ISchema {
 
	public AccessBase a = null;

	Tables tables = null;

	Sequences sequences = null;

	Packages packages = null;

	Views views = null;

	ISQLClause sqlClause = null;

	Configure m_configure = null;

	Object[][] pks = null;

	public NetSchemaBase(IConnectionPool pool, Configure configure) {
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
	 * ����SQL����ṩ��
	 * 
	 * @param c
	 */
	public void setSQLClause(ISQLClause c) {
		sqlClause = c;
	}

	/**
	 * ��ȡ��ǰ����
	 * 
	 * @return
	 */
	public Configure getConfigure() {
		return m_configure;
	}

	/**
	 * �� tn�е���cn �Ƿ�Ϊ����
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
			if (tna.equals(tn) && cna.equals(cn)) {
				b = true;
				break;
			}
		}
		return b;
	}

	/**
	 * ��ȡSCHEMA���
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
		java.io.FileWriter fw = null;
		try {
			fw = new java.io.FileWriter(path + "\\" + fn);
			fw.write(s);
			fw.flush();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String exportJSONTools(Configure conf) {
		StringBuilder sb = new StringBuilder();
		out(sb, getCopyright());
		writeJSONTools(sb, conf);
		writeToFile(conf.getNetFilePath(), "JSONHelper.cs", sb.toString());
		return "";
	}

	private void writeJSONTools(StringBuilder sb, Configure conf) {
		
		StringBuilder sb1=Util.readFromFile(new File("d:\\workspace2008\\database2java4\\src\\template\\json.txt"));
		String s=sb1.toString();
		s=s.replace("${package}", conf.getPackage());
		sb.append(s);
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
		out(sb, "namespace " + conf.getPackage() + "{");
		out(sb, "\tusing System.Data.OleDb;");
		
		out(sb, "\tpublic interface IConnectionPool {");
		out(sb, "\t\t OleDbConnection getConnection();");
		out(sb, "\t\t void releaseConnection(OleDbConnection con);");
		out(sb, "\t}");
		out(sb, "}");
	
		writeToFile(conf.getNetFilePath(), "IConnectionPool.cs", sb.toString());
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
		out(sb, "* �ǵ������ ���� dispose �ͷ���Դ");
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
		out(sb, "* ��ȡִ�еĽ��");
		out(sb, "* ");
		out(sb, "* @return");
		out(sb, "*/");
		out(sb, "public ResultSet getResultSet()");
		out(sb, "{");
		out(sb, "return this.result;");
		out(sb, "}");
		out(sb, "}");

		writeToFile(conf.getNetFilePath(), "ExecuteResult.cs", sb.toString());

	}

	public void exportAccessBase(Configure conf) {
		StringBuilder sb=new StringBuilder();
		sb.append(getCopyright());
		StringBuilder sb1=Util.readFromFile(new File("d:\\workspace2008\\database2java4\\src\\template\\accessbase.txt"));
		String s=sb1.toString();
		s=s.replace("${package}", conf.getPackage());
		sb.append(s);
		writeToFile(conf.getNetFilePath(), "AccessBase.cs", sb.toString());
		
		
		sb=new StringBuilder();
		sb.append(getCopyright());
		sb1=Util.readFromFile(new File("d:\\workspace2008\\database2java4\\src\\template\\MyPool.txt"));
		s=sb1.toString();
		s=s.replace("${package}", conf.getPackage());
		sb.append(s);
		writeToFile(conf.getNetFilePath(), "MyPool.cs", sb.toString());

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
	
	public void exportSpringConfigure(Configure conf)
	{}

	@Override
	public void exportDwrConfigure(Configure conf) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportGwtModule(Configure conf)
	{
	    // TODO Auto-generated method stub
	    
	}

	@Override
	public void exportJavaBean(Configure conf) {
		// TODO Auto-generated method stub
		
	}
}
