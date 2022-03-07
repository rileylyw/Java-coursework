package edu.uob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadInFile {
    File databaseFile;
    public ReadInFile(String fileName){
        String path = "../" + fileName;
        databaseFile = new File(path);
    }
    public void readInFile() throws IOException {
        if(databaseFile.exists()){
            try (BufferedReader br = new BufferedReader(new FileReader(databaseFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
}
