package com.mapway.database2java.model.oracle;

import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;

import com.mapway.database2java.database.AccessBase;
import com.mapway.database2java.database.MyPool;
import com.mapway.database2java.model.itf.ISQLClause;

public class Oracle_SQLClause implements ISQLClause {

	public Oracle_SQLClause(String database) {
		// TODO Auto-generated constructor stub
	}

	
	public String getTableSQL() {

		return "select c.comments ,a.table_name ,a.column_name ,a.data_type,a.data_length , a.column_id ,a.nullable,b.comments from"
				+ " user_tables d,"
				+ "(select * from user_tab_columns order by table_name ,column_id)a,"
				+ "(select * from user_col_comments) b,"
				+ "(select * from user_tab_comments) c"
				+ " where d.table_name=a.table_name and a.table_name=b.table_name and a.column_name=b.column_name and a.table_name=c.table_name";
	}

	public static void main(String[] args) {
		Oracle_SQLClause app = new Oracle_SQLClause("");
		MyPool pool = MyPool.getInstance("Oracle");
		AccessBase a = new AccessBase(pool);
		try {
			Result r = a.execute(app.getProcedureSQL());
			a.log(a.resultToString(r));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String getPKSQL() {
		String sql = "select m.table_name,m.column_name from "
				+ "(SELECT * FROM USER_CONS_COLUMNS) m, "
				+ "(select * from user_constraints where CONSTRAINT_TYPE='P') n "
				+ "where m.constraint_name=n.constraint_name";

		return sql;
	}

	public String getProcedureSQL() {
		return "select decode(a.procedure_name,null,'DefaultPackage',a.object_name) pack ,decode (a.procedure_name ,null,a.object_name,a.procedure_name) proname,b.argument_name argname,b.position  seq ,b.in_out  property ,b.data_type  dt from user_procedures a , "
				+ "(select * from user_arguments) b "
				+ "where b.object_name = a.procedure_name or b.object_name = a.object_name order by pack, proname,seq";
	}

	public String getViewSQL() {
		return "select c.comments ,a.table_name ,a.column_name ,a.data_type,a.data_length , a.column_id ,a.nullable,b.comments from "
				+ " user_views d,"
				+ " (select * from user_tab_columns order by table_name ,column_id) a,"
				+ " (select * from user_col_comments) b,"
				+ " (select * from user_tab_comments) c"
				+ " where d.view_name=a.table_name and a.table_name=b.table_name and a.column_name=b.column_name and a.table_name=c.table_name";
	}

	public String getSequence_SQL() {
		return "select * from user_objects  where object_type='SEQUENCE'";
	}

}
