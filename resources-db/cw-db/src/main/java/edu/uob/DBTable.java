package edu.uob;
import java.util.ArrayList;
import java.util.HashMap;

public class DBTable {
    private DBRow rows = new DBRow();
    private DBColumn columns = new DBColumn();

    public DBTable(ArrayList<String> attributeList,
                   ArrayList<HashMap<String, String>> attributeValues){
        columns.setAttributeList(attributeList);
        rows.setAttributeValues(attributeValues);
    }

    public void addColumn(String columnToAdd){
        columns.addColumn(columnToAdd);
    }

    public boolean dropColumn(String columnToDrop){
        return columns.dropColumn(columnToDrop);
    }

    public ArrayList<HashMap<String, String>> getAttributeValues(){
        return rows.getAttributeValues();
    }

    public ArrayList<String> getAttributeList(){
        return columns.getAttributeList();
    }

    public void addAttributeValues(ArrayList<String> attributeValuesToAdd,
                                   ArrayList<String> attributeList){
        rows.addAttributeValues(attributeValuesToAdd, attributeList);
    }

    public void setAttributeValuesWithoutID(ArrayList<HashMap<String, String>> attributeValues) {
        rows.setAttributeValuesWithoutID(attributeValues);
    }
}
