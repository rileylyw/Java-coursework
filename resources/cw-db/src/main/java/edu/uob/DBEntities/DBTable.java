package edu.uob.DBEntities;

import java.util.ArrayList;

public class DBTable {
    private String databaseName;
    private String tableName;
    private ArrayList<DBRow> rows;
    private ArrayList<DBColumn> columns;
    private int numberOfRows;
    private int numberOfColumns;

    public DBTable(){

    }
}
