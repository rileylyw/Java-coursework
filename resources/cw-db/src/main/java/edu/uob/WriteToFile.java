package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class WriteToFile {
    public void writeToFile(DBTable table) throws IOException{
        String path = "../" + table.getTableName() + ".tab";
        File tableFile = new File(path);
        if(tableFile.exists()) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(tableFile));
                for (int i = 0; i < table.getAttributeList().size(); i++) { //cols
                    bw.write(table.getAttributeList().get(i));
                    bw.write(" ");
                }
                bw.write("\n");
                for(HashMap value: table.getAttributeValues()){ //rows
                    for (int i = 0; i < table.getAttributeList().size(); i++) {
                        bw.write((String) value.get(table.getAttributeList().get(i)));
                        bw.write(" ");
                    }
                    bw.write("\n");
                }
                bw.flush();
                bw.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("file doesn't exist");
        }
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
        for (int i = 0; i < table.getAttributeList().size(); i++) { //cols
            bw.write(table.getAttributeList().get(i));
            bw.write("\t");
        }
        bw.write("\n");
        if(table.getAttributeValues()!=null) {
            for (HashMap value : table.getAttributeValues()) { //rows
                for (int i = 0; i < table.getAttributeList().size(); i++) {
                    bw.write((String) value.get(table.getAttributeList().get(i)));
                    bw.write("\t");
                }
                bw.write("\n");
            }
        }
        bw.flush();
        bw.close();
    }


    public String formatString(ArrayList<String> stringToFormat){
        String formatString;
        StringBuilder stringBuilder = new StringBuilder();
        //turning list to String and putting a tab between each token
        for (String s : stringToFormat) {
            stringBuilder.append(s);
            stringBuilder.append("\t");
        }
        formatString = stringBuilder.toString();
        return formatString;
    }
}
