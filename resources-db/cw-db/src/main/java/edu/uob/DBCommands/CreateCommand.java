package edu.uob.DBCommands;

import edu.uob.AttributeList;
import edu.uob.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CreateCommand extends Parser {
    AttributeList al = new AttributeList();

    public CreateCommand() {
        super("");
    }

    public String createTable(String tableFile, String tableName) throws IOException {
        File file = new File(Parser.getCurrentDirectory(), tableFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        return "[OK]: Table "+tableName+ " created";
    }

    public boolean populateAttributeList(int index, ArrayList<String> tokens,
                                         ArrayList<String> attributeList) {
        return al.populateAttributeList(index, tokens, attributeList);
    }

    public int getIndex() {
        return al.getIndex();
    }

    public String createDatabase(){
        String path = Paths.get(".").toAbsolutePath()+ File.separator + Parser.getDBName();
        File databaseDirectory = new File(path);
        if(databaseDirectory.exists()) {
            return "[ERROR]: Database "+Parser.getDBName()+" already exists";
        }
        databaseDirectory.mkdir(); //create directory
        return "[OK]: Database "+Parser.getDBName()+" created";
    }
}