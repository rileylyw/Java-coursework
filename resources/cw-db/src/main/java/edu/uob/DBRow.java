package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class DBRow {
//    private int id;
    private ArrayList<HashMap<String, String>> attributeValues; //list of hashmap

//    public DBRow(ArrayList<HashMap<String, String>> attributeValues){
    public DBRow(){
//        this.attributeValues = attributeValues;
    }

    public ArrayList<HashMap<String, String>> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<HashMap<String, String>> attributeValues) {
        this.attributeValues = attributeValues;
    }

}