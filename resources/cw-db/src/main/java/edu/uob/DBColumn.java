package edu.uob;

import java.util.ArrayList;
import java.util.Collection;

public class DBColumn {
    private ArrayList<String> columnNames;

//    public DBColumn(String tableName, ArrayList<String> columnNames){
    public DBColumn(){
//        this.columnNames = columnNames;
    }

    public ArrayList<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String tableName, ArrayList<String> columnNames) {
        this.columnNames = columnNames;
    }
}
