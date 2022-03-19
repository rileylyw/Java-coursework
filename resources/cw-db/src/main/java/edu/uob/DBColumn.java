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

    public void setColumnNames(ArrayList<String> columnNames) {
        this.columnNames = columnNames;
        if(!columnNames.get(0).equalsIgnoreCase("id")) {
            this.columnNames.add(0, "id");
        }
    }
}
