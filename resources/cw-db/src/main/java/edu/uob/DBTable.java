package edu.uob;

import edu.uob.ReadInFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DBTable {
    private String tableName;
    private DBRow row;
    private DBColumn column;

    public DBTable(){

    }

    public void storeFileToTable(String tableName, String tableFileName) throws IOException {
        ReadInFile readInFile = new ReadInFile();
        readInFile.readInFile(tableFileName);
        this.tableName = tableName;
        column = new DBColumn(tableName, readInFile.getAttributeList());
        row = new DBRow(readInFile.getAttributeValues());

//        columns = new ArrayList<>();
//        columns.add(new DBColumn(tableName, readInFile.getAttributeList()));
//        rows = new ArrayList<>();
//        rows.add(new DBRow(readInFile.getAttributeValues()));

//        System.out.println(rows.get(0).getAttributeValues().get(0).get(0));
//        System.out.println(columns.get(0).getColumnNames());
//        System.out.println(column.getTableName());
    }


}
