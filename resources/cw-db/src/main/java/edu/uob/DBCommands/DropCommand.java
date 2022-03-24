package edu.uob.DBCommands;

import edu.uob.Parser;

import java.io.File;

public class DropCommand extends Parser {
    public DropCommand() {
        super("");
    }

    public String dropTable(String tableFile){
        File file = new File(Parser.getCurrentDirectory(), tableFile);
        if(file.exists()){
            file.delete();
            return "[OK]: Table deleted";
        }
        else{
            return "[ERROR]: Table doesn't exist";
        }
    }

    public String dropDatabase(String path){
        File file = new File(path);
        if(file.exists() && file.isDirectory()){
            String[] files = file.list();
            for(String s: files){
                File currentFile = new File(file.getPath(),s);
                currentFile.delete();
            }
            file.delete();
            Parser.setCurrentDirectory(null);
            return "[OK] Database deleted";
        }
        else{
            return "[ERROR] Database does not exist";
        }
    }
}
