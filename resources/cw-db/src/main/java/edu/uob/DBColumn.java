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
        attributeList = new ArrayList<>();
        if(!columnNames.get(0).equals("id")) {
            this.attributeList.add("id");
        }
        this.attributeList.addAll(columnNames);
    }

    public void setAttributeListWithoutID(ArrayList<String> columnNames) {
        attributeList = new ArrayList<>();
//        if(!columnNames.get(0).equals("id")) {
//            this.attributeList.add("id");
//        }
        this.attributeList.addAll(columnNames);
    }


    public void addColumn(String columnToAdd){
        attributeList.add(columnToAdd);
    }

    public boolean dropColumn(String columnToDrop){
        if(columnToDrop.equalsIgnoreCase("id")||!attributeList.contains(columnToDrop)) {
            return false;
        }
        attributeList.removeIf(col -> Objects.equals(col, columnToDrop));
        return true;
    }
}
