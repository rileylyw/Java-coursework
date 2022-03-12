package edu.uob;

import java.util.ArrayList;
import java.util.Collection;

public class DBColumn {
    private String tableName;
    private ArrayList<String> columnNames;

    public DBColumn(String tableName, ArrayList<String> columnNames){
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public String getTableName() {
        return tableName;
    }
}
