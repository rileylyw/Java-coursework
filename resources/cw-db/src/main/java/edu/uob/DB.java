package edu.uob;

import java.util.ArrayList;

public class DB {
    private ArrayList<DBTable> tables;

    public DB(DBTable table){
        tables.add(table);
    }

    public ArrayList<DBTable> getTables() {
        return tables;
    }

}
