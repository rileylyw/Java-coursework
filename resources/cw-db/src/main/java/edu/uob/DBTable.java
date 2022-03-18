package edu.uob;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBTable {
    private String tableName;
    private DBRow rows = new DBRow();
    private DBColumn columns = new DBColumn();

    public DBTable(String tableName, ArrayList<String> attributeList){
        this.tableName = tableName;
        columns.setColumnNames(attributeList);
    }

    public void storeFileToTable(String tableName, String tableFileName) throws IOException {
        ReadInFile readInFile = new ReadInFile();
        readInFile.readInFile(tableFileName);
        this.tableName = tableName;
        columns = new DBColumn();
//        columns.setColumnNames(tableName, readInFile.getAttributeList());
        rows = new DBRow();
        rows.setAttributeValues(readInFile.getAttributeValues());

//        for(HashMap value: rows.getAttributeValues()) { //TODO: updates values
//            value.replace("Age", String.valueOf(Math.round(Math.random() * (50 - 1 + 1) + 1)));
//        }
        System.out.println(rows.getAttributeValues());
    }

//    public void updateTableToFile(String tableFileName) throws IOException{
//        WriteToFile writeToFile = new WriteToFile();
//        writeToFile.writeToFile(tableFileName);
//    }

    public ArrayList<HashMap<String, String>> getAttributeValues(){
        return rows.getAttributeValues();
    }

    public ArrayList<String> getAttributeList(){
        return columns.getColumnNames();
    }

    public String getTableName() {
        return tableName;
    }


}
