package edu.uob.DBCommands;

import edu.uob.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InsertCommand extends Parser {

    public InsertCommand() {
        super("");
    }

    public String insert(File file, WriteToFile writeToFile, String tableName,
                         ArrayList<String> attributeValues) throws IOException {
        ReadInFile readInFile = new ReadInFile(file);
        DBTable table = new DBTable(readInFile.getAttributeList(), readInFile.getAttributeValues());
        table.addAttributeValues(attributeValues,readInFile.getAttributeList());
        writeToFile.writeAttribListToFile(Parser.getDBName(),tableName,table);
        return "[OK]: values inserted";
    }


}
