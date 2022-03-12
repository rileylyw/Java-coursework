package edu.uob;

import java.util.ArrayList;

public class DB {
    private ArrayList<DBTable> tables = new ArrayList<>();

    public DB(){
//        tables.add(table);
    }

    public void setTables(DBTable table){
        tables.add(table);
    }

    public ArrayList<DBTable> getTables() {
        return tables;
    }

}
