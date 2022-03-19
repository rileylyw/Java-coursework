package edu.uob;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DBColumn {
    private ArrayList<String> attributeList;

//    public DBColumn(String tableName, ArrayList<String> columnNames){
    public DBColumn(){
//        this.columnNames = columnNames;
    }

    public ArrayList<String> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(ArrayList<String> columnNames) {
        this.attributeList = columnNames;
        if(!columnNames.get(0).equalsIgnoreCase("id")) {
            this.attributeList.add(0, "id");
        }
    }

    public void addColumn(String columnToAdd){
        attributeList.add(columnToAdd);
    }

    public void dropColumn(String columnToDrop){
        attributeList.removeIf(col -> Objects.equals(col, columnToDrop));
    }
}
