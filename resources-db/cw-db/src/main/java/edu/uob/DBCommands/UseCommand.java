package edu.uob.DBCommands;

import edu.uob.Parser;

import java.io.File;
import java.nio.file.Paths;

public class UseCommand extends Parser {

    public UseCommand() {
        super("");
    }

    public String useCommand(){
        String path = Paths.get(".").toAbsolutePath()+ File.separator + Parser.getDBName();
        File databaseDirectory = new File(path);
        if(databaseDirectory.exists()){
            Parser.setCurrentDirectory(databaseDirectory);
            return "[OK]: Current directory: "+Parser.getDBName();
        }
        else{
            return "[ERROR]: Database doesn't exist, please create";
        }
    }
}
