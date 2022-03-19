package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteToFile {
//    public void writeToFile(DBTable table) throws IOException{
//        String path = "../" + table.getTableName() + ".tab";
//        File tableFile = new File(path);
//        if(tableFile.exists()) {
//            try {
//                BufferedWriter bw = new BufferedWriter(new FileWriter(tableFile));
//                for (int i = 0; i < table.getAttributeList().size(); i++) { //cols
//                    bw.write(table.getAttributeList().get(i));
//                    bw.write(" ");
//                }
//                bw.write("\n");
//                for(HashMap value: table.getAttributeValues()){ //rows
//                    for (int i = 0; i < table.getAttributeList().size(); i++) {
//                        bw.write((String) value.get(table.getAttributeList().get(i)));
//                        bw.write(" ");
//                    }
//                    bw.write("\n");
//                }
//                bw.flush();
//                bw.close();
//            }
//            catch (IOException e){
//                e.printStackTrace();
//            }
//        }
//        else{
//            System.out.println("file doesn't exist");
//        }
//    }

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
