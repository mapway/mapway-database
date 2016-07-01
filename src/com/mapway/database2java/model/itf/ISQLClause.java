package com.mapway.database2java.model.itf;

public interface ISQLClause {

   
    public String getTableSQL();

    public String getPKSQL();

    public String getSequence_SQL();

    public String getViewSQL();

    public String getProcedureSQL();
}
