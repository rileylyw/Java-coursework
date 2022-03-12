package edu.uob;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ReadInFile {
    private ArrayList<String> attributeList = new ArrayList<>(); //column
    private ArrayList<HashMap<String, String>> attributeValues = new ArrayList<HashMap<String, String>>(); //row

    public void readInFile(String fileName) throws IOException {
        File tableFile;
        String path = "../" + fileName;
        tableFile = new File(path);
        if (tableFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(tableFile));
                String line = br.readLine();
                String[] tokens = line.split("\\s+");
                attributeList.addAll(Arrays.asList(tokens)); //columnNames
                while ((line = br.readLine()) != null) { //row by row
                    if (line.trim().length() > 0) {
                        String[] tokens2 = line.split("\\s+"); //row values
                        HashMap<String, String> pair = new HashMap<String, String>();
                        for (int i = 0; i < attributeList.size(); i++) {
                            pair.put(attributeList.get(i), tokens2[i]);
                        }
                        attributeValues.add(pair);
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> getAttributeList() { //column names
        return attributeList;
    }

    public ArrayList<HashMap<String, String>> getAttributeValues() { //row values
        return attributeValues;
    }
}