package com.mapway.database2java.model.itf;

import com.mapway.database2java.model.base.Configure;
import com.mapway.database2java.model.schema.ITable;
import com.mapway.database2java.model.schema.Tables;
import com.mapway.database2java.model.schema.View;
import com.mapway.database2java.model.schema.Views;

public interface ISchema {

    /**
     * �ļ�ͷ�İ�Ȩ˵��
     */
    public String getCopyright();

    /**
     * ����ݿ��л�ȡ��ݿ�Ԫ���
     * 
     * @return
     */
    public boolean fetchSchema();

    /**
     * ���JSONHelper��
     * 
     * @param conf
     * @return
     */
    public String exportJSONTools(Configure conf);

    /**
     * �����
     * 
     * @param table
     * @param conf
     * @return
     */
    public String exportTable(ITable table, Configure conf);

    public void exportAccessBase(Configure conf);

    public void exportPoolInterface(Configure conf);

    public Tables getTables();

    public void exportExecuteResult(Configure confbase);

    public Views getViews();

    public void exportViews(View at, Configure confTable);

    public void exportProcedures(Configure confProcedure);

    public void exportSequence(Configure conf);
    
    public void exportSpringConfigure(Configure conf);
    
    public void exportDwrConfigure(Configure conf);
    
    public void exportGwtModule(Configure conf);
    
    public void exportJavaBean(Configure conf);
}
