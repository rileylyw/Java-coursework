package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ReadInFile {
    private File tableFile;
    private ArrayList<String> attributeList = new ArrayList<>(); //column
//    private ArrayList<ArrayList<String>> attributeValues = new ArrayList<ArrayList<String>>(); //row
    private ArrayList<HashMap<String, String>> attributeValues = new ArrayList<HashMap<String, String>>(); //row

    public void readInFile(String fileName) throws IOException {
        String path = "../" + fileName;
        tableFile = new File(path);
        if (tableFile.exists()) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(tableFile));
                String line = br.readLine();
                String[] tokens = line.split("\\s+");
                attributeList.addAll(Arrays.asList(tokens));
                while ((line = br.readLine()) != null) {
                    String[] tokens2 = line.split("\\s+"); //1, bob, 21, bob@bob.com, 2,
                    HashMap<String, String> pair = new HashMap<String, String>();
                    for(int i=0; i<attributeList.size(); i++) {
                        pair.put(attributeList.get(i), tokens2[i]); //TODO
                    }
                    attributeValues.add(pair);
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

//    public ArrayList<ArrayList<String>> getAttributeValues() { //row values
    public ArrayList<HashMap<String, String>> getAttributeValues() { //row values
        return attributeValues;
    }
}