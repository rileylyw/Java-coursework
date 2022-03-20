package edu.uob;

import java.util.ArrayList;
import java.util.HashMap;

public class DBRow {
    private int id;
    private ArrayList<HashMap<String, String>> attributeValues; //list of hashmap

    public DBRow(){
    }

    public ArrayList<HashMap<String, String>> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(ArrayList<HashMap<String, String>> attributeValues) {
        if(attributeValues!=null) {
            this.attributeValues = attributeValues;
            this.id = attributeValues.size();
        }
    }

    public String setId() {
        this.id++;
        return Integer.toString(this.id);
    }

    public int getId() {
        return id;
    }

    public void addAttributeValues(ArrayList<String> attributeValuesToAdd,
                                   ArrayList<String> attributeList){
        HashMap<String, String> pair = new HashMap<>();
        pair.put(attributeList.get(0), setId());
        for(int i=0; i<attributeValuesToAdd.size(); i++){
            pair.put(attributeList.get(i+1), attributeValuesToAdd.get(i));
        }
        attributeValues.add(pair);
    }

}
