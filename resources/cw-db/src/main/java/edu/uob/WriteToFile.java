package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteToFile {

    public String displayTableToClient(DBTable table, ArrayList<String> attributeList){
        StringBuilder str = new StringBuilder();
        for (String col: attributeList) { //column list
            str.append(col);
            str.append("\t");
        }
        str.append("\n");
        ArrayList<HashMap<String, String>> rows = table.getAttributeValues();
            for (HashMap map : rows) {
                for(String col: attributeList) {
                    if (map.containsKey(col)){
                        str.append(map.get(col));
                        str.append("\t");
                    }
                }
                str.append("\n");
            }
        String s = str.toString();
        return s;
    }

    public void writeAttribListToFile(String DBName, String tableName, DBTable table)
            throws IOException {
        File file = new File("../" +DBName+"/"+tableName+".tab");
        System.out.println(file);
        if(!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        ArrayList<String> columns = table.getAttributeList();
        ArrayList<HashMap<String, String>> rows = table.getAttributeValues();
        for (int i = 0; i < columns.size(); i++) { //cols
            bw.write(columns.get(i));
            bw.write("\t");
        }
        bw.write("\n");
        if(rows!=null) {
            for (HashMap value : rows) { //rows
                for (int i = 0; i < columns.size(); i++) {
                    if(value.get(columns.get(i))!=null) {
                        bw.write((String) value.get(columns.get(i)));
                        bw.write("\t");
                    }
                }
                bw.write("\n");
            }
        }
        bw.flush();
        bw.close();
    }
}
