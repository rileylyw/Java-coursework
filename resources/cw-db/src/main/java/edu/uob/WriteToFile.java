package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WriteToFile {
    public void writeToFile(DBTable table) throws IOException{
        String path = "../" + table.getTableName() + ".tab";
        File tableFile = new File(path);
        if(tableFile.exists()) {
            System.out.println();
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
}
