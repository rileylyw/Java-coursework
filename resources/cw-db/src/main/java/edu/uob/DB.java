package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class DB {
    private String DBName;
    private HashMap<String, DBTable> tables = new HashMap<>(); //tablename

    public DB(String DBName){
        this.DBName = DBName;
//        tables.add(table);
    }


    public DBTable getTable(String tableName){
        return tables.get(tableName);
    }

    public HashMap<String, DBTable> getTables() {
        return tables;
    }

//    public void addTable(DBTable tableToAdd){
//        tables.put(tableToAdd.getTableName(), tableToAdd);
//    }

    public String getDBName() {
        return DBName;
    }

}
