package edu.uob;

import edu.uob.ReadInFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBTable {
    private String tableName;
    private DBRow rows;
    private DBColumn columns;

    public DBTable(){

    }

    public void storeFileToTable(String tableName, String tableFileName) throws IOException {
        ReadInFile readInFile = new ReadInFile();
        readInFile.readInFile(tableFileName);
        this.tableName = tableName;
        columns = new DBColumn();
        columns.setColumnNames(tableName, readInFile.getAttributeList());
        rows = new DBRow();
        rows.setAttributeValues(readInFile.getAttributeValues());
        for(HashMap value: rows.getAttributeValues()) {
            value.replace("Age", String.valueOf(Math.round(Math.random() * (50 - 1 + 1) + 1)));
//            rows.updateAttributeValues("Age", String.valueOf(Math.round(Math.random() * (50 - 1 + 1) + 1)));
        }
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
